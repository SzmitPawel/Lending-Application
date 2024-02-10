package com.lending.application.domain.credit.rating;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreditRatingResponseDTO {
    @Schema(description = "Credit Rating id.", example = "1")
    private Long ratingId;
    @Schema(description = "Credit Rating.", example = "FIVE")
    private CreditRatingEnum creditRating;
    @Schema(description = "Date of Credit Rating.", example = "2024-01-30")
    private LocalDate dateOfRating;
}
