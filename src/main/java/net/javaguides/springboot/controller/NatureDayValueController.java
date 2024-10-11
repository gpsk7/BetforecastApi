package net.javaguides.springboot.controller;

import net.javaguides.springboot.WebRequest.NatureOfTheDayResponse;
import net.javaguides.springboot.WebRequest.master.HorseResponse;
import net.javaguides.springboot.dto.NatureOfTheDayDto;
import net.javaguides.springboot.model.NatureOfDayAndValue;
import net.javaguides.springboot.response.NatureDayValue;
import net.javaguides.springboot.service.NatureDayValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/nature")
public class NatureDayValueController {
    @Autowired
    private NatureDayValueService natureDayService;

    @GetMapping
    public ResponseEntity<NatureDayValue> getNatureDayValue(@RequestParam String date){
       return  ResponseEntity.ok(natureDayService.getNatureDayValue(date));
    }

//  CREATE_____
    @PostMapping("/create")
    public ResponseEntity<NatureOfDayAndValue> createNatureOfDayAndValue(@RequestBody NatureOfDayAndValue natureOfDayAndValue){
        NatureOfDayAndValue savedNatureOfDayAndValue = natureDayService.saveNatureOfDayAndValue(natureOfDayAndValue);
        return new ResponseEntity<>(savedNatureOfDayAndValue, HttpStatus.CREATED);
    }
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody NatureOfDayAndValue natureOfDayAndValue) {
        if (natureOfDayAndValue.getId() == null) {
            return ResponseEntity.badRequest().body("ID is missing"); // If ID is not provided, return bad request
        }

        NatureOfDayAndValue updatedEntity = natureDayService.update(natureOfDayAndValue);
        return ResponseEntity.ok(updatedEntity); // Return the updated entity
    }

    @PostMapping("/upload")          //Upload file from CSVfile
    public ResponseEntity<Map<String, String>> uploadCSVFile(@RequestParam("file") MultipartFile file) {
        Map<String, String> response = new HashMap<>();
        try {
            natureDayService.uploadCSVFile(file);
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
    @GetMapping("/all")
    public ResponseEntity<NatureOfTheDayResponse> findAll(@RequestParam(value = "size", required = false) Integer size,
                                                          @RequestParam(value = "page", required = false) Integer page) {
        NatureOfTheDayResponse response = natureDayService.findAllNatures(page, size);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        natureDayService.delete(id);
        return ResponseEntity.ok("Deleted Successfully");
    }
}
