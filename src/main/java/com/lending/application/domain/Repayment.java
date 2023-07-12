package com.lending.application.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "REPAYMENT")
public class Repayment {
    private Long repaymentId;
    private BigDecimal repaymentAmount;
    private LocalDate repaymentDate;
    private Loan loan;
    private Penalty penalty;

    public Repayment() {
    }

    public Repayment(final BigDecimal repaymentAmount, final LocalDate repaymentDate) {
        this.repaymentAmount = repaymentAmount;
        this.repaymentDate = repaymentDate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true)
    public Long getRepaymentId() {
        return repaymentId;
    }

    @Column(name = "REPAYMENT_AMOUNT", nullable = false)
    public BigDecimal getRepaymentAmount() {
        return repaymentAmount;
    }

    @Column(name = "REPAYMENT_DATE", nullable = false)
    public LocalDate getRepaymentDate() {
        return repaymentDate;
    }

    @ManyToOne
    @JoinColumn(name = "LOAN_ID")
    public Loan getLoan() {
        return loan;
    }

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "PENALTY_ID")
    public Penalty getPenalty() {
        return penalty;
    }

    public void setRepaymentId(Long repaymentId) {
        this.repaymentId = repaymentId;
    }

    public void setRepaymentAmount(BigDecimal repaymentAmount) {
        this.repaymentAmount = repaymentAmount;
    }

    public void setRepaymentDate(LocalDate repaymentDate) {
        this.repaymentDate = repaymentDate;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public void setPenalty(Penalty penalty) {
        this.penalty = penalty;
    }
}