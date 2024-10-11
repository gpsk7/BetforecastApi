package net.javaguides.springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RaceDto {
    private Long id;
    private String date;
    private String course;
    private String country;
    private String time;
    private String raceId;
    private String horseName;
    private String jockeyName;
    private String trainerName;
    private int speed;
    private String horseBallot;
    private String finishPos;
    private int dayValue;
    private String raceNumber;
    private String predictionColor;
    private int age;
    private int distance;
    private int horseWeight;
    private int drawnStall;
    private int sdHorseBallot;
    private int sdDrawnStall;
}
