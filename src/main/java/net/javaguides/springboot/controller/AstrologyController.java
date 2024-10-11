package net.javaguides.springboot.controller;

import net.javaguides.springboot.WebRequest.AstrologyResponse;
import net.javaguides.springboot.WebRequest.master.HorseResponse;
import net.javaguides.springboot.dto.AstrologyDto;
import net.javaguides.springboot.dto.master.HorseDto;
import net.javaguides.springboot.exception.ApiException;
import net.javaguides.springboot.model.Astrology;
import net.javaguides.springboot.model.master.Horse;
import net.javaguides.springboot.service.AstrologyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/admin/astrology")
public class AstrologyController {
    @Autowired
    private AstrologyService astrologyService;

    @PostMapping
    public ResponseEntity<Astrology> create(@RequestBody AstrologyDto astrologyDto) {
        return new ResponseEntity<>(astrologyService.createAstrology(astrologyDto), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<String> updateAstrology(@RequestBody AstrologyDto astrologyDto) {
        astrologyService.updateAstrology(astrologyDto);
        return ResponseEntity.ok("Updated Successfully");
    }

    @GetMapping
    public ResponseEntity<AstrologyResponse> findAll(@RequestParam(value = "size", required = false) Integer size,
                                                     @RequestParam(value = "page", required = false) Integer page) {
        if (size == null || page == null) {
            return ResponseEntity.badRequest().body(null);
        }

        AstrologyResponse response = astrologyService.findAllAstrologies(page, size);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/upload")          //Upload file from CSVfile
    public ResponseEntity<Map<String, String>> uploadCSVFile(@RequestParam("file") MultipartFile file) {
        Map<String, String> response = new HashMap<>();
        try {
            astrologyService.uploadCSVFile(file);
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
        astrologyService.delete(id);
        return ResponseEntity.ok("Deleted Successfully");
    }
}
