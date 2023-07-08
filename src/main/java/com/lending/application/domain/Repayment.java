package com.lending.application.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "REPAYMENT")
public class Repayment {
    @Setter(AccessLevel.PRIVATE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true)
    private Long repaymentId;
    @Column(name = "REPAYMENT_AMOUNT", nullable = false)
    private BigDecimal repaymentAmount;
    @Column(name = "REPAYMENT_DATE", nullable = false)
    private LocalDate repaymentDate;
    @ManyToOne
    @JoinColumn(name = "LOAN_ID")
    private Loan loan;

    public Repayment() {
    }

    public Repayment(final BigDecimal repaymentAmount, final LocalDate repaymentDate) {
        this.repaymentAmount = repaymentAmount;
        this.repaymentDate = repaymentDate;
    }
}
