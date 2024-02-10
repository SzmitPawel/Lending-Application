package com.lending.application.domain;

import com.lending.application.domain.credit.rating.CreditRating;
import com.lending.application.domain.credit.rating.CreditRatingEnum;
import com.lending.application.repository.CreditRatingRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CreditRatingTest {
    @Autowired
    CreditRatingRepository creditRatingRepository;

    @Test
    void createCreditRating_shouldReturnCreditRating() {
        // given
        CreditRating creditRating = new CreditRating();
        creditRatingRepository.saveAndFlush(creditRating);

        // when
        CreditRating retrievedCreditRating = creditRatingRepository
                .findById(creditRating.getRatingId())
                .orElse(null);

        // then
        assertNotNull(retrievedCreditRating);
        assertEquals(1, creditRatingRepository.count());
    }

    @Test
    void deleteCreditRating_shouldReturn0() {
        // given
        CreditRating creditRating = new CreditRating();
        creditRatingRepository.saveAndFlush(creditRating);

        // when
        creditRatingRepository.delete(creditRating);

        CreditRating retrievedCreditRating = creditRatingRepository
                .findById(creditRating.getRatingId())
                .orElse(null);

        // then
        assertNull(retrievedCreditRating);
        assertEquals(0, creditRatingRepository.count());
    }

    @Test
    void updateCreditRating_shouldReturnUpdatedData() {
        // given
        CreditRating creditRating = new CreditRating(
                CreditRatingEnum.FOUR,
                LocalDate.of(2023,01,01)
        );
        creditRatingRepository.saveAndFlush(creditRating);

        // when
        creditRating.setCreditRating(CreditRatingEnum.ONE);
        creditRating.setDateOfRating(LocalDate.now());
        creditRatingRepository.saveAndFlush(creditRating);

        CreditRating retrievedCreditRating = creditRatingRepository
                .findById(creditRating.getRatingId())
                .orElse(null);

        // then
        assertNotNull(retrievedCreditRating);
        assertEquals(CreditRatingEnum.ONE, retrievedCreditRating.getCreditRating());
        assertEquals(LocalDate.now(), retrievedCreditRating.getDateOfRating());
    }

    @Test
    void readCreditRating_shouldReturnAllData() {
        // given
        CreditRating creditRating = new CreditRating(
               CreditRatingEnum.ONE,
                LocalDate.now()
        );
        creditRatingRepository.saveAndFlush(creditRating);

        // when
        CreditRating retrievedCreditRating = creditRatingRepository
                .findById(creditRating.getRatingId())
                .orElse(null);

        // then
        assertNotNull(retrievedCreditRating);
        assertEquals(LocalDate.now(), retrievedCreditRating.getDateOfRating());
        assertEquals(CreditRatingEnum.ONE, retrievedCreditRating.getCreditRating());
    }
}