package net.javaguides.springboot.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import net.javaguides.springboot.WebRequest.AstrologyResponse;
import net.javaguides.springboot.WebRequest.master.HorseResponse;
import net.javaguides.springboot.dto.AstrologyDto;
import net.javaguides.springboot.dto.master.HorseDto;
import net.javaguides.springboot.exception.ApiException;
import net.javaguides.springboot.model.Astrology;
import net.javaguides.springboot.model.MoonDay;
import net.javaguides.springboot.model.master.Horse;
import net.javaguides.springboot.repository.AstrologyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class AstrologyService {
    @Autowired
    private AstrologyRepository astrologyRepository;

    public Astrology save(Astrology astrology) {
        return astrologyRepository.save(astrology);
    }

    public Astrology findByDateAndTime(String date, String time) {
        return astrologyRepository.findByDateAndTime(date, time);
    }

    public Astrology createAstrology(AstrologyDto astrologyDto) {
        Astrology existingAstrology = astrologyRepository.findByDateAndTime(astrologyDto.getDate(), astrologyDto.getTime());
        if (existingAstrology != null) {
            throw new ApiException("DATA ALREADY EXIST WITH DATE " + astrologyDto.getDate() + " AND TIME " + astrologyDto.getTime(), HttpStatus.BAD_REQUEST);
        }
        Astrology astrology = Astrology.builder()
                .time(astrologyDto.getTime())
                .date(astrologyDto.getDate())
                .dasha(astrologyDto.getDasha())
                .antarDasha(astrologyDto.getAntarDasha())
                .ownHouse(astrologyDto.getOwnHouse())
                .build();
        return astrologyRepository.save(astrology);
    }

    public Optional<Astrology> findById(Long id) {
        return astrologyRepository.findById(id);
    }

    public Astrology updateAstrology(AstrologyDto astrologyDto) {
        Optional<Astrology> existAstrology = astrologyRepository.findById(astrologyDto.getId());

        if (existAstrology.isEmpty()) {
            throw new ApiException("NO DATA FOUND WITH THIS ID " + astrologyDto.getId(), HttpStatus.BAD_REQUEST);
        }

        Astrology astrology = existAstrology.get();
        astrology.setTime(astrologyDto.getTime());
        astrology.setDate(astrologyDto.getDate());
        astrology.setDasha(astrologyDto.getDasha());
        astrology.setAntarDasha(astrologyDto.getAntarDasha());
        astrology.setOwnHouse(astrologyDto.getOwnHouse());
        return astrologyRepository.save(astrology);
    }

    public Page<Astrology> findAll(Pageable pageRequest) {
        return astrologyRepository.findAll(pageRequest);
    }

    //    public AstrologyResponse findAllAstrologies(int page, int size) {
//        Page<Astrology> astrologies = astrologyRepository.findAll(PageRequest.of(page, size));
//        System.out.println(astrologies.getContent());
//        List<AstrologyDto> astrologyDtos = astrologies.getContent().stream().map(astrology ->
//                AstrologyDto.builder()
//                        .id(astrology.getId())
//                        .time(astrology.getTime())
//                        .date(astrology.getDate())
//                        .dasha(astrology.getDasha())
//                        .antarDasha(astrology.getAntarDasha())
//
//                        .build()
//        ).collect(Collectors.toList());
//        System.out.println(astrologyDtos);
//        return AstrologyResponse.builder().astrologyDtos(astrologyDtos).totalCount(astrologies.getTotalElements()).build();
//    }
//}
    public AstrologyResponse findAllAstrologies(int page, int size) {
        Page<Astrology> astrologies = astrologyRepository.findAll(PageRequest.of(page, size));


        List<AstrologyDto> astrologyDtos = astrologies.getContent().stream().map(astrology -> {
            AstrologyDto dto = AstrologyDto.builder()
                    .id(astrology.getId())
                    .time(astrology.getTime())
                    .date(astrology.getDate())
                    .dasha(astrology.getDasha())
                    .antarDasha(astrology.getAntarDasha())
                    .ownHouse(astrology.getOwnHouse())
                    .build();
            return dto;
        }).collect(Collectors.toList());

        return AstrologyResponse.builder()
                .astrologyDtos(astrologyDtos)
                .totalCount(astrologies.getTotalElements())
                .build();
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
                Astrology astrology = new Astrology();
                try {
                    astrology.setTime(line[0]);
                    astrology.setDate(line[1]);
                    astrology.setDasha(line[2]);
                    astrology.setAntarDasha(line[3]);
                    astrology.setOwnHouse(line[4]);

                    astrologyRepository.save(astrology);
                } catch (NumberFormatException e) {
                    throw new RuntimeException("Invalid number format in line number : " + rowNumber + String.join(",", line), e);
                }
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException("Failed to process CSV file", e);
        }
    }

    public void delete(Long id) {
        Optional<Astrology> existingAstrology = astrologyRepository.findById(id);
        if (existingAstrology.isEmpty()) {
            throw new ApiException("No data found with ID " + id, HttpStatus.BAD_REQUEST);
        }
        astrologyRepository.deleteById(id);
    }
}



