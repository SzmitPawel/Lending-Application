package com.lending.application.mapper;

import com.lending.application.domain.penalty.Penalty;
import com.lending.application.domain.penalty.PenaltyDto;
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
        Penalty penalty = new Penalty();
        penalty.setPenaltyId(penaltyDto.getPenaltyId());
        penalty.setPenaltyPercentage(penaltyDto.getPenaltyPercentage());
        penalty.setPenaltyDate(penaltyDto.getPenaltyDate());

        return penalty;
    }
}