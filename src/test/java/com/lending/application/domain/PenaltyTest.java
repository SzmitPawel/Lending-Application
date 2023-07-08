package com.lending.application.domain;

import com.lending.application.repository.PenaltyRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class PenaltyTest {
    @Autowired
    PenaltyRepository penaltyRepository;

    @Test
    void createPenaltyWithoutPercentAndDate_shouldReturnDataIntegrityViolationException() {
        // given
        Penalty penalty = new Penalty();

        // when & then
        assertThrows(DataIntegrityViolationException.class, () -> penaltyRepository.saveAndFlush(penalty));
    }

    @Test
    void createPenalty_shouldReturnPenalty() {
        // given
        Penalty penalty = new Penalty(5, LocalDate.now());
        penaltyRepository.saveAndFlush(penalty);

        // when
        Penalty retrievedPenalty = penaltyRepository.findById(penalty.getPenaltyId()).orElse(null);

        // when & then
        assertNotNull(retrievedPenalty);
        assertEquals(1, penaltyRepository.count());
    }

    @Test
    void deletePenalty_shouldReturn0() {
        // given
        Penalty penalty = new Penalty(5, LocalDate.now());
        penaltyRepository.saveAndFlush(penalty);

        // when
        penaltyRepository.delete(penalty);
        Penalty retrievedPenaltyAfterDelete = penaltyRepository.findById(penalty.getPenaltyId()).orElse(null);

        // then
        assertNull(retrievedPenaltyAfterDelete);
        assertEquals(0, penaltyRepository.count());
    }

    @Test
    void updatePenalty_shouldReturnUpdatedData() {
        // given
        Penalty penalty = new Penalty(5, LocalDate.now());
        penaltyRepository.saveAndFlush(penalty);

        // when
        penalty.setPenaltyDate(LocalDate.of(2023,01,01));
        penalty.setPenaltyPercentage(10);

        Penalty retrievedPenaltyAfterUpdate = penaltyRepository.findById(penalty.getPenaltyId()).orElse(null);

        // then
        assertNotNull(retrievedPenaltyAfterUpdate);
        assertEquals(LocalDate.of(2023,01,01), retrievedPenaltyAfterUpdate.getPenaltyDate());
        assertEquals(10, retrievedPenaltyAfterUpdate.getPenaltyPercentage());
    }

    @Test
    void readPenalty_shouldReturnAllData() {
        // given
        Penalty penalty = new Penalty(5, LocalDate.now());
        penaltyRepository.saveAndFlush(penalty);

        // when
        Penalty retrievedPenalty = penaltyRepository.findById(penalty.getPenaltyId()).orElse(null);

        // then
        assertNotNull(retrievedPenalty);
        assertEquals(LocalDate.now(), retrievedPenalty.getPenaltyDate());
        assertEquals(5, retrievedPenalty.getPenaltyPercentage());
    }
}