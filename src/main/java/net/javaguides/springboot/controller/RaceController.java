package net.javaguides.springboot.controller;

import net.javaguides.springboot.dto.HorseNameCountResponse;
import net.javaguides.springboot.dto.RaceDto;
import net.javaguides.springboot.dto.RaceInfo;
import net.javaguides.springboot.exception.ApiException;
import net.javaguides.springboot.model.Race;

import net.javaguides.springboot.service.RaceService;
import net.javaguides.springboot.service.UserSubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
//@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/races")
public class RaceController {

    @Autowired
    private RaceService raceService;


    @Autowired
    private UserSubscriptionService userSubscriptionService;

    @GetMapping("/countries")
    public List<String> getCountriesByDate(@RequestParam String date, @RequestParam String userId) {
        if (userSubscriptionService.isUserPaymentDone(userId)) {
            return raceService.getCountriesByDate(date);
        }
        throw new ApiException("User is not authorized or payment not done.", HttpStatus.BAD_REQUEST);
    }
    @GetMapping("/countries-by-year-month")
    public List<String> getCountriesByYearAndMonth(@RequestParam("month") String month, @RequestParam("year") String year) {
        return raceService.getCountriesByYearAndMonth(month, year);
    }

    @GetMapping("/courses")
    public List<String> getCoursesByCountryAndDate(@RequestParam String country, @RequestParam String date,@RequestParam String userId) {
        if(userSubscriptionService.isUserPaymentDone(userId)) {
            return raceService.getCoursesByCountryAndDate(country, date);
        }
        return null;
    }



    @GetMapping("/racenumbers")
    public List<RaceInfo> getDistinctRaceNumberAndTimeByCountryAndDateAndCourse(@RequestParam String country, @RequestParam String date, @RequestParam String course,@RequestParam String userId) {
        if(userSubscriptionService.isUserPaymentDone(userId)) {
            return raceService.getDistinctRaceNumberAndTimeByCountryAndDateAndCourse(country, date, course);
        }
        return null;
    }

    // Method to map Race entity to RaceInfo DTO
    private RaceInfo mapToRaceInfo(Race race) {
        RaceInfo raceInfo = new RaceInfo();
        raceInfo.setRaceNumber(race.getRaceNumber());
        raceInfo.setTime(race.getTime());
        return raceInfo;
    }


    @GetMapping("/details")
    public List<RaceDto> getDetailsByCountryAndDateAndCourseAndRaceNumber(@RequestParam String country, @RequestParam String date, @RequestParam String course, @RequestParam String raceNumber, @RequestParam String userId) {
        if(userSubscriptionService.isUserPaymentDone(userId)) {
            return raceService.getDetailsByCountryAndDateAndCourseAndRaceNumber3(country, date, course, raceNumber);
        }
        return null;
    }


    @GetMapping("/horsecounters")
    public List<HorseNameCountResponse> getHorseNameCountsByRace(@RequestParam String country, @RequestParam String date, @RequestParam String course, @RequestParam String raceNumber,@RequestParam String userId) {
        if(userSubscriptionService.isUserPaymentDone(userId)) {
            return raceService.getHorseNameCountsByRace(country, date, course, raceNumber);
        }
        return null;
    }

    @GetMapping("/details-numerology")
    public List<RaceDto> getDetailsByCountryAndDateAndCourseAndRaceNumberNumerology(@RequestParam String country, @RequestParam String date, @RequestParam String course, @RequestParam String raceNumber, @RequestParam String userId) {
        if(userSubscriptionService.isUserPaymentDone(userId)) {
            return raceService.getDetailsByCountryAndDateAndCourseAndRaceNumberNumerology(country, date, course, raceNumber);
        }
        return null;
    }
//@GetMapping("/details-numerology")
//public List<RaceDto> getDetailsByCountryAndDateAndCourseAndRaceNumberNumerology(@RequestParam String country, @RequestParam String date, @RequestParam String course, @RequestParam String raceNumber, @RequestParam String userId) {
//    if(userSubscriptionService.isUserPaymentDone(userId)) {
//        List<Race> races = raceService.getDetailsByCountryAndDateAndCourseAndRaceNumber(country, date, course, raceNumber);
//        return ruleNumberLogicService.prediction(races);
//    }
//    return null;
//}

    @GetMapping("/details-Astrology")
    public List<RaceDto> getDetailsByCountryAndDateAndCourseAndRaceNumberAstrology(@RequestParam String country, @RequestParam String date, @RequestParam String course, @RequestParam String raceNumber, @RequestParam String userId) {
        if(userSubscriptionService.isUserPaymentDone(userId)) {
            return raceService.getDetailsByCountryAndDateAndCourseAndRaceNumberAstrology(country, date, course, raceNumber);
        }
        return null;
    }

}


