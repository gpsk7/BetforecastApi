package net.javaguides.springboot.WebRequest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import net.javaguides.springboot.dto.InplayDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class InplayResponse {
    private List<InplayDto> inplayDtos;
    private long totalCount;
}
