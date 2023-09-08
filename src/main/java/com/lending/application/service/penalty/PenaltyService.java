package com.lending.application.service.penalty;

import com.lending.application.domain.Penalty;
import com.lending.application.domain.dto.PenaltyDto;
import com.lending.application.exception.PenaltyNotFoundException;
import com.lending.application.mapper.PenaltyMapper;
import com.lending.application.repository.PenaltyRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PenaltyService {
    private final PenaltyRepository penaltyRepository;
    private final PenaltyMapper penaltyMapper;

    public PenaltyDto createPenalty(final PenaltyDto penaltyDto) {
        Penalty penalty = penaltyMapper.mapToPenalty(penaltyDto);
        Penalty createdPenalty = penaltyRepository.saveAndFlush(penalty);

        return penaltyMapper.mapToPenaltyDto(createdPenalty);
    }

    public PenaltyDto getPenaltyById(final Long penaltyId) throws PenaltyNotFoundException {
        Penalty penalty = penaltyRepository
                .findById(penaltyId)
                .orElseThrow(PenaltyNotFoundException::new);

        return penaltyMapper.mapToPenaltyDto(penalty);
    }

    public PenaltyDto updatePenalty(final PenaltyDto penaltyDto) throws PenaltyNotFoundException {
        Penalty penalty = penaltyRepository
                .findById(penaltyDto.getPenaltyId())
                .orElseThrow(PenaltyNotFoundException::new);

        penalty.setPenaltyPercentage(penaltyDto.getPenaltyPercentage());
        penalty.setPenaltyDate(penaltyDto.getPenaltyDate());

        Penalty updatedPenalty = penaltyRepository.saveAndFlush(penalty);

        return penaltyMapper.mapToPenaltyDto(updatedPenalty);
    }

    public void deletePenaltyById(final Long penaltyId) throws PenaltyNotFoundException {
        if (penaltyRepository.findById(penaltyId).isPresent()) {
            penaltyRepository.deleteById(penaltyId);
        } else {
            throw new PenaltyNotFoundException();
        }
    }
}