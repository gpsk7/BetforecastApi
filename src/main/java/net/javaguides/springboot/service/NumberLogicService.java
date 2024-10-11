package net.javaguides.springboot.service;//package net.javaguides.springboot.service;
//
import net.javaguides.springboot.dto.RaceDto;
import net.javaguides.springboot.model.Race;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NumberLogicService {
    @Autowired
    private NatureDayValueService natureDayValueService;

    public RaceDto placeBet(Race race) {
        int horseBallot = Integer.parseInt(race.getHorseBallot());
        int dayValue = natureDayValueService.getNatureDayValue(race.getDate()).getDayValue();
        int horseCount = calculateWordSum(race.getHorseName());
        int jockeyCount = calculateWordSum(race.getJockeyName());
        double odds;
        int drawnStall = race.getDrawnStall();
        if (drawnStall > 9) {
            drawnStall = NumberLogicService.reduceToSingleDigit(drawnStall);
        }
        if(horseBallot>9){
            horseBallot = NumberLogicService.reduceToSingleDigit(horseBallot);
        }
        // if horse ballot is equal to today's day value directly given green all other ballot numbers are checked
        if(horseBallot == dayValue){
            return mapToRaceDto(race, "Green");
        }
        // First block of conditions (2,7,11,16,20)
        else if (horseBallot == 2 || horseBallot == 7 ) {
            if (drawnStall == 2 || drawnStall == 11 || drawnStall == 7 || drawnStall == 9 || drawnStall == 5) {
                if (dayValue == 2 || horseCount == 2 || jockeyCount == 2) {
                    return mapToRaceDto(race, "Green");
                }
                return mapToRaceDto(race, "yellow");
            }
        }         // second block of horse ballot conditions (1)
        else if(horseBallot == 1){
            if (drawnStall == 4 ) {
                if (isValueInList(dayValue, 1,8) || isValueInList(horseCount, 1,8) || isValueInList(jockeyCount, 1,8)) {
                    return mapToRaceDto(race, "Green");
                }
                return mapToRaceDto(race, "yellow");
            }
        }
        // third block of horse ballot conditions (4)
        else if( horseBallot ==4 ){
            if (drawnStall == 2 || drawnStall == 1 ) {
                if (isValueInList(dayValue, 4,8) || isValueInList(horseCount, 4,8) || isValueInList(jockeyCount, 4,8)) {
                    return mapToRaceDto(race, "Green");
                }
                return mapToRaceDto(race, "yellow");
            }
        }
        // fourth block of conditions for horseBallot 3 and 6
        else if (horseBallot == 3 || horseBallot == 6) {
            if (drawnStall == 3 || drawnStall == 6) {
                if (isValueInList(dayValue, 3, 6) || isValueInList(horseCount, 3, 6) || isValueInList(jockeyCount, 3, 6) || isValueInList(3, 6)) {
                    return mapToRaceDto(race, "Green");
                }
                return mapToRaceDto(race, "yellow");
            }
        }
        else if (horseBallot == 5) {
            if (drawnStall == 5 || drawnStall == 2 || drawnStall == 7 || drawnStall == 9) {
                if (dayValue == 5 || horseCount == 5 || jockeyCount == 5) {
                    return mapToRaceDto(race, "Green");
                }
                return mapToRaceDto(race, "yellow");
            }
        }
        else if (horseBallot == 9) {
            if (drawnStall == 9 || drawnStall == 8) {
                if (dayValue == 9 || horseCount == 9 || jockeyCount == 9) {
                    return mapToRaceDto(race, "Green");
                }
                return mapToRaceDto(race, "yellow");
            }
        }
        else if (horseBallot == 8) {
            if (drawnStall == 8) {
                if (dayValue == 8 || horseCount == 8 || jockeyCount == 8) {
                    return mapToRaceDto(race, "Green");
                }
                return mapToRaceDto(race, "yellow");
            }
        }

        // If no conditions are met
        return mapToRaceDto(race, "Red");
    }


    public List<RaceDto> prediction(List<Race> races) {
        List<RaceDto> raceDtos = races.stream().map(this::placeBet).collect(Collectors.toList());
        return raceDtos;
    }

    public int calculateWordSum(String word) {
        word = word.toUpperCase();

        int sum = 0;

        // Calculate the sum of letter values.
        for (char letter : word.toCharArray()) {
            if (letter >= 'A' && letter <= 'Z') {
                sum += letter - 'A' + 1; // 'A' is 65 in ASCII, so 'A' - 'A' + 1 = 1, 'B' - 'A' + 1 = 2, etc.
            }
        }

        // Reduce to single digit.
        return reduceToSingleDigit(sum);
    }

    public static int reduceToSingleDigit(int number) {
        while (number > 9) {
            number = sumOfDigits(number);
        }
        return number;
    }

    public static int sumOfDigits(int number) {
        int sum = 0;
        while (number > 0) {
            sum += number % 10;
            number /= 10;
        }
        return sum;
    }

        private String betDecision(double odds) {
            if (odds <= 7) {
                return "Place a win/place bet";
            } else if (odds >= 8) {
                return "Don’t place a bet";
            }
            return "No action taken";
        }

    private boolean isValueInList(int value, int... values) {
        for (int v : values) {
            if (value == v) {
                return true;
            }
        }
        return false;
    }

    private RaceDto mapToRaceDto(Race race, String predictionColor) {
        RaceDto raceDto = RaceDto.builder().raceId(race.getRaceId())
                .date(race.getDate())
                .raceNumber(race.getRaceNumber())
                .country(race.getCountry())
                .course(race.getCourse())
                .jockeyName(race.getJockeyName())
                .finishPos(race.getFinishPos())
                .dayValue(race.getDayValue())
                .time(race.getTime())
                .trainerName(race.getTrainerName())
                .horseBallot(race.getHorseBallot())
                .predictionColor(predictionColor)
                .speed(race.getSpeed())
                .raceNumber(race.getRaceNumber())
                .horseName(race.getHorseName())
                .drawnStall(race.getDrawnStall())
                .horseWeight(race.getHorseWeight())
                .age(race.getAge())
                .distance(race.getDistance())
                .build();
        return raceDto;
    }
}

