package com.lending.application.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@Table(name = "REPAYMENT")
public class Repayment {
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
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "PENALTY_ID")
    private Penalty penalty;

    public Repayment() {
    }

    public Repayment(final BigDecimal repaymentAmount, final LocalDate repaymentDate) {
        this.repaymentAmount = repaymentAmount;
        this.repaymentDate = repaymentDate;
    }

    public Repayment(
            final Long repaymentId,
            final BigDecimal repaymentAmount,
            final LocalDate repaymentDate
    ) {
        this.repaymentId = repaymentId;
        this.repaymentAmount = repaymentAmount;
        this.repaymentDate = repaymentDate;
    }
}