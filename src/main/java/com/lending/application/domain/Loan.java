package com.lending.application.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "LOAN")
public class Loan {
    @Id
    @Setter(AccessLevel.PRIVATE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true)
    private Long loanId;
    @Column(name = "LOAN_AMOUNT", nullable = false)
    private BigDecimal loanAmount;
    @Column(name = "INTEREST", nullable = false)
    private Float interest;
    @Column(name = "LOAN_START", nullable = false)
    private LocalDate loanStartDate;
    @Column(name = "REPAYMENT_PERIOD", nullable = false)
    private Integer repaymentPeriod;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENT_ID")
    private Client client;

    public Loan() {
    }

    public Loan(
            final BigDecimal loanAmount,
            final Float interest,
            final LocalDate loanStartDate,
            final Integer repaymentPeriod
    ) {
        this.loanAmount = loanAmount;
        this.interest = interest;
        this.loanStartDate = loanStartDate;
        this.repaymentPeriod = repaymentPeriod;
    }
}
