package com.lending.application.domain.dto;

import com.lending.application.domain.CreditRatingEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class CreditRatingDto {
    private Long ratingId;
    private CreditRatingEnum creditRating;
    private LocalDate dateOfRating;
}
