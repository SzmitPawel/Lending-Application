package com.lending.application.domain.credit.rating;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "RATING")
public class CreditRating {
    private Long ratingId;
    private CreditRatingEnum creditRating;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true)
    public Long getRatingId() {
        return ratingId;
    }

    @Column(name = "CREDIT_RATING")
    public CreditRatingEnum getCreditRating() {
        return creditRating;
    }

    @Column(name = "DATE")
    public LocalDate getDateOfRating() {
        return dateOfRating;
    }

    public void setRatingId(Long ratingId) {
        this.ratingId = ratingId;
    }

    public void setCreditRating(CreditRatingEnum creditRating) {
        this.creditRating = creditRating;
    }

    public void setDateOfRating(LocalDate dateOfRating) {
        this.dateOfRating = dateOfRating;
    }
}