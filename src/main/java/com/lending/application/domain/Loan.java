package com.lending.application.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "LOAN")
public class Loan {
    @Id
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
    @ManyToOne
    @JoinColumn(name = "CLIENT_ID")
    private Client client;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY, mappedBy = "loan",targetEntity = Repayment.class)
    List<Repayment> repaymentList = new ArrayList<>();

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

    public Loan(
            final Long loanId,
            final BigDecimal loanAmount,
            final Float interest,
            final LocalDate loanStartDate,
            final Integer repaymentPeriod
    ) {
        this.loanId = loanId;
        this.loanAmount = loanAmount;
        this.interest = interest;
        this.loanStartDate = loanStartDate;
        this.repaymentPeriod = repaymentPeriod;
    }
}