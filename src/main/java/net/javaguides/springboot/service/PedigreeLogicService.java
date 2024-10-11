package net.javaguides.springboot.service;

import net.javaguides.springboot.dto.RaceDto;
import net.javaguides.springboot.exception.ApiException;
import net.javaguides.springboot.model.MoonDay;
import net.javaguides.springboot.model.Race;
import net.javaguides.springboot.repository.MoonDayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PedigreeLogicService {

    @Autowired
    private MoonDayRepository moonDayRepository;

    public List<RaceDto> prediction(List<Race> races) {
        return races.stream()
                .map(race -> pedigree(race, races))  // Pass the entire race list to pedigree
                .collect(Collectors.toList());
    }

    public RaceDto pedigree(Race race, List<Race> races) {
        String predictionColor = "Red";
        int count = 0;


        Optional<MoonDay> moonDayOptional = moonDayRepository.findByDate(race.getDate());

//        if(moonDayOptional.isEmpty()){
//            throw new ApiException("NO MOON DATA FOUND WITH THIS DATE", HttpStatus.NOT_FOUND);
//        }

        if (moonDayOptional.isPresent()) {
            MoonDay moonDay = moonDayOptional.get();
            String phase = moonDay.getMoonDay();

            if ("Amavasya".equals(phase)) {
                // Bet on top-weight horses during New Moon
                int maxWeight = findMaxWeightedHorse(races);
                if (race.getHorseWeight() == maxWeight) {
                    return mapToRaceDto(race, "Green");
                }
            } else if ("Poornima".equals(phase)) {
                // Bet on bottom-weight horses during Full Moon
                int leastWeight = findLeastWeightedHorse(races);
                if (race.getHorseWeight() == leastWeight) {
                    return mapToRaceDto(race, "Green");
                }
            }
        }


        if (race.getDistance() > 1600) {
            int leastWeight = findLeastWeightedHorse(races);
            if (race.getHorseWeight() == leastWeight) {
                return mapToRaceDto(race, "Green");
            }
        }

        if (race.getAge() < 9) {
            count++;
        }

        if (race.getAge() > 9) {
            return mapToRaceDto(race, "Red");  // Return with "No Bet" color or label
        }

//        if (count > 0) {
//            return mapToRaceDto(race, "Green");
//        }

        return mapToRaceDto(race, predictionColor);
    }

    private int findLeastWeightedHorse(List<Race> races) {
        return races.stream()
                .mapToInt(Race::getHorseWeight)
                .min()
                .orElseThrow(() -> new IllegalArgumentException("Race list is empty"));
    }

    private int findMaxWeightedHorse(List<Race> races) {
        return races.stream()
                .mapToInt(Race::getHorseWeight)
                .max()
                .orElseThrow(() -> new IllegalArgumentException("Race list is empty"));
    }


    private RaceDto mapToRaceDto(Race race, String predictionColor) {
        return RaceDto.builder()
                .raceId(race.getRaceId())
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
                .horseName(race.getHorseName())
                .drawnStall(race.getDrawnStall())
                .horseWeight(race.getHorseWeight())
                .age(race.getAge())
                .distance(race.getDistance())
                .build();
    }
}
//public class PedigreeLogicService {
//
//    public List<RaceDto> prediction(List<Race> races){
//     return races.stream().map(this::pedigree).collect(Collectors.toList());
//    }
//
//    public RaceDto pedigree(Race race){
//        int count =0;
//        if(race.getAge() < 9){
//            count++;
//        }
//
//
//
//
//
//        if(count >0){
//            return mapToRaceDto(race,"Green");
//        }
//      return mapToRaceDto(race,"Red");
//    }
//
//    private RaceDto mapToRaceDto(Race race, String predictionColor){
//        RaceDto raceDto=  RaceDto.builder().raceId(race.getRaceId())
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
//                .build();
//        return raceDto;
//    }}
