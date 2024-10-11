package net.javaguides.springboot.dto.master;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HorseDto {
    private Long id;
    private String name;
    private String breed;
    private String gender;
    private String color;
    private Integer age;
    private Float height;
    private Float weight;
    private Integer registrationNumber;
    private Integer count;
    private String country;
    private Integer speed;
    private String dam;
    private String sire;
}
