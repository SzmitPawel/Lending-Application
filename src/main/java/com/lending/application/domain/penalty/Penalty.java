package com.lending.application.domain.penalty;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "PENALTY")
public class Penalty {
    private Long penaltyId;
    private Integer penaltyPercentage;
    private LocalDate penaltyDate;

    public Penalty() {
    }

    public Penalty(
            final Integer penaltyPercentage,
            final LocalDate penaltyDate
    ) {
        this.penaltyPercentage = penaltyPercentage;
        this.penaltyDate = penaltyDate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    public Long getPenaltyId() {
        return penaltyId;
    }

    @Column(name = "PENALTY_PERCENTAGE", nullable = false)
    public Integer getPenaltyPercentage() {
        return penaltyPercentage;
    }

    @Column(name = "PENALTY_DATE", nullable = false)
    public LocalDate getPenaltyDate() {
        return penaltyDate;
    }

    public void setPenaltyId(Long penaltyId) {
        this.penaltyId = penaltyId;
    }

    public void setPenaltyPercentage(Integer penaltyPercentage) {
        this.penaltyPercentage = penaltyPercentage;
    }

    public void setPenaltyDate(LocalDate penaltyDate) {
        this.penaltyDate = penaltyDate;
    }
}