package net.javaguides.springboot.dto;

public class RaceInfo {
    private String raceNumber;
    private String time;

    // Constructors
    public RaceInfo() {
    }

    public RaceInfo(String raceNumber, String time) {
        this.raceNumber = raceNumber;
        this.time = time;
    }

    // Getters and Setters
    public String getRaceNumber() {
        return raceNumber;
    }

    public void setRaceNumber(String raceNumber) {
        this.raceNumber = raceNumber;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
