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
    void createRepayment_shouldReturn1() {
        // given
        Repayment repayment = new Repayment(new BigDecimal(100.00), LocalDate.now());

        // when
        repaymentRepository.saveAndFlush(repayment);

        // then
        assertEquals(1, repaymentRepository.count());
    }

    @Test
    void deleteRepayment_shouldReturn0() {
        // given
        Repayment repayment = new Repayment(new BigDecimal(100.00), LocalDate.now());
        repaymentRepository.saveAndFlush(repayment);

        // when
        repaymentRepository.deleteAll();

        // then
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

        // then
        assertEquals(new BigDecimal(200), repaymentRepository.findAll()
                .get(0)
                .getRepaymentAmount());
        assertEquals(LocalDate.of(2023,01,01), repaymentRepository.findAll()
                .get(0)
                .getRepaymentDate());
    }

    @Test
    void readRepayment_shouldReturnAllData() {
        // given
        Repayment repayment = new Repayment(new BigDecimal(100.00), LocalDate.now());
        repaymentRepository.saveAndFlush(repayment);

        // when & then
        assertEquals(new BigDecimal(100.00), repaymentRepository.findAll()
                .get(0)
                .getRepaymentAmount());
        assertEquals(LocalDate.now(), repaymentRepository.findAll()
                .get(0)
                .getRepaymentDate());
    }
}