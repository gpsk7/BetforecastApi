package net.javaguides.springboot.dto.master;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrainerDto {
    private Long id;
    private String trainerName;
    private Integer trainerCount;
    private Integer trainerRegistrationId;
    private String trainerCountry;
    private String trainerStable;
    private Integer trainerAge;
    private String trainerGender;
}
