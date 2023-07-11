package com.lending.application.mapper;

import com.lending.application.domain.Penalty;
import com.lending.application.domain.dto.PenaltyDto;
import org.springframework.stereotype.Component;

@Component
public class PenaltyMapper {
    public PenaltyDto mapToPenaltyDto(final Penalty penalty) {
        return new PenaltyDto(
                penalty.getPenaltyId(),
                penalty.getPenaltyPercentage(),
                penalty.getPenaltyDate()
        );
    }

    public Penalty mapToPenalty(final PenaltyDto penaltyDto) {
        return new Penalty(
                penaltyDto.getPenaltyId(),
                penaltyDto.getPenaltyPercentage(),
                penaltyDto.getPenaltyDate()
        );
    }
}
