package net.javaguides.springboot.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NatureDayValue {
    private int dayValue;
    private String natureOfDay;
}
