package net.javaguides.springboot.controller.master;

import net.javaguides.springboot.WebRequest.master.HorseResponse;
import net.javaguides.springboot.dto.RaceDto;
import net.javaguides.springboot.dto.master.HorseDto;
import net.javaguides.springboot.model.master.Horse;
import net.javaguides.springboot.service.master.HorseService;
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
@RequestMapping("/api/v1/admin/horse")
public class HorseController {


    @Autowired
    HorseService horseService;

    @PostMapping
    public ResponseEntity<Horse> createHorse(@RequestBody HorseDto horseDto) {
        Horse horse = horseService.createHorseMaster(horseDto);
        return new ResponseEntity<>(horse, HttpStatus.OK);
    }

    @PostMapping("/upload")          //Upload file from CSVfile
    public ResponseEntity<Map<String, String>> uploadCSVFile(@RequestParam("file") MultipartFile file) {
        Map<String, String> response = new HashMap<>();
        try {
            horseService.uploadCSVFile(file);
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

    //    @RequestMapping(value = "/update/horseMaster", method = RequestMethod.PUT)
    @PutMapping
    public ResponseEntity<String> updateHorse(@RequestBody HorseDto horseDto) {
        horseService.updateHorseMaster(horseDto);
        return ResponseEntity.ok("Updated Successfully");
    }

    //    @RequestMapping(value = "/horseMaster", method = RequestMethod.GET)
    @GetMapping
    public ResponseEntity<HorseResponse> findAll(@RequestParam(value = "size", required = false) Integer size,
                                                 @RequestParam(value = "page", required = false) Integer page) {
        HorseResponse response = horseService.findAllHorse(page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<List<HorseDto>> search(@PathVariable String name) {
        return ResponseEntity.ok(horseService.search(name));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        horseService.delete(id);
        return ResponseEntity.ok("Deleted Successfully");}
}
