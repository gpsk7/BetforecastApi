package net.javaguides.springboot.service.master;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import net.javaguides.springboot.WebRequest.master.TrainerResponse;
import net.javaguides.springboot.dto.master.JockeyDto;
import net.javaguides.springboot.dto.master.TrainerDto;
import net.javaguides.springboot.exception.ApiException;
import net.javaguides.springboot.model.master.Jockey;
import net.javaguides.springboot.model.master.Trainer;
import net.javaguides.springboot.repository.master.TrainerRepository;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TrainerService {

    @Autowired
    TrainerRepository repository;

    public Trainer saveTrainer(Trainer trainer) {
        return repository.save(trainer);
    }

    public Trainer findByTrainerName(String trainerName) {
        return repository.findByTrainerName(trainerName);
    }

    public Trainer createTrainerMaster(TrainerDto trainerDto) {
        Trainer trainer = repository.findByTrainerName(trainerDto.getTrainerName());
        try {
            trainer = Trainer.builder()
                    .trainerName(trainerDto.getTrainerName())
                    .trainerCount(trainerDto.getTrainerCount())
                    .trainerRegistrationId(trainerDto.getTrainerRegistrationId())
                    .trainerCountry(trainerDto.getTrainerCountry())
                    .trainerStable(trainerDto.getTrainerStable())
                    .trainerAge(trainerDto.getTrainerAge())
                    .trainerGender(trainerDto.getTrainerGender())
                    .build();
            return repository.save(trainer);
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
            int rowNumber=1;

            while ((line = csvReader.readNext()) != null) {
                rowNumber++;
                Trainer trainer = new Trainer();
                try {
                    trainer.setTrainerName(line[0]);
                    trainer.setTrainerCount(Integer.parseInt(line[1]));
                    trainer.setTrainerRegistrationId(Integer.parseInt(line[2]));
                    trainer.setTrainerCountry(line[3]);
                    trainer.setTrainerStable(line[4]);
                    trainer.setTrainerAge(Integer.parseInt(line[5]));
                    trainer.setTrainerGender(line[6]);

                    repository.save(trainer);
                } catch (NumberFormatException e) {
                    throw new RuntimeException("Invalid number format in line number :  "+ rowNumber  + String.join(",", line), e);
                }
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException("Failed to process CSV file", e);
        }
    }


    public Optional<Trainer> findById(Long id) {
        return repository.findById(id);
    }

    public Trainer updateTrainerMaster(TrainerDto trainerDto) {
        Optional<Trainer> existingTrainer = repository.findById(trainerDto.getId());

        if (existingTrainer.isEmpty()) {
            throw new ApiException("NO DATA FOUND WITH THIS ID" + trainerDto.getId(), HttpStatus.BAD_REQUEST);
        }

        Trainer trainer = existingTrainer.get();
        trainer.setTrainerName(trainerDto.getTrainerName());
        trainer.setTrainerCount(trainerDto.getTrainerCount());
        trainer.setTrainerRegistrationId(trainerDto.getTrainerRegistrationId());
        trainer.setTrainerCountry(trainerDto.getTrainerCountry());
        trainer.setTrainerStable(trainerDto.getTrainerStable());
        trainer.setTrainerAge(trainerDto.getTrainerAge());
        trainer.setTrainerGender(trainerDto.getTrainerGender());

        return repository.save(trainer);
    }

    public Page<Trainer> findAll(Pageable pageRequest) {
        return repository.findAll(pageRequest);
    }

    public TrainerResponse findAllTrainer(int page, int size) {
        Page<Trainer> trainers = repository.findAll(PageRequest.of(page, size));

        List<TrainerDto> trainerDtos = trainers.getContent().stream().map(trainerName ->
                TrainerDto.builder()
                        .id(trainerName.getId())
                        .trainerName(trainerName.getTrainerName())
                        .trainerCount(trainerName.getTrainerCount())
                        .trainerRegistrationId(trainerName.getTrainerRegistrationId())
                        .trainerCountry(trainerName.getTrainerCountry())
                        .trainerStable(trainerName.getTrainerStable())
                        .trainerAge(trainerName.getTrainerAge())
                        .trainerGender(trainerName.getTrainerGender())
                        .build()
        ).collect(Collectors.toList());
        return TrainerResponse.builder().trainerDtos(trainerDtos).totalCount(trainers.getTotalElements()).build();
    }
    public void delete(Long id) {
        Optional<Trainer> existingTrainer = repository.findById(id);
        if (existingTrainer.isEmpty()) {
            throw new ApiException("No data found with ID " + id, HttpStatus.BAD_REQUEST);
        }
        repository.deleteById(id);
    }
}