//import net.javaguides.springboot.dto.RaceDto;
//import net.javaguides.springboot.model.Race;
//import net.javaguides.springboot.rulesengine.HorseBallotEqualsDayValueRule;
//import net.javaguides.springboot.rulesengine.HorseBallotInRangeRule;
//import net.javaguides.springboot.service.NatureDayValueService;
//import org.jeasy.rules.api.Rules;
//import org.jeasy.rules.api.Facts;
//import org.jeasy.rules.core.DefaultRulesEngine;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class NumberLogicService {
//    @Autowired
//    private NatureDayValueService natureDayValueService;
//
//    public RaceDto placeBet(Race race) {
//        int horseBallot = Integer.parseInt(race.getHorseBallot());
//        int dayValue = natureDayValueService.getNatureDayValue(race.getDate()).getDayValue();
//        int horseCount = calculateWordSum(race.getHorseName());
//        int jockeyCount = calculateWordSum(race.getJockeyName());
//        int drawnStall = race.getDrawnStall();
//        if (drawnStall > 9) {
//            drawnStall = NumberLogicService.reduceToSingleDigit(drawnStall);
//        }
//        if(horseBallot>9){
//            horseBallot = NumberLogicService.reduceToSingleDigit(horseBallot);
//        }
//
//        // Create a RaceDto object to hold the result
//        RaceDto raceDto = mapToRaceDto(race, "Red");
//
//        // Create Easy Rules engine and add rules
//        Facts facts = new Facts();
//        facts.put("horseBallot", horseBallot);
//        facts.put("dayValue", dayValue);
//        facts.put("drawnStall", drawnStall);
//        facts.put("horseCount", horseCount);
//        facts.put("jockeyCount", jockeyCount);
//        facts.put("race", raceDto);
//
//        Rules rules = new Rules();
//        rules.register(new HorseBallotEqualsDayValueRule());
//        rules.register(new HorseBallotInRangeRule());
//        // Add other rules as needed
//
//        DefaultRulesEngine rulesEngine = new DefaultRulesEngine();
//        rulesEngine.fire(rules, facts);
//
//        return raceDto;
//    }
//    public int calculateWordSum(String word) {
//        word = word.toUpperCase();
//
//        int sum = 0;
//
//        // Calculate the sum of letter values.
//        for (char letter : word.toCharArray()) {
//            if (letter >= 'A' && letter <= 'Z') {
//                sum += letter - 'A' + 1; // 'A' is 65 in ASCII, so 'A' - 'A' + 1 = 1, 'B' - 'A' + 1 = 2, etc.
//            }
//        }
//
//        // Reduce to single digit.
//        return reduceToSingleDigit(sum);
//    }
//
//    public static int reduceToSingleDigit(int number) {
//        while (number > 9) {
//            number = sumOfDigits(number);
//        }
//        return number;
//    }
//
//    public static int sumOfDigits(int number) {
//        int sum = 0;
//        while (number > 0) {
//            sum += number % 10;
//            number /= 10;
//        }
//        return sum;
//    }
//        private RaceDto mapToRaceDto(Race race, String predictionColor) {
//        RaceDto raceDto = RaceDto.builder().raceId(race.getRaceId())
//                .date(race.getDate())
//                .raceNumber(race.getRaceNumber())
//                .country(race.getCountry())
//                .course(race.getCourse())
//                .jockeyName(race.getJockeyName())
//                .finishPos(race.getFinishPos())
//                .dayValue(race.getDayValue())
//                .time(race.getTime())
//                .trainerName(race.getTrainerName())
//                .horseBallot(race.getHorseBallot())
//                .predictionColor(predictionColor)
//                .speed(race.getSpeed())
//                .raceNumber(race.getRaceNumber())
//                .horseName(race.getHorseName())
//                .drawnStall(race.getDrawnStall())
//                .horseWeight(race.getHorseWeight())
//                .age(race.getAge())
//                .distance(race.getDistance())
//                .build();
//        return raceDto;
//    }
//    public List<RaceDto> prediction(List<Race> races) {
//        return races.stream().map(this::placeBet).collect(Collectors.toList());
//    }
//}
