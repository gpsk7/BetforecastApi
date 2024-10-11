package net.javaguides.springboot.RuleEngine.Util;

import net.javaguides.springboot.dto.RaceDto;
import net.javaguides.springboot.model.Race;

public class RuleEngineeUtil {
    public static int calculateWordSum(String word) {
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
    public static RaceDto mapToRaceDto(Race race, String predictionColor){
        RaceDto raceDto=  RaceDto.builder().raceId(race.getRaceId())
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
                .sdHorseBallot(Integer.parseInt(race.getHorseBallot()))
                .sdDrawnStall(race.getDrawnStall())
                .build();
        return raceDto;
    }
}