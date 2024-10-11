package net.javaguides.springboot.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class Coupon extends AbstractEntity {
    private String coupon;
    @CreationTimestamp
    private LocalDateTime creationAt;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate ExpirationDate;
    private Integer percentage;
}
