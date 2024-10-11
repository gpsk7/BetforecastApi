package net.javaguides.springboot.controller.master;

import net.javaguides.springboot.WebRequest.master.TrainerResponse;
import net.javaguides.springboot.dto.master.JockeyDto;
import net.javaguides.springboot.dto.master.TrainerDto;
import net.javaguides.springboot.model.master.Trainer;
import net.javaguides.springboot.service.master.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/trainer")
public class TrainerController {

    @Autowired
    TrainerService trainerService;

    @PostMapping("/upload")          //Upload file from CSVfile
    public ResponseEntity<Map<String, String>> uploadCSVFile(@RequestParam("file") MultipartFile file) {
        Map<String, String> response = new HashMap<>();
        try {
            trainerService.uploadCSVFile(file);
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
    public ResponseEntity<Trainer> createTrainer(@RequestBody TrainerDto trainerDto) {
        Trainer trainer = trainerService.createTrainerMaster(trainerDto);
        return new ResponseEntity<>(trainer, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<String> updateTrainer(@RequestBody TrainerDto trainerDto) {
        trainerService.updateTrainerMaster(trainerDto);
        return ResponseEntity.ok("Updated Successfully");
    }

    @GetMapping
    public ResponseEntity<TrainerResponse> findAll(@RequestParam(value = "size", required = false) Integer size,
                                                   @RequestParam(value = "page", required = false) Integer page) {

        TrainerResponse response = trainerService.findAllTrainer(page, size);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        trainerService.delete(id);
        return ResponseEntity.ok("Deleted Successfully");
    }
}
