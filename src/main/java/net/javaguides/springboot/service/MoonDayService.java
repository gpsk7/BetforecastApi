package net.javaguides.springboot.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import net.javaguides.springboot.WebRequest.MoonDayResponse;
import net.javaguides.springboot.dto.MoonDayDto;
import net.javaguides.springboot.exception.ApiException;
import net.javaguides.springboot.model.MoonDay;
import net.javaguides.springboot.repository.MoonDayRepository;
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
public class MoonDayService {

    @Autowired
    public MoonDayRepository repository;

    public MoonDay saveMoonDay(MoonDay moonDay) {
        return repository.save(moonDay);
    }

    public MoonDay createMoonDay(MoonDayDto moonDayDto) {
        Optional<MoonDay> existingMoonDay = repository.findByDate(moonDayDto.getDate());
        if (existingMoonDay.isPresent()) {
            throw new ApiException("Moon data already present for this date: " + moonDayDto.getDate(), HttpStatus.BAD_REQUEST);
        }
        MoonDay moonDay = MoonDay.builder()
                .date(moonDayDto.getDate())
                .moonDay(moonDayDto.getMoonDay())
                .build();
        return repository.save(moonDay);
    }

    public Optional<MoonDay> findById(Long id) {
        return repository.findById(id);
    }

    public MoonDay updateMoonDay(MoonDayDto moonDayDto) {
        Optional<MoonDay> existingMoonDay = repository.findById(moonDayDto.getId());

        if (existingMoonDay.isEmpty()) {
            throw new ApiException("NO DATA FOUND WITH THIS ID " + moonDayDto.getId(), HttpStatus.BAD_REQUEST);
        }

        MoonDay moonDay = existingMoonDay.get();
        moonDay.setDate(moonDayDto.getDate());
        moonDay.setMoonDay(moonDayDto.getMoonDay());

        return repository.save(moonDay);
    }

    public Page<MoonDay> findAll(Pageable pageRequest) {
        return repository.findAll(pageRequest);
    }


    public MoonDayResponse findAllMoonDay(int page, int size) {
        Page<MoonDay> moonDays = repository.findAll(PageRequest.of(page, size));

        List<MoonDayDto> moonDayDtos = moonDays.getContent().stream().map(moonDay ->
                MoonDayDto.builder()
                        .id(moonDay.getId())
                        .date(moonDay.getDate())
                        .moonDay(moonDay.getMoonDay())
                        .build()
        ).collect(Collectors.toList());
        return MoonDayResponse.builder().moonDayDtos(moonDayDtos).totalCount(moonDays.getTotalElements()).build();
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
                MoonDay moonDay = new MoonDay();
                try {
                    moonDay.setDate(line[0]);
                    moonDay.setMoonDay(line[1]);

                    repository.save(moonDay);
                } catch (NumberFormatException e) {
                    throw new RuntimeException("Invalid number format in line number : " + rowNumber  + String.join(",", line), e);
                }
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException("Failed to process CSV file", e);
        }
    }

    public void delete(Long id) {
        Optional<MoonDay> existMoonDay = repository.findById(id);
        if (existMoonDay.isEmpty()) {
            throw new ApiException("No data found with ID " + id, HttpStatus.BAD_REQUEST);
        }
        repository.deleteById(id);
    }
}
