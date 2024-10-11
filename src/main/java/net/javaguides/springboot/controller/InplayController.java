package net.javaguides.springboot.controller;

import net.javaguides.springboot.WebRequest.AstrologyResponse;
import net.javaguides.springboot.WebRequest.InplayResponse;
import net.javaguides.springboot.WebRequest.master.HorseResponse;
import net.javaguides.springboot.dto.AstrologyDto;
import net.javaguides.springboot.dto.InplayDto;
import net.javaguides.springboot.dto.master.HorseDto;
import net.javaguides.springboot.model.Astrology;
import net.javaguides.springboot.model.Inplay;
import net.javaguides.springboot.service.AstrologyService;
import net.javaguides.springboot.service.InplayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/inplay")
public class InplayController {

    @Autowired
    private InplayService inplayService;

    @PostMapping
    public ResponseEntity<Inplay> create(@RequestBody InplayDto inplayDto) {
        return new ResponseEntity<>(inplayService.createInplay(inplayDto), HttpStatus.OK);
    }
    @PutMapping
    public ResponseEntity<String> update(@RequestBody InplayDto inplayDto) {
        inplayService.updateInplay(inplayDto);
        return ResponseEntity.ok("Updated Successfully");
    }

    @GetMapping
    public ResponseEntity<InplayResponse> findAll(@RequestParam(value = "size", required = false) Integer size,
                                                 @RequestParam(value = "page", required = false) Integer page) {
        InplayResponse response = inplayService.findAllInplays(page, size);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        inplayService.delete(id);
        return ResponseEntity.ok("Deleted Successfully");
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<List<InplayDto>> search(@PathVariable String name) {
        return ResponseEntity.ok(inplayService.search(name));
    }
    @PostMapping("/upload")          //Upload file from CSVfile
    public ResponseEntity<Map<String, String>> uploadCSVFile(@RequestParam("file") MultipartFile file) {
        Map<String, String> response = new HashMap<>();
        try {
            inplayService.uploadCSVFile(file);
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
}
