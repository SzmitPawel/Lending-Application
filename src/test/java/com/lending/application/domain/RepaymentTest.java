package com.lending.application.domain;

import com.lending.application.repository.RepaymentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RepaymentTest {
    @Autowired
    RepaymentRepository repaymentRepository;

    @Test
    void createRepaymentWithoutAmountAndDate_shouldReturnDataIntegrityViolationException() {
        // given
        Repayment repayment = new Repayment();

        // when & then
        assertThrows(DataIntegrityViolationException.class, () -> repaymentRepository.saveAndFlush(repayment));
    }

    @Test
    void createRepayment_shouldReturnRepayment() {
        // given
        Repayment repayment = new Repayment(new BigDecimal(100.00), LocalDate.now());
        repaymentRepository.saveAndFlush(repayment);

        // when
        Repayment retrievedRepayment = repaymentRepository
                .findById(repayment.getRepaymentId())
                .orElse(null);

        // then
        assertNotNull(retrievedRepayment);
        assertEquals(1, repaymentRepository.count());
    }

    @Test
    void deleteRepayment_shouldReturn0() {
        // given
        Repayment repayment = new Repayment(new BigDecimal(100.00), LocalDate.now());
        repaymentRepository.saveAndFlush(repayment);

        // when
        repaymentRepository.deleteAll();
        Repayment retrievedRepaymentAfterDelete = repaymentRepository
                .findById(repayment.getRepaymentId())
                .orElse(null);

        // then
        assertNull(retrievedRepaymentAfterDelete);
        assertEquals(0, repaymentRepository.count());
    }

    @Test
    void updateRepayment_shouldReturnUpdatedData() {
        // given
        Repayment repayment = new Repayment(new BigDecimal(100.00), LocalDate.now());
        repaymentRepository.saveAndFlush(repayment);

        // when
        repayment.setRepaymentAmount(new BigDecimal(200.00));
        repayment.setRepaymentDate(LocalDate.of(2023,01,01));
        repaymentRepository.saveAndFlush(repayment);

        Repayment retrievedRepaymentAfterUpdate = repaymentRepository
                .findById(repayment.getRepaymentId())
                .orElse(null);

        // then
        assertNotNull(retrievedRepaymentAfterUpdate);
        assertEquals(new BigDecimal(200), retrievedRepaymentAfterUpdate.getRepaymentAmount());
        assertEquals(LocalDate.of(2023,01,01), retrievedRepaymentAfterUpdate.getRepaymentDate());
    }

    @Test
    void readRepayment_shouldReturnAllData() {
        // given
        Repayment repayment = new Repayment(new BigDecimal(100.00), LocalDate.now());
        repaymentRepository.saveAndFlush(repayment);

        // when
        Repayment retrievedRepayment = repaymentRepository
                .findById(repayment.getRepaymentId())
                .orElse(null);

        // then
        assertNotNull(retrievedRepayment);
        assertEquals(new BigDecimal(100.00), retrievedRepayment.getRepaymentAmount());
        assertEquals(LocalDate.now(), retrievedRepayment.getRepaymentDate());
    }
}