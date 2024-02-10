package com.lending.application.domain.credit.rating;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreditRatingRequestDTO {
    @Schema(description = "Credit Rating",example = "THREE")
    private CreditRatingEnum creditRatingEnum;
    @Schema(description = "Date when Credit Rating was created.", example = "2024-01-30")
    private LocalDate dateOfRating;
}
