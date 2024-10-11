package net.javaguides.springboot.WebRequest.master;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import net.javaguides.springboot.dto.RaceDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class RaceResponse {
    private List<RaceDto> raceDtos;
    private Long totalCount;
}
