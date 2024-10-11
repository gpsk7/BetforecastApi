package net.javaguides.springboot.service.master;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import net.javaguides.springboot.WebRequest.master.HorseResponse;
import net.javaguides.springboot.dto.RaceDto;
import net.javaguides.springboot.dto.master.HorseDto;
import net.javaguides.springboot.exception.ApiException;
import net.javaguides.springboot.model.master.Horse;
import net.javaguides.springboot.model.master.Jockey;
import net.javaguides.springboot.repository.master.HorseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HorseService {

    @Autowired
    HorseRepository repository;

    public Horse saveHorse(Horse horse) {
        return repository.save(horse);
    }

    public Horse findByName(String name) {
        return repository.findByName(name);
    }

    public Horse createHorseMaster(HorseDto horseDto) {
        Horse horse = repository.findByName(horseDto.getName());
        try {
            horse = Horse.builder()
                    .name(horseDto.getName())
                    .breed(horseDto.getBreed())
                    .gender(horseDto.getGender())
                    .color(horseDto.getColor())
                    .age(horseDto.getAge())
                    .height(horseDto.getHeight())
                    .weight(horseDto.getWeight())
                    .registrationNumber(horseDto.getRegistrationNumber())
                    .count(horseDto.getCount())
                    .country(horseDto.getCountry())
                    .speed(horseDto.getSpeed())
                    .dam(horseDto.getDam())
                    .sire(horseDto.getSire())
                    .build();
            return repository.save(horse);
        } catch (Exception exception) {
            throw new ApiException(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public void uploadCSVFile(MultipartFile file) throws IOException {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
             CSVReader csvReader = new CSVReader(reader)) {

            String[] line;
            csvReader.readNext(); // Skip header row
            int rowNumber = 1;

            while ((line = csvReader.readNext()) != null) {
                rowNumber++;
                Horse horse = new Horse();
                try {
                    horse.setName(line[0]);
                    horse.setBreed(line[1]);
                    horse.setGender(line[2]);
                    horse.setColor(line[3]);
                    horse.setAge(Integer.parseInt(line[4]));
                    horse.setHeight(Float.parseFloat(line[5]));
                    horse.setWeight(Float.parseFloat(line[6]));
                    horse.setRegistrationNumber(Integer.parseInt(line[7]));
                    horse.setCount(Integer.parseInt(line[8]));
                    horse.setCountry(line[9]);
                    horse.setSpeed(Integer.parseInt(line[10]));
                    horse.setDam(line[11]);
                    horse.setSire(line[12]);

                    repository.save(horse);
                } catch (NumberFormatException e) {
                    throw new RuntimeException("Invalid number format in line number : " + rowNumber + String.join(",", line), e);
                }
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException("Failed to process CSV file", e);
        }
    }

    public Optional<Horse> findById(Long id) {
        return repository.findById(id);
    }

    public Horse updateHorseMaster(HorseDto horseDto) {
        Optional<Horse> existingHorse = repository.findById(horseDto.getId());

        if (existingHorse.isEmpty()) {
            throw new ApiException("NO DATA FOUND WITH THIS ID" + horseDto.getId(), HttpStatus.BAD_REQUEST);
        }
        Horse horse = existingHorse.get();
        horse.setName(horseDto.getName());
        horse.setBreed(horseDto.getBreed());
        horse.setGender(horseDto.getGender());
        horse.setColor(horseDto.getColor());
        horse.setAge(horseDto.getAge());
        horse.setHeight(horseDto.getHeight());
        horse.setWeight(horseDto.getWeight());
        horse.setRegistrationNumber(horseDto.getRegistrationNumber());
        horse.setCount(horseDto.getCount());
        horse.setCountry(horseDto.getCountry());
        horse.setSpeed(horseDto.getSpeed());
        horse.setDam(horseDto.getDam());
        horse.setSire(horseDto.getSire());

        return repository.save(horse);
    }

    public Page<Horse> findAll(Pageable pageRequest) {
        return repository.findAll(pageRequest);
    }

    public HorseResponse findAllHorse(int page, int size) {
        Page<Horse> horses = repository.findAll(PageRequest.of(page, size));

        List<HorseDto> horseDtos = horses.getContent().stream().map(horseName ->
                HorseDto.builder()
                        .id(horseName.getId())
                        .name(horseName.getName())
                        .breed(horseName.getBreed())
                        .gender(horseName.getGender())
                        .color(horseName.getColor())
                        .age(horseName.getAge())
                        .height(horseName.getHeight())
                        .weight(horseName.getWeight())
                        .registrationNumber(horseName.getRegistrationNumber())
                        .count(horseName.getCount())
                        .country(horseName.getCountry())
                        .speed(horseName.getSpeed())
                        .dam(horseName.getDam())
                        .sire(horseName.getSire())
                        .build()
        ).collect(Collectors.toList());
        return HorseResponse.builder().horseDtos(horseDtos).totalCount(horses.getTotalElements()).build();
    }

    public List<HorseDto> search(String name) {
        List<HorseDto> horseDtos = new ArrayList<>();
        repository.findByLike(name).stream().forEach(horse -> {
            HorseDto horseDto = HorseDto.builder()
                    .id(horse.getId())
                    .name(horse.getName())
                    .breed(horse.getBreed())
                    .gender(horse.getGender())
                    .color(horse.getColor())
                    .age(horse.getAge())
                    .height(horse.getHeight())
                    .weight(horse.getWeight())
                    .registrationNumber(horse.getRegistrationNumber())
                    .count(horse.getCount())
                    .country(horse.getCountry())
                    .speed(horse.getSpeed())
                    .dam(horse.getDam())
                    .sire(horse.getSire())
                    .build();
            horseDtos.add(horseDto);
        });
        return horseDtos;
    }
    public void delete(Long id) {
        Optional<Horse> existingHorse = repository.findById(id);
        if (existingHorse.isEmpty()) {
            throw new ApiException("No data found with ID " + id, HttpStatus.BAD_REQUEST);
        }
        repository.deleteById(id);
    }
}
