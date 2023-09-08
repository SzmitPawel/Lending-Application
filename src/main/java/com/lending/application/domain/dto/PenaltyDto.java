package com.lending.application.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PenaltyDto {
    private Long penaltyId;
    private Integer penaltyPercentage;
    private LocalDate penaltyDate;
}
