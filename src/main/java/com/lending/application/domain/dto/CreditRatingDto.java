package com.lending.application.domain.dto;

import com.lending.application.domain.CreditRatingEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreditRatingDto {
    private Long ratingId;
    private CreditRatingEnum creditRating;
    private LocalDate dateOfRating;
}
