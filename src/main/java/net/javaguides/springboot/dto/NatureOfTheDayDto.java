package net.javaguides.springboot.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NatureOfTheDayDto {
    private Long id;
    private String date;
    private int dayValue;
    private String nakshatra;
    private String natureOfDay;
    private String nakshatraNumber;
    private String natureOfDayNo;
    private String nruler;
    private String rcolour;
    private String rcolourNo;
    private String nrulerNumber;

}
