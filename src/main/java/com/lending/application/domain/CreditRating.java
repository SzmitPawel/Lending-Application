package com.lending.application.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "RATING")
public class CreditRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true)
    private Long ratingId;
    @Column(name = "CREDIT_RATING")
    private CreditRatingEnum creditRating;
    @Column(name = "DATE")
    private LocalDate dateOfRating;

    public CreditRating() {
    }

    public CreditRating(
            final CreditRatingEnum creditRating,
            final LocalDate dateOfRating
    ) {
        this.creditRating = creditRating;
        this.dateOfRating = dateOfRating;
    }
}
