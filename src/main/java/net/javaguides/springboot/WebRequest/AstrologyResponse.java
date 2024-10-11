package net.javaguides.springboot.WebRequest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import net.javaguides.springboot.dto.AstrologyDto;
import net.javaguides.springboot.dto.master.HorseDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class AstrologyResponse {
    private List<AstrologyDto> astrologyDtos;
    private Long totalCount;
}
