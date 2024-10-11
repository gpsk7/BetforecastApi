package net.javaguides.springboot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "natureofdayandvalue")
@Builder
public class NatureOfDayAndValue {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "DATE")
    private String date;

    @Column(name = "DayValue")
    private int dayValue;

    @Column(name = "Nakshatra")
    private String nakshatra;

    @Column(name = "NatureofDay")
    private String natureOfDay;

    @Column(name = "NakshatraNumber")
    private String nakshatraNumber;

    @Column(name = "NatureofDayNumber")
    private String natureOfDayNo;

    @Column(name = "NRuler")
    private String nruler;

    @Column(name = "RColour")
    private String rcolour;

    @Column(name = "RColourNumber")
    private String rcolourNo;

    @Column(name = "NRulerNumber")
    private String nrulerNumber;


}
