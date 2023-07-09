package com.lending.application.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "PENALTY")
public class Penalty {
    @Setter(AccessLevel.PRIVATE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long penaltyId;
    @Column(name = "PENALTY_PERCENTAGE", nullable = false)
    private Integer penaltyPercentage;
    @Column(name = "PENALTY_DATE", nullable = false)
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

    public Penalty(
            final Long penaltyId,
            final Integer penaltyPercentage,
            final LocalDate penaltyDate
    ) {
        this.penaltyId = penaltyId;
        this.penaltyPercentage = penaltyPercentage;
        this.penaltyDate = penaltyDate;
    }
}
