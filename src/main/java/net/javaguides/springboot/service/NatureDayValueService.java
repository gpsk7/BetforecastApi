package net.javaguides.springboot.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import net.javaguides.springboot.WebRequest.AstrologyResponse;
import net.javaguides.springboot.WebRequest.NatureOfTheDayResponse;
import net.javaguides.springboot.dto.AstrologyDto;
import net.javaguides.springboot.dto.NatureOfTheDayDto;
import net.javaguides.springboot.exception.ApiException;
import net.javaguides.springboot.model.Astrology;
import net.javaguides.springboot.model.NatureOfDayAndValue;
import net.javaguides.springboot.model.master.Jockey;
import net.javaguides.springboot.repository.NatureDayValueRepository;
import net.javaguides.springboot.response.NatureDayValue;
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
public class NatureDayValueService {

    @Autowired
    private NatureDayValueRepository natureDayRepository;


//  CREATE
    public NatureOfDayAndValue saveNatureOfDayAndValue(NatureOfDayAndValue natureOfDayAndValue) {
        Optional<NatureOfDayAndValue> existingDate = natureDayRepository.findByDate(natureOfDayAndValue.getDate());
        if (existingDate.isPresent()) {
            throw new ApiException("DATA IS ALREADY EXIST WITH THIS DATE " + existingDate.get().getDate(), HttpStatus.BAD_REQUEST);
        }
        return natureDayRepository.save(natureOfDayAndValue);
    }

    public NatureOfTheDayResponse findAllNatures(int page, int size) {
        Page<NatureOfDayAndValue> natureOfDayAndValues = natureDayRepository.findAll(PageRequest.of(page, size));

        List<NatureOfTheDayDto> natureOfTheDayDtos = natureOfDayAndValues.getContent().stream().map(natureOfDayAndValue -> {
            NatureOfTheDayDto dto = NatureOfTheDayDto.builder()
                    .id(natureOfDayAndValue.getId())
                    .dayValue(natureOfDayAndValue.getDayValue())
                    .date(natureOfDayAndValue.getDate())
                    .nakshatra(natureOfDayAndValue.getNakshatra())
                    .natureOfDay(natureOfDayAndValue.getNatureOfDay())
                    .nakshatraNumber(natureOfDayAndValue.getNakshatraNumber())
                    .natureOfDayNo(natureOfDayAndValue.getNatureOfDayNo())
                    .nruler(natureOfDayAndValue.getNruler())
                    .rcolour(natureOfDayAndValue.getRcolour())
                    .rcolourNo(natureOfDayAndValue.getRcolourNo())
                    .nrulerNumber(natureOfDayAndValue.getNrulerNumber())
                    .build();
            System.out.println("Mapped DTO: " + dto); // Debugging line
            return dto;
        }).collect(Collectors.toList());

        return NatureOfTheDayResponse.builder()
                .natureOfTheDayDtos(natureOfTheDayDtos)
                .totalCount(natureOfDayAndValues.getTotalElements())
                .build();
    }


    public NatureDayValue getNatureDayValue(String date){
        Optional<NatureOfDayAndValue> byDate = natureDayRepository.findByDate(date);
        if(byDate.isEmpty()){
            throw new ApiException("NO NATURE OF DAY DATA FOUND WITH THIS DATE", HttpStatus.NOT_FOUND);
        }
        return new NatureDayValue(byDate.get().getDayValue(),byDate.get().getNatureOfDay());
    }

    public List<String> findByNakshatra(String dasha, String antarDasha){
        List<String> dashaNumber =     natureDayRepository.findByNakshatra(dasha);
        List<String> antarDashaNumber =  natureDayRepository.findByNakshatra(antarDasha);

        if(dashaNumber.isEmpty() || antarDashaNumber.isEmpty()){
            throw new ApiException("No Nakshatra number found with this dasha and antardasha",HttpStatus.BAD_REQUEST);
        }
        String dashaNum = dashaNumber.get(0);
        String antarDashaNum = antarDashaNumber.get(0);

        if(Integer.parseInt(dashaNum)> 9){
           dashaNum = String.valueOf(NumberLogicService.reduceToSingleDigit(Integer.parseInt(dashaNum)));
        }if(Integer.parseInt(antarDashaNum)>9){
            antarDashaNum = String.valueOf(NumberLogicService.reduceToSingleDigit(Integer.parseInt(antarDashaNum)));
        }
        return List.of(dashaNum,antarDashaNum);
    }


    public NatureOfDayAndValue update(NatureOfDayAndValue natureOfDayAndValue) {
        System.out.println(natureOfDayAndValue);
        Optional<NatureOfDayAndValue> existingEntity = natureDayRepository.findById(natureOfDayAndValue.getId());
        if(existingEntity.isPresent()){
            NatureOfDayAndValue updateEntity=existingEntity.get();
            updateEntity.setDate(natureOfDayAndValue.getDate());
            updateEntity.setDayValue(natureOfDayAndValue.getDayValue());
            updateEntity.setNakshatra(natureOfDayAndValue.getNakshatra());
            updateEntity.setNruler(natureOfDayAndValue.getNruler());
            updateEntity.setNatureOfDay(natureOfDayAndValue.getNatureOfDay());
            updateEntity.setRcolour(natureOfDayAndValue.getRcolour());
            updateEntity.setNakshatraNumber(natureOfDayAndValue.getNakshatraNumber());
            updateEntity.setNrulerNumber(natureOfDayAndValue.getNrulerNumber());
            updateEntity.setNatureOfDayNo(natureOfDayAndValue.getNatureOfDayNo());
            updateEntity.setRcolourNo(natureOfDayAndValue.getRcolourNo());

            return natureDayRepository.save(updateEntity); // Save and return the updated entity
        } else {
            throw new ApiException("Entity with ID " + natureOfDayAndValue.getId() + " not found", HttpStatus.BAD_REQUEST);
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
                NatureOfDayAndValue entity = new NatureOfDayAndValue();
                try {

                    entity.setDate(line[0]);
                    entity.setDayValue(Integer.parseInt(line[1]));
                    entity.setNakshatra(line[2]);
                    entity.setNruler(line[3]);
                    entity.setNatureOfDay(line[4]);
                    entity.setRcolour(line[5]);
                    entity.setNakshatraNumber(line[6]);
                    entity.setNrulerNumber(line[7]);
                    entity.setNatureOfDayNo(line[8]);
                    entity.setRcolourNo(line[9]);


                    natureDayRepository.save(entity);
                } catch (NumberFormatException e) {
                    throw new RuntimeException("Invalid number format in line number : " + rowNumber  + String.join(",", line), e);
                }
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException("Failed to process CSV file", e);
        }
    }
    public void delete(Long id) {
        Optional<NatureOfDayAndValue> existDayAndValue = natureDayRepository.findById(id);
        if (existDayAndValue.isEmpty()) {
            throw new ApiException("No data found with ID " + id, HttpStatus.BAD_REQUEST);
        }
        natureDayRepository.deleteById(id);
    }
}
