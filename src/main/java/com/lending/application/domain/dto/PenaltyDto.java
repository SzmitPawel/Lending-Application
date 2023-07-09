package com.lending.application.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class PenaltyDto {
    private Long penaltyId;
    private Integer penaltyPercentage;
    private LocalDate penaltyDate;
}
