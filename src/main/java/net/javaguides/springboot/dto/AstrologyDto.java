package net.javaguides.springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class AstrologyDto {
    private Long id;
    private String time;
    private String date;
    private String dasha;
    private String antarDasha;
    private String ownHouse;
}
