package com.lending.application.service.penalty;

import com.lending.application.domain.penalty.Penalty;
import com.lending.application.exception.PenaltyNotFoundException;
import com.lending.application.repository.PenaltyRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PenaltyServiceTest {
    @Autowired
    private PenaltyService penaltyService;
    @Autowired
    private PenaltyRepository penaltyRepository;

    private Penalty preparePenalty() {
        Penalty penalty = new Penalty();
        penalty.setPenaltyDate(LocalDate.now());
        penalty.setPenaltyPercentage(10);

        return penalty;
    }

    @Test
    void testSavePenalty() {
        // given
        Penalty penalty = preparePenalty();

        // when
        Penalty retrievedPenalty = penaltyService.savePenalty(penalty);

        // then
        assertNotNull(retrievedPenalty);
        assertEquals(penalty.getPenaltyDate(),retrievedPenalty.getPenaltyDate());
        assertEquals(penalty.getPenaltyPercentage(),retrievedPenalty.getPenaltyPercentage());
    }

    @Test
    void testDeletePenaltyById_shouldThrowPenaltyNotFoundException() {
        // given
        Long penaltyId = 999L;

        // when & then
        assertThrows(PenaltyNotFoundException.class, () -> penaltyService.deletePenaltyById(penaltyId));
    }

    @Test
    void testGetPenaltyById() throws PenaltyNotFoundException {
        // given
        Penalty penalty = penaltyRepository.saveAndFlush(preparePenalty());

        // when
        Penalty retrievedPenalty = penaltyService.getPenaltyById(penalty.getPenaltyId());

        // then
        assertNotNull(retrievedPenalty);
        assertEquals(penalty.getPenaltyDate(),retrievedPenalty.getPenaltyDate());
        assertEquals(penalty.getPenaltyPercentage(),retrievedPenalty.getPenaltyPercentage());
    }

    @Test
    void testGetPenaltyById_shouldThrowPenaltyNotFoundException() {
        // given
        Long penaltyId = 999L;

        // when & then
        assertThrows(PenaltyNotFoundException.class, () -> penaltyService.getPenaltyById(penaltyId));
    }

    @Test
    void testDeletePenaltyById() throws PenaltyNotFoundException {
        // given
        Penalty penalty = penaltyRepository.saveAndFlush(preparePenalty());

        // when
        penaltyService.deletePenaltyById(penalty.getPenaltyId());

        // then
        assertThrows(PenaltyNotFoundException.class, () -> penaltyService.deletePenaltyById(penalty.getPenaltyId()));
    }
}