package com.lending.application.domain.credit.rating;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class CreditRatingRequestDTO {
    @Schema(description = "Credit Rating",example = "THREE")
    private CreditRatingEnum creditRatingEnum;
    @Schema(description = "Date when Credit Rating was created.", example = "2024-01-30")
    private LocalDate dateOfRating;
}
