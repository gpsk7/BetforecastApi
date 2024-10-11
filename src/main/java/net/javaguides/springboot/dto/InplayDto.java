package net.javaguides.springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InplayDto {
    private Long id;
    private double time;
    private String date;
    private String course;
    private String country;
    private int position1;
    private int position2;
    private int position3;
    private String odds;
}
