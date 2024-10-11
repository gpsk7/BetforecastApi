package net.javaguides.springboot.controller.master;

import com.opencsv.exceptions.CsvValidationException;
import net.javaguides.springboot.WebRequest.master.JockeyResponse;
import net.javaguides.springboot.dto.master.HorseDto;
import net.javaguides.springboot.dto.master.JockeyDto;
import net.javaguides.springboot.exception.ApiException;
import net.javaguides.springboot.model.master.Jockey;
import net.javaguides.springboot.service.master.JockeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
//@RequestMapping("/api/v1/admin")
@RequestMapping("/api/v1/admin/jockey")
public class JockeyController {

    @Autowired
    JockeyService jockeyService;

    @PostMapping("/upload")          //Upload file from CSVfile
    public ResponseEntity<Map<String, String>> uploadCSVFile(@RequestParam("file") MultipartFile file) {
        Map<String, String> response = new HashMap<>();
        try {
            jockeyService.uploadCSVFile(file);
            response.put("message", "File uploaded and data saved successfully");
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            response.put("error", "Failed to process the file: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (RuntimeException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping
    public ResponseEntity<Jockey> createJockeyMaster(@RequestBody JockeyDto jockeyDto) {
     //   Jockey jockey = jockeyService.findByJockeyFirstName(jockeyDto.getJockeyFirstName());
        try {
          Jockey  jockey = Jockey.builder()
                    .jockeyRegistrationId(jockeyDto.getJockeyRegistrationId())
                    .jockeyFirstName(jockeyDto.getJockeyFirstName())
                    .jockeyLastName(jockeyDto.getJockeyLastName())
                    .jockeyAge(jockeyDto.getJockeyAge())
                    .jockeyGender(jockeyDto.getJockeyGender())
                    .jockeyCount(jockeyDto.getJockeyCount())
                    .jockeyCountry(jockeyDto.getJockeyCountry())
                    .jockeyExperienceYears(jockeyDto.getJockeyExperienceYears())
                    .jockeyWins(jockeyDto.getJockeyWins())
                    .jockeyLosses(jockeyDto.getJockeyLosses())
                    .build();
            jockeyService.saveJockey(jockey);
            return new ResponseEntity<>(jockey, HttpStatus.OK);
        } catch (Exception exception) {
            throw new ApiException(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity<String> updateJockeyMaster(@RequestBody JockeyDto jockeyDto) {
        Optional<Jockey> existingJokey = jockeyService.findById(jockeyDto.getId());

        if (existingJokey.isEmpty()) {
            throw new ApiException("NO DATA FOUND WITH THIS ID" + jockeyDto.getId(), HttpStatus.BAD_REQUEST);
        }

        Jockey jockey = existingJokey.get();
        jockey.setJockeyRegistrationId(jockeyDto.getJockeyRegistrationId());
        jockey.setJockeyFirstName(jockeyDto.getJockeyFirstName());
        jockey.setJockeyLastName(jockeyDto.getJockeyLastName());
        jockey.setJockeyAge(jockeyDto.getJockeyAge());
        jockey.setJockeyGender(jockeyDto.getJockeyGender());
        jockey.setJockeyCount(jockeyDto.getJockeyCount());
        jockey.setJockeyCountry(jockeyDto.getJockeyCountry());
        jockey.setJockeyExperienceYears(jockeyDto.getJockeyExperienceYears());
        jockey.setJockeyWins(jockeyDto.getJockeyWins());
        jockey.setJockeyLosses(jockeyDto.getJockeyLosses());

        jockeyService.saveJockey(jockey);
        return ResponseEntity.ok("Updated Successfully");
    }

    @GetMapping
    public ResponseEntity<JockeyResponse> findAll(@RequestParam(value = "size", required = false) Integer size,
                                                  @RequestParam(value = "page", required = false) Integer page) {
        Page<Jockey> Jockeys = jockeyService.findAll(PageRequest.of(page, size));

        List<JockeyDto> jockeyDtos = new ArrayList<>();
        Jockeys.getContent().stream().forEach(jockeyName -> {
            JockeyDto jockeyDto = JockeyDto.builder()
                    .id(jockeyName.getId())
                    .jockeyRegistrationId(jockeyName.getJockeyRegistrationId())
                    .jockeyFirstName(jockeyName.getJockeyFirstName())
                    .jockeyLastName(jockeyName.getJockeyLastName())
                    .jockeyAge(jockeyName.getJockeyAge())
                    .jockeyGender(jockeyName.getJockeyGender())
                    .jockeyCount(jockeyName.getJockeyCount())
                    .jockeyCountry(jockeyName.getJockeyCountry())
                    .jockeyExperienceYears(jockeyName.getJockeyExperienceYears())
                    .jockeyWins(jockeyName.getJockeyWins())
                    .jockeyLosses(jockeyName.getJockeyLosses())
                    .build();
            jockeyDtos.add(jockeyDto);
        });
        return ResponseEntity.ok(JockeyResponse.builder()
                .jockeyDtos(jockeyDtos).totalCount(Jockeys.getTotalElements()).build());
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        jockeyService.delete(id);
        return ResponseEntity.ok("Deleted Successfully");
    }
}
