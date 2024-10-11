package net.javaguides.springboot.WebRequest.master;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import net.javaguides.springboot.dto.master.HorseDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class HorseResponse {
    private List<HorseDto> horseDtos;
    private Long totalCount;
}
