package net.javaguides.springboot.service;

import net.javaguides.springboot.dto.RaceDto;
import net.javaguides.springboot.exception.ApiException;
import net.javaguides.springboot.model.Astrology;
import net.javaguides.springboot.model.Race;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AstrologyLogicService {
    @Autowired
    private AstrologyService astrologyService;

    @Autowired
    private NatureDayValueService natureDayValueService;
    private final static  Map<String,String> planetNumber = Map.of("saturn","8","jupiter","3","rahu","4","mars","9","sun","1","venus","6","ketu","7","mercury","5","moon","2");

    public List<RaceDto> prediction(List<Race> races, String date,String time) throws ApiException {
       Astrology astrology = astrologyService.findByDateAndTime(date, time);
       if(astrology == null){
           throw new ApiException("Dasha or AntarDasha not found for this date ", HttpStatus.BAD_REQUEST);
       }
       List<String> planetNumbers = getPlanetNumber(astrology);
         System.out.println("nakstranumbers: "+ planetNumbers);
      List<RaceDto> raceDtos = races.stream().map(race -> {
          int ballotNumber = Integer.parseInt(race.getHorseBallot());
          if(ballotNumber > 9){
              if(planetNumbers.contains(String.valueOf(NumberLogicService.reduceToSingleDigit(ballotNumber)))){
                  return mapToRaceDto(race,"Green");
              }
          }
           if(planetNumbers.contains(race.getHorseBallot())){
               return mapToRaceDto(race,"Green");
           }
           return mapToRaceDto(race,"Red");
       }).collect(Collectors.toList());

        return raceDtos;
    }
    public List<String > getPlanetNumber(Astrology astrology){
        try{
            List<String> planetNumbers = List.of(planetNumber.get(astrology.getDasha().toLowerCase()),planetNumber.get(astrology.getAntarDasha().toLowerCase()),planetNumber.get(astrology.getOwnHouse())) ;
            return planetNumbers;
        }
        catch (NullPointerException e){
            throw new ApiException("ASTROLOGY DATA IS MISSING FOR GIVEN DATE "+astrology.getDate()+" AND TIME " + astrology.getTime(),HttpStatus.BAD_REQUEST);
        }
    }
    public RaceDto mapToRaceDto(Race race, String predictionColor){
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
                .build();
        return raceDto;
    }


}
