package net.javaguides.springboot.service;


import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.transaction.Transactional;
import net.javaguides.springboot.RuleEngine.Astrology.ApplyRules;
import net.javaguides.springboot.WebRequest.master.RaceResponse;
import net.javaguides.springboot.dto.HorseNameCountResponse;
import net.javaguides.springboot.dto.RaceDto;
import net.javaguides.springboot.dto.RaceInfo;
import net.javaguides.springboot.exception.ApiException;
import net.javaguides.springboot.model.Race;
import net.javaguides.springboot.repository.RaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.print.Pageable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RaceService {

    @Autowired
    private RaceRepository raceRepository;
    @Autowired
    private NumberLogicService numberLogicService;

    @Autowired
    private PedigreeLogicService pedigreeLogicService;

    @Autowired
    AstrologyLogicService astrologyLogicService;

    @Autowired
    ApplyRules applyRules;

    public List<String> getCountriesByDate(String date) {
        return raceRepository.findByDate(date).stream()
                .map(Race::getCountry)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> getCountriesByYearAndMonth(String month, String year) {
        return raceRepository.findDistinctCountriesByMonthAndYear(month,year);
    }

    public List<String> getCoursesByCountryAndDate(String country, String date) {
        return raceRepository.findByCountryAndDate(country, date).stream()
                .map(Race::getCourse)
                .distinct()
                .collect(Collectors.toList());
    }


    public List<RaceInfo> getDistinctRaceNumberAndTimeByCountryAndDateAndCourse(String country, String date, String course) {
        return raceRepository.findDistinctRaceNumberAndTimeByCountryAndDateAndCourse(country, date, course);
    }

    public List<Race> getDetailsByCountryAndDateAndCourseAndRaceNumber(String country, String date, String course, String raceNumber) {
        return raceRepository.findByCountryAndDateAndCourseAndRaceNumber(country, date, course, raceNumber);
    }
    public List<RaceDto> getDetailsByCountryAndDateAndCourseAndRaceNumberNumerology(String country, String date, String course, String raceNumber) {
       List<Race> races = raceRepository.findByCountryAndDateAndCourseAndRaceNumber(country, date, course, raceNumber);
        System.out.println(races);
       if(!races.isEmpty()) {
           return numberLogicService.prediction(races);
       }
       return null;
    }
    public List<RaceDto> getDetailsByCountryAndDateAndCourseAndRaceNumberAstrology(String country, String date, String course, String raceNumber) {
        List<Race> races = raceRepository.findByCountryAndDateAndCourseAndRaceNumber(country, date, course, raceNumber);
        if(!races.isEmpty()) {
            return applyRules.prediction(races,date,races.get(0).getTime());
        }
        return null;
    }

    public List<RaceDto> getDetailsByCountryAndDateAndCourseAndRaceNumber3(String country, String date, String course, String raceNumber) {
        List<Race> races = raceRepository.findByCountryAndDateAndCourseAndRaceNumber(country, date, course, raceNumber);
        if(!races.isEmpty()){
            return pedigreeLogicService.prediction(races);
        }
        return null;
    }

    public List<HorseNameCountResponse> getHorseNameCountsByRace(String country, String date, String course, String raceNumber) {
            List<Race> races = raceRepository.findByCountryAndDateAndCourseAndRaceNumber(country, date, course, raceNumber);
            return races.stream()
                    .map(this::mapToHorseNameCountResponse)
                    .collect(Collectors.toList());
        }

    private HorseNameCountResponse mapToHorseNameCountResponse(Race race) {
        HorseNameCountResponse response = new HorseNameCountResponse();
        int horseNameCount = calculateNameCount(race.getHorseName());
        int jockeyNameCount = calculateNameCount(race.getJockeyName());
        int trainerNameCount = calculateNameCount(race.getTrainerName());

        response.setHorseName( race.getHorseName()  );
        response.setHorseNameCount(reduceToSingleDigit(horseNameCount));

        response.setJockeyName(race.getJockeyName() );
        response.setJockeyNameCount(reduceToSingleDigit(jockeyNameCount));

        response.setTrainerName(race.getTrainerName() );
        response.setTrainerNameCount(reduceToSingleDigit(trainerNameCount));

        return response;
    }

    private int calculateNameCount(String name) {
        return name.chars()
                .map(Character::toLowerCase)
                .filter(Character::isLetter)
                .map(c -> c - 'a' + 1)
                .sum();
    }

    private int reduceToSingleDigit(int number) {
        while (number >= 10) {
            number = String.valueOf(number)
                    .chars()
                    .map(Character::getNumericValue)
                    .sum();
        }
        return number;
    }
    public Race saveRace(Race race) {
        return raceRepository.save(race);
    }

/*    public Race findByHorseName(String horseName) {
        return raceRepository.findByHorseName(horseName);
    }*/

    public Race createRace(RaceDto raceDto) throws Exception {
//        Race race = raceRepository.findByHorseName(raceDto.getHorseName());

        try {
         Race   race = Race.builder()
                    .id(raceDto.getId())
                    .date(raceDto.getDate())
                    .course(raceDto.getCourse())
                    .country(raceDto.getCountry())
                    .time(raceDto.getTime())
                    .raceId(raceDto.getRaceId())
                    .horseName(raceDto.getHorseName())
                    .jockeyName(raceDto.getJockeyName())
                    .trainerName(raceDto.getTrainerName())
                    .speed(raceDto.getSpeed())
                    .horseBallot(raceDto.getHorseBallot())
                    .finishPos(raceDto.getFinishPos())
                    .dayValue(raceDto.getDayValue())
                    .raceNumber(raceDto.getRaceNumber())
                    .age(raceDto.getAge())
                    .distance(raceDto.getDistance())
                    .horseWeight(raceDto.getHorseWeight())
                    .drawnStall(raceDto.getDrawnStall())
                    .build();
            return raceRepository.save(race);
        } catch (Exception exception) {
            throw new ApiException(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public Optional<Race> findById(Long id) {
        return raceRepository.findById(id);
    }

    public Race updateRace(RaceDto raceDto) throws Exception {
        Optional<Race> existingRace = raceRepository.findById(raceDto.getId());
        if(existingRace.isEmpty()){
            throw new ApiException("NO DATA FOUND WITH THIS ID" +raceDto.getId(),HttpStatus.BAD_REQUEST);
        }
        Race race = existingRace.get();
        race.setDate(raceDto.getDate());
        race.setCourse(raceDto.getCourse());
        race.setCountry(raceDto.getCountry());
        race.setTime(raceDto.getTime());
        race.setRaceId(raceDto.getRaceId());
        race.setHorseName(raceDto.getHorseName());
        race.setJockeyName(raceDto.getJockeyName());
        race.setTrainerName(raceDto.getTrainerName());
        race.setSpeed(raceDto.getSpeed());
        race.setHorseBallot(raceDto.getHorseBallot());
        race.setFinishPos(raceDto.getFinishPos());
        race.setDayValue(raceDto.getDayValue());
        race.setRaceNumber(raceDto.getRaceNumber());
        race.setAge(raceDto.getAge());
        race.setDistance(raceDto.getDistance());
        race.setHorseWeight(raceDto.getHorseWeight());
        race.setDrawnStall(raceDto.getDrawnStall());

        return raceRepository.save(race);
    }
    public Page<Race> findAll(Pageable pageRequest) {
        return raceRepository.findAll((org.springframework.data.domain.Pageable) pageRequest);
    }

    public RaceResponse findAllRace(int page, int size) {
        Page<Race> races = raceRepository.findAll(PageRequest.of(page, size));

        List<RaceDto> raceDtos = races.getContent().stream().map(raceName ->
                RaceDto.builder()
                        .id(raceName.getId())
                        .date(raceName.getDate())
                        .course(raceName.getCourse())
                        .country(raceName.getCountry())
                        .time(raceName.getTime())
                        .raceId(raceName.getRaceId())
                        .horseName(raceName.getHorseName())
                        .jockeyName(raceName.getJockeyName())
                        .trainerName(raceName.getTrainerName())
                        .speed(raceName.getSpeed())
                        .horseBallot(raceName.getHorseBallot())
                        .finishPos(raceName.getFinishPos())
                        .dayValue(raceName.getDayValue())
                        .raceNumber(raceName.getRaceNumber())
                        .distance(raceName.getDistance())
                        .horseWeight(raceName.getHorseWeight())
                        .age(raceName.getAge())
                        .drawnStall(raceName.getDrawnStall())
                        .build()
        ).collect(Collectors.toList());
        return RaceResponse.builder().raceDtos(raceDtos).totalCount(races.getTotalElements()).build();
    }
    @Transactional
//    public void uploadCSVFile(MultipartFile file) throws IOException {
//
//        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
//             CSVReader csvReader = new CSVReader(reader)) {
//
//            String[] line;
//            csvReader.readNext();
//
//            while ((line = csvReader.readNext()) != null) {
//                Race race = new Race();
//
//                try {
//                    race.setDate(line[0]);
//                    race.setCourse(line[1]);
//                    race.setCountry(line[2]);
//                    race.setTime(line[3]);
//                    race.setRaceId(line[4]);
//                    race.setHorseName(line[5]);
//                    race.setJockeyName(line[6]);
//                    race.setTrainerName(line[7]);
//                    race.setSpeed(Integer.parseInt(line[8]));
//                    race.setHorseBallot(line[9]);
//                    race.setFinishPos(line[10]);
//                    race.setDayValue(Integer.parseInt(line[11]));
//                    race.setRaceNumber(line[12]);
//                    race.setAge(Integer.parseInt(line[13]));
//                    race.setDistance(Integer.parseInt(line[14]));
//                    race.setHorseWeight(Integer.parseInt(line[15]));
//                    race.setDrawnStall(Integer.parseInt(line[16]));
//                    race.setId(Long.parseLong(line[17]));
//
//                    raceRepository.save(race);
//                } catch (NumberFormatException exception) {
//                    throw new RuntimeException("Invalid number format in line: " + String.join(",", line), exception);
//                }
//            }
//        } catch (IOException | CsvValidationException exception) {
//            throw new RuntimeException("Failed to process CSV file: ", exception);
//        }
//    }
    public void uploadCSVFile(MultipartFile file) throws IOException {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
             CSVReader csvReader = new CSVReader(reader)) {

            String[] line;
            csvReader.readNext(); // Skip header
             int rowNumber=1;


            while ((line = csvReader.readNext()) != null) {
                rowNumber++;
                Race race = new Race();

                try {
                    race.setDate(line[0]);
                    race.setCourse(line[1]);
                    race.setCountry(line[2]);
                    race.setTime(line[3]);
                    race.setRaceId(line[4]);
                    race.setHorseName(line[5]);
                    race.setJockeyName(line[6]);
                    race.setTrainerName(line[7]);
                    race.setSpeed(Integer.parseInt(line[8]));
                    race.setHorseBallot(line[9]);
                    race.setFinishPos(line[10]);
                    race.setDayValue(Integer.parseInt(line[11]));
                    race.setRaceNumber(line[12]);
                    race.setAge(Integer.parseInt(line[13]));
                    race.setDistance(Integer.parseInt(line[14]));
                    race.setHorseWeight(Integer.parseInt(line[15]));
                    race.setDrawnStall(Integer.parseInt(line[16]));

                    // id is autogenerated, so we don't set it from CSV
                    raceRepository.save(race);
                } catch (NumberFormatException exception) {
                    throw new RuntimeException("Invalid number format in line number : "+rowNumber + String.join(",", line), exception);
                }
            }
        } catch (IOException | CsvValidationException exception) {
            throw new RuntimeException("Failed to process CSV file: ", exception);
        }
    }
//        public RaceResponse findAllRaceInReverseOrder(int page, int size) {
//            Page<Race> races = raceRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
//
//            List<RaceDto> raceDtos = races.getContent().stream().map(raceName ->
//                    RaceDto.builder()
//                            .id(raceName.getId())
//                            .date(raceName.getDate())
//                            .course(raceName.getCourse())
//                            .country(raceName.getCountry())
//                            .time(raceName.getTime())
//                            .raceId(raceName.getRaceId())
//                            .horseName(raceName.getHorseName())
//                            .jockeyName(raceName.getJockeyName())
//                            .trainerName(raceName.getTrainerName())
//                            .speed(raceName.getSpeed())
//                            .horseBallot(raceName.getHorseBallot())
//                            .finishPos(raceName.getFinishPos())
//                            .dayValue(raceName.getDayValue())
//                            .raceNumber(raceName.getRaceNumber())
//                            .build()
//            ).collect(Collectors.toList());
//            Collections.reverse(raceDtos);
//            return RaceResponse.builder().raceDtos(raceDtos).totalCount(races.getTotalElements()).build();
//        }

    public RaceResponse findAllRaceInReverseOrder(int page, int size) {
        Page<Race> races = raceRepository.findAllByOrderByIdDesc(PageRequest.of(page, size));

        List<RaceDto> raceDtos = races.getContent().stream().map(raceName ->
                RaceDto.builder()
                        .id(raceName.getId())
                        .date(raceName.getDate())
                        .course(raceName.getCourse())
                        .country(raceName.getCountry())
                        .time(raceName.getTime())
                        .raceId(raceName.getRaceId())
                        .horseName(raceName.getHorseName())
                        .jockeyName(raceName.getJockeyName())
                        .trainerName(raceName.getTrainerName())
                        .speed(raceName.getSpeed())
                        .horseBallot(raceName.getHorseBallot())
                        .finishPos(raceName.getFinishPos())
                        .dayValue(raceName.getDayValue())
                        .raceNumber(raceName.getRaceNumber())
                        .age(raceName.getAge())
                        .drawnStall(raceName.getDrawnStall())
                        .horseWeight(raceName.getHorseWeight())
                        .distance(raceName.getDistance())
                        .build()
        ).collect(Collectors.toList());
        Collections.reverse(raceDtos);
        return RaceResponse.builder().raceDtos(raceDtos).totalCount(races.getTotalElements()).build();
    }

public void delete(Long id) {
    Optional<Race> existingRace = raceRepository.findById(id);
    if (existingRace.isEmpty()) {
        throw new ApiException("No data found with ID " + id, HttpStatus.BAD_REQUEST);
    }
    raceRepository.deleteById(id);
}
}


