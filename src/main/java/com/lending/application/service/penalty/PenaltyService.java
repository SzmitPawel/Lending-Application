package com.lending.application.service.penalty;

import com.lending.application.domain.Penalty;
import com.lending.application.domain.dto.PenaltyDto;
import com.lending.application.exception.PenaltyNotFoundException;
import com.lending.application.mapper.PenaltyMapper;
import com.lending.application.repository.PenaltyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PenaltyService {
    PenaltyRepository penaltyRepository;
    PenaltyMapper penaltyMapper;

    public void createPenalty(final PenaltyDto penaltyDto) {
        penaltyRepository.saveAndFlush(penaltyMapper.mapToPenalty(penaltyDto));
    }

    public PenaltyDto getPenaltyById(final Long penaltyId) throws PenaltyNotFoundException {
        Penalty penalty = penaltyRepository
                .findById(penaltyId)
                .orElseThrow(PenaltyNotFoundException::new);

        return penaltyMapper.mapToPenaltyDto(penalty);
    }

    public void updatePenalty(final PenaltyDto penaltyDto) throws PenaltyNotFoundException {
        Penalty penalty = penaltyRepository
                .findById(penaltyDto.getPenaltyId())
                .orElseThrow(PenaltyNotFoundException::new);

        penalty.setPenaltyPercentage(penaltyDto.getPenaltyPercentage());
        penalty.setPenaltyDate(penaltyDto.getPenaltyDate());

        penaltyRepository.saveAndFlush(penalty);
    }

    public void deletePenaltyById(final Long penaltyId) throws PenaltyNotFoundException {
        if (penaltyRepository.findById(penaltyId).isPresent()) {
            penaltyRepository.deleteById(penaltyId);
        } else {
            throw new PenaltyNotFoundException();
        }
    }
}