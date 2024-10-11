package net.javaguides.springboot.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MoonDayDto {
    private Long id;
    private String date;
    private String moonDay;
}
