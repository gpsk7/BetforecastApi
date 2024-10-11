package net.javaguides.springboot.controller;

import net.javaguides.springboot.WebRequest.MoonDayResponse;
import net.javaguides.springboot.dto.MoonDayDto;
import net.javaguides.springboot.model.MoonDay;
import net.javaguides.springboot.service.MoonDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/MoonDay")
public class MoonDayController {

    @Autowired
    MoonDayService moonDayService;

    @PostMapping("/create")
    public ResponseEntity<MoonDay> create(@RequestBody MoonDayDto moonDayDto) {
        return new ResponseEntity<>(moonDayService.createMoonDay(moonDayDto), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestBody MoonDayDto moonDayDto) {
        moonDayService.updateMoonDay(moonDayDto);
        return ResponseEntity.ok("Updated Successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<MoonDayResponse> findAll(@RequestParam(value = "size", required = false) Integer size,
                                                   @RequestParam(value = "page", required = false) Integer page) {
        MoonDayResponse response = moonDayService.findAllMoonDay(page, size);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/upload")          //Upload file from CSVfile
    public ResponseEntity<Map<String, String>> uploadCSVFile(@RequestParam("file") MultipartFile file) {
        Map<String, String> response = new HashMap<>();
        try {
            moonDayService.uploadCSVFile(file);
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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        moonDayService.delete(id);
        return ResponseEntity.ok("Deleted Successfully");
    }
}
