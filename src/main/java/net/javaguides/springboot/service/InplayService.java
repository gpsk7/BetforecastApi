package net.javaguides.springboot.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import net.javaguides.springboot.WebRequest.AstrologyResponse;
import net.javaguides.springboot.WebRequest.InplayResponse;
import net.javaguides.springboot.dto.InplayDto;
import net.javaguides.springboot.exception.ApiException;
import net.javaguides.springboot.model.Inplay;
import net.javaguides.springboot.repository.AstrologyRepository;
import net.javaguides.springboot.repository.InplayRepository;
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
public class InplayService {
    @Autowired
    private InplayRepository inplayRepository;

    public Inplay save(Inplay inplay) {
        return inplayRepository.save(inplay);
    }
    public Inplay findByDateAndTime(String date, double time) {
        return inplayRepository.findByDateAndTime(date, time);
    }


    public Inplay createInplay(InplayDto inplayDto) {
        Inplay existingInplay = inplayRepository.findByDateAndTime(inplayDto.getDate(), inplayDto.getTime());
        if (existingInplay != null) {
            throw new ApiException("DATA ALREADY EXIST WITH DATE " + inplayDto.getDate() + " AND TIME " + inplayDto.getTime(), HttpStatus.BAD_REQUEST);
        }
        Inplay inplay = Inplay.builder()
                .time(inplayDto.getTime())
                .date(inplayDto.getDate())
                .course(inplayDto.getCourse())
                .country(inplayDto.getCountry())
                .position1(inplayDto.getPosition1())
                .position2(inplayDto.getPosition2())
                .position3(inplayDto.getPosition3())
                .odds(inplayDto.getOdds())
                .build();
        return inplayRepository.save(inplay);
    }

    public Optional<Inplay> findById(Long id) {
        return inplayRepository.findById(id);
    }

    public Inplay updateInplay(InplayDto inplayDto) {
        Optional<Inplay> existInplay = inplayRepository.findById(inplayDto.getId());

        if (existInplay.isEmpty()) {
            throw new ApiException("NO DATA FOUND WITH THIS ID " + inplayDto.getId(), HttpStatus.BAD_REQUEST);
        }

        Inplay inplay = existInplay.get();
        inplay.setTime(inplayDto.getTime());
        inplay.setDate(inplayDto.getDate());
        inplay.setCourse(inplayDto.getCourse());
        inplay.setCountry(inplayDto.getCountry());
        inplay.setPosition1(inplayDto.getPosition1());
        inplay.setPosition2(inplayDto.getPosition2());
        inplay.setPosition3(inplayDto.getPosition3());
        inplay.setOdds(inplayDto.getOdds());
        return inplayRepository.save(inplay);
    }

    public Page<Inplay> findAll(Pageable pageRequest) {
        return inplayRepository.findAll(pageRequest);
    }
    public InplayResponse findAllInplays(int page, int size) {
        Page<Inplay> inplays = inplayRepository.findAll(PageRequest.of(page, size));
        List<InplayDto> inplayDtos = inplays.getContent().stream().map(inplay -> {
            InplayDto dto = InplayDto.builder()
                    .id(inplay.getId())
                    .time(inplay.getTime())
                    .date(inplay.getDate())
                    .course(inplay.getCourse())
                    .country(inplay.getCountry())
                    .position1(inplay.getPosition1())
                    .position2(inplay.getPosition2())
                    .position3(inplay.getPosition3())
                    .odds(inplay.getOdds())
                    .build();
            return dto;
        }).collect(Collectors.toList());
        return InplayResponse.builder().inplayDtos(inplayDtos).totalCount(inplays.getTotalElements()).build();
    }

    public void delete(Long id) {
        Optional<Inplay> existingInplay = inplayRepository.findById(id);
        if (existingInplay.isEmpty()) {
            throw new ApiException("No data found with ID " + id, HttpStatus.BAD_REQUEST);
        }
        inplayRepository.deleteById(id);
    }

    public List<InplayDto> search(String name) {
        List<InplayDto> inplayDtos = inplayRepository.search(name).stream().map(inplay -> {
           InplayDto dto = InplayDto.builder()
                   .id(inplay.getId())
                   .time(inplay.getTime())
                   .date(inplay.getDate())
                   .course(inplay.getCourse())
                   .country(inplay.getCountry())
                   .position1(inplay.getPosition1())
                   .position2(inplay.getPosition2())
                   .position3(inplay.getPosition3())
                   .odds(inplay.getOdds())
                    .build();
           return dto;
       }).collect(Collectors.toList());
        return inplayDtos;
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
                InplayDto inplay = new InplayDto();
                try {
                    inplay.setDate(line[0]);
                    inplay.setTime(Double.parseDouble(line[1]));
                    inplay.setCountry(line[2]);
                    inplay.setCourse(line[3]);
                    inplay.setPosition1(Integer.parseInt(line[4]));
                    inplay.setPosition2(Integer.parseInt(line[5]));
                    inplay.setPosition3(Integer.parseInt(line[6]));
                    inplay.setOdds(line[7]);

                    createInplay(inplay);
                } catch (NumberFormatException e) {
                    throw new RuntimeException("Invalid number format in line number :  "+ rowNumber  + String.join(",", line), e);
                }
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException("Failed to process CSV file", e);
        }
    }
}
