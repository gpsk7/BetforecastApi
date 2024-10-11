package net.javaguides.springboot.controller;

import net.javaguides.springboot.dto.HorseNameCountResponse;
import net.javaguides.springboot.dto.RaceInfo;
import net.javaguides.springboot.model.Race;
import net.javaguides.springboot.service.RaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public")
public class PublicRaceController {

    @Autowired
    private RaceService raceService;

    @GetMapping("/countries")
    public List<String> getCountriesByDate(@RequestParam String date) {
        return raceService.getCountriesByDate(date);
    }

    @GetMapping("/courses")
    public List<String> getCoursesByCountryAndDate(@RequestParam String country, @RequestParam String date) {
        return raceService.getCoursesByCountryAndDate(country, date);
    }


    @GetMapping("/racenumbers")
    public List<RaceInfo> getDistinctRaceNumberAndTimeByCountryAndDateAndCourse(@RequestParam String country, @RequestParam String date, @RequestParam String course) {
        return raceService.getDistinctRaceNumberAndTimeByCountryAndDateAndCourse(country, date, course);
    }

    // Method to map Race entity to RaceInfo DTO
    private RaceInfo mapToRaceInfo(Race race) {
        RaceInfo raceInfo = new RaceInfo();
        raceInfo.setRaceNumber(race.getRaceNumber());
        raceInfo.setTime(race.getTime());
        return raceInfo;
    }


    @GetMapping("/details")
    public List<Race> getDetailsByCountryAndDateAndCourseAndRaceNumber(@RequestParam String country, @RequestParam String date, @RequestParam String course, @RequestParam String raceNumber) {
        return raceService.getDetailsByCountryAndDateAndCourseAndRaceNumber(country, date, course, raceNumber );
    }


    @GetMapping("/horsecounters")
    public List<HorseNameCountResponse> getHorseNameCountsByRace(@RequestParam String country, @RequestParam String date, @RequestParam String course, @RequestParam String raceNumber) {
        return raceService.getHorseNameCountsByRace(country, date, course, raceNumber);
    }

}