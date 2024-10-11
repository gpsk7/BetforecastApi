package net.javaguides.springboot.service.master;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import net.javaguides.springboot.dto.master.HorseDto;
import net.javaguides.springboot.dto.master.JockeyDto;
import net.javaguides.springboot.exception.ApiException;
import net.javaguides.springboot.model.master.Horse;
import net.javaguides.springboot.model.master.Jockey;
import net.javaguides.springboot.repository.master.JockeyRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.params.shadow.com.univocity.parsers.csv.CsvFormat;
import org.junit.jupiter.params.shadow.com.univocity.parsers.csv.CsvParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JockeyService {
    @Autowired
    JockeyRepository jockeyRepository;

    @Transactional
    public void uploadCSVFile(MultipartFile file) throws IOException {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
             CSVReader csvReader = new CSVReader(reader)) {

            String[] line;
            csvReader.readNext(); // Skip header row
            int rowNumber=1;

            while ((line = csvReader.readNext()) != null) {
                rowNumber++;
                Jockey jockey = new Jockey();
                try {
                    jockey.setJockeyRegistrationId(Integer.parseInt(line[0]));
                    jockey.setJockeyFirstName(line[1]);
                    jockey.setJockeyLastName(line[2]);
                    jockey.setJockeyAge(Integer.parseInt(line[3]));
                    jockey.setJockeyGender(line[4]);
                    jockey.setJockeyCount(Integer.parseInt(line[5]));
                    jockey.setJockeyCountry(line[6]);
                    jockey.setJockeyExperienceYears(Integer.parseInt(line[7]));
                    jockey.setJockeyWins(Integer.parseInt(line[8]));
                    jockey.setJockeyLosses(Integer.parseInt(line[9]));

                    jockeyRepository.save(jockey);
                } catch (NumberFormatException e) {
                    throw new RuntimeException("Invalid number format in line number : "+ rowNumber + String.join(",", line), e);
                }
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException("Failed to process CSV file", e);
        }
    }


    public Jockey saveJockey(Jockey jockey) {
        return jockeyRepository.save(jockey);
    }

    public Jockey findByJockeyFirstName(String jockeyFirstName) {
        return jockeyRepository.findByJockeyFirstName(jockeyFirstName);
    }

    public Optional<Jockey> findById(Long id) {
        return jockeyRepository.findById(id);
    }

    public Page<Jockey> findAll(Pageable pageRequest) {
        return jockeyRepository.findAll(pageRequest);
    }

    public List<Jockey> findAll() {
        return jockeyRepository.findAll();
    }


    public void delete(Long id) {
        Optional<Jockey> existingJockey = jockeyRepository.findById(id);
        if (existingJockey.isEmpty()) {
            throw new ApiException("No data found with ID " + id, HttpStatus.BAD_REQUEST);
        }
        jockeyRepository.deleteById(id);
    }

}
