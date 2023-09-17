package com.lending.application.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "LOAN")
public class Loan {
    private Long loanId;
    private BigDecimal loanAmount;
    private BigDecimal monthlyPayment;
    private Float interest;
    private LocalDate loanStartDate;
    private Integer repaymentPeriod;
    private Client client;
    List<Repayment> repaymentList = new ArrayList<>();

    public Loan() {
    }

    public Loan(
            final BigDecimal loanAmount,
            final BigDecimal installmentAmount,
            final Float interest,
            final LocalDate loanStartDate,
            final Integer repaymentPeriod
    ) {
        this.loanAmount = loanAmount;
        this.monthlyPayment = installmentAmount;
        this.interest = interest;
        this.loanStartDate = loanStartDate;
        this.repaymentPeriod = repaymentPeriod;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true)
    public Long getLoanId() {
        return loanId;
    }

    @Column(name = "LOAN_AMOUNT", nullable = false)
    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    @Column(name = "INSTALLMENT_AMOUNT")
    public BigDecimal getMonthlyPayment() {
        return monthlyPayment;
    }

    @Column(name = "INTEREST", nullable = false)
    public Float getInterest() {
        return interest;
    }

    @Column(name = "LOAN_START", nullable = false)
    public LocalDate getLoanStartDate() {
        return loanStartDate;
    }

    @Column(name = "REPAYMENT_PERIOD", nullable = false)
    public Integer getRepaymentPeriod() {
        return repaymentPeriod;
    }

    @ManyToOne
    @JoinColumn(name = "CLIENT_ID")
    public Client getClient() {
        return client;
    }

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY, mappedBy = "loan",targetEntity = Repayment.class)
    public List<Repayment> getRepaymentList() {
        return repaymentList;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public void setMonthlyPayment(BigDecimal installmentAmount) {
        this.monthlyPayment = installmentAmount;
    }

    public void setInterest(Float interest) {
        this.interest = interest;
    }

    public void setLoanStartDate(LocalDate loanStartDate) {
        this.loanStartDate = loanStartDate;
    }

    public void setRepaymentPeriod(Integer repaymentPeriod) {
        this.repaymentPeriod = repaymentPeriod;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setRepaymentList(List<Repayment> repaymentList) {
        this.repaymentList = repaymentList;
    }
}