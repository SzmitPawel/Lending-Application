package com.lending.application.domain.transaction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class TransactionResponseDTO {
    private Long transactionId;
    private BigDecimal transactionAmount;
    private LocalDate transactionDate;
    private TransactionMethodEnum transactionMethodEnum;
}
