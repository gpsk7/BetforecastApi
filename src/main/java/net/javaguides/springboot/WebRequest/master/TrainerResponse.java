package net.javaguides.springboot.WebRequest.master;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import net.javaguides.springboot.dto.master.TrainerDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class TrainerResponse {
    private List<TrainerDto> trainerDtos;
    private Long totalCount;
}
