package com.lending.application.relations;

import com.lending.application.domain.Penalty;
import com.lending.application.domain.Repayment;
import com.lending.application.repository.PenaltyRepository;
import com.lending.application.repository.RepaymentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class RepaymentToPenaltyRelationsTest {
    @Autowired
    RepaymentRepository repaymentRepository;
    @Autowired
    PenaltyRepository penaltyRepository;

    @Test
    void createRepaymentWithPenalty_shouldReturnPenalty() {
        // given
        Repayment repayment = new Repayment(
                new BigDecimal(100.00),
                LocalDate.now()
        );
        Penalty penalty = new Penalty(10, LocalDate.now());
        repayment.setPenalty(penalty);
        repaymentRepository.saveAndFlush(repayment);

        // when
        Repayment retrievedRepayment = repaymentRepository.findById(repayment.getRepaymentId()).orElse(null);

        // when & then
        assertNotNull(retrievedRepayment);
        assertEquals(10, retrievedRepayment.getPenalty().getPenaltyPercentage());
        assertEquals(LocalDate.now(), retrievedRepayment.getPenalty().getPenaltyDate());
        assertEquals(1, repaymentRepository.count());
        assertEquals(1, penaltyRepository.count());
    }

    @Test
    void deletePenaltyFromRepayment_shouldReturnOnlyRepayment() {
        // given
        Repayment repayment = new Repayment(
                new BigDecimal(100.00),
                LocalDate.now()
        );
        Penalty penalty = new Penalty(10, LocalDate.now());
        repayment.setPenalty(penalty);
        repaymentRepository.saveAndFlush(repayment);

        // when
        Repayment retrievedRepayment = repaymentRepository.findById(repayment.getRepaymentId()).orElse(null);
        retrievedRepayment.setPenalty(null);
        repaymentRepository.saveAndFlush(retrievedRepayment);
        penaltyRepository.delete(penalty);

        Repayment retrievedRepaymentAfterDelete = repaymentRepository.findById(repayment.getRepaymentId()).orElse(null);

        // then
        assertNotNull(retrievedRepaymentAfterDelete);
        assertNull(retrievedRepaymentAfterDelete.getPenalty());
        assertEquals(1, repaymentRepository.count());
        assertEquals(0, penaltyRepository.count());
    }

    @Test
    void deleteRepaymentAndPenalty_shouldReturn0RepaymentAnd0Penalty() {
        // given
        Repayment repayment = new Repayment(
                new BigDecimal(100.00),
                LocalDate.now()
        );
        Penalty penalty = new Penalty(10, LocalDate.now());
        repayment.setPenalty(penalty);
        repaymentRepository.saveAndFlush(repayment);

        // when
        repaymentRepository.delete(repayment);

        // then
        assertEquals(0, repaymentRepository.count());
        assertEquals(0, penaltyRepository.count());
    }

    @Test
    void updatePenaltyFromRepayment_shouldReturnUpdatedData() {
        // given
        Repayment repayment = new Repayment(
                new BigDecimal(100.00),
                LocalDate.now()
        );
        Penalty penalty = new Penalty(10, LocalDate.now());
        repayment.setPenalty(penalty);
        repaymentRepository.saveAndFlush(repayment);

        // when
        penalty.setPenaltyPercentage(5);
        penaltyRepository.saveAndFlush(penalty);

        Repayment retrievedRepaymentAfterUpdate = repaymentRepository
                .findById(repayment.getRepaymentId())
                .orElse(null);

        // then
        assertNotNull(retrievedRepaymentAfterUpdate);
        assertEquals(5, retrievedRepaymentAfterUpdate.getPenalty().getPenaltyPercentage());
    }

    @Test
    void readPenaltyFromRepayment_shouldReturnPenalty() {
        // given
        Repayment repayment = new Repayment(
                new BigDecimal(100.00),
                LocalDate.now()
        );
        Penalty penalty = new Penalty(10, LocalDate.now());
        repayment.setPenalty(penalty);
        repaymentRepository.saveAndFlush(repayment);

        // when
        Repayment retrievedRepayment = repaymentRepository.findById(repayment.getRepaymentId()).orElse(null);

        // then
        assertNotNull(retrievedRepayment);
        assertEquals(10, retrievedRepayment.getPenalty().getPenaltyPercentage());
        assertEquals(LocalDate.now(), retrievedRepayment.getPenalty().getPenaltyDate());
    }
}
