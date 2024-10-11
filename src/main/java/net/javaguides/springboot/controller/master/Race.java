package net.javaguides.springboot.controller.master;

import net.javaguides.springboot.WebRequest.master.RaceResponse;
import net.javaguides.springboot.dto.RaceDto;
import net.javaguides.springboot.service.RaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/race")
public class Race {

    @Autowired
    public RaceService raceService;

    @PostMapping("/createRace")
    public ResponseEntity<net.javaguides.springboot.model.Race> createRace(@RequestBody RaceDto raceDto) throws Exception {
        net.javaguides.springboot.model.Race race = raceService.createRace(raceDto);
        return new ResponseEntity<>(race, HttpStatus.OK);
    }

    @PutMapping("/updateRace")
    public ResponseEntity<String> updateRace(@RequestBody RaceDto raceDto) throws Exception {
        raceService.updateRace(raceDto);
        return ResponseEntity.ok("Updated Successfully");
    }

    @GetMapping("/getRace")
    public ResponseEntity<RaceResponse> findAll(@RequestParam(value = "size", required = false) Integer size,
                                                @RequestParam(value = "page", required = false) Integer page) {
        RaceResponse response = raceService.findAllRace(page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getRaceInReverse")
    public ResponseEntity<RaceResponse> findAllInReverse(@RequestParam(value = "size", required = false) Integer size,
                                                         @RequestParam(value = "page", required = false) Integer page) {
        RaceResponse response = raceService.findAllRaceInReverseOrder(page, size);
        return ResponseEntity.ok(response);
    }
    /**
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadCSVFile(@RequestParam("file") MultipartFile file) {
        Map<String, String> response = new HashMap<>();
        try {
            raceService.uploadCSVFile(file);
            response.put("message", "File uploaded and data saved successfully");
            return ResponseEntity.ok(response);
        } catch (IOException exception) {
            response.put("error", "Failed to process the file: " + exception.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (RuntimeException exception) {
            response.put("error", exception.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

@DeleteMapping("/delete/{id}")
public ResponseEntity<String> delete(@PathVariable Long id) {
    raceService.delete(id);
    return ResponseEntity.ok("Deleted Successfully");
}
}
