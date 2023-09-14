package com.lending.application.service.penalty;

import com.lending.application.domain.Penalty;
import com.lending.application.exception.PenaltyNotFoundException;
import com.lending.application.repository.PenaltyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PenaltyService {
    private final PenaltyRepository penaltyRepository;

    public Penalty savePenalty(final Penalty penalty) {
        return penaltyRepository.saveAndFlush(penalty);
    }

    public Penalty getPenaltyById(final Long penaltyId) throws PenaltyNotFoundException {
        return penaltyRepository.findById(penaltyId).orElseThrow(PenaltyNotFoundException::new);
    }

    public void deletePenaltyById(final Long penaltyId) throws PenaltyNotFoundException {
        if (penaltyRepository.findById(penaltyId).isPresent()) {
            penaltyRepository.deleteById(penaltyId);
        } else {
            throw new PenaltyNotFoundException();
        }
    }
}