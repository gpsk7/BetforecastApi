package net.javaguides.springboot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserSubscription {
    @Id
    @GeneratedValue
    private Long id;
    @CreationTimestamp
    private LocalDateTime createdAt;
    private String userId;
    private LocalDateTime validFrom;
    private LocalDateTime ExpirationDate;
}
