package net.javaguides.springboot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
public class TransactionDetails {
    @Id
    @GeneratedValue
    private Long id;
    private String orderId;
    private String currency;
    private Integer amount;
    private String receiptId;
    private String userId;
    private String status;
    @CreationTimestamp
    private LocalDateTime creationTime;
}
