package net.javaguides.springboot.WebRequest;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionRequest {
    private Double amount;
    private String userId;
}
