package com.lending.application.domain;

import com.lending.application.repository.CreditRatingRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class CreditRatingTest {
    @Autowired
    CreditRatingRepository creditRatingRepository;

    @Test
    void createCreditRating_shouldReturn1() {
        // given
        CreditRating creditRating = new CreditRating();

        creditRatingRepository.save(creditRating);

        // when & then
        assertEquals(1, creditRatingRepository.count());
    }

    @Test
    void deleteCreditRating_shouldReturn0() {
        // given
        CreditRating creditRating = new CreditRating();

        creditRatingRepository.save(creditRating);

        // when
        creditRatingRepository.delete(creditRating);

        // then
        assertEquals(0, creditRatingRepository.count());
    }

    @Test
    void updateCreditRating_shouldReturnUpdatedData() {
        // given
        CreditRating creditRating = new CreditRating(
                CreditRatingEnum.FOUR,
                LocalDate.of(2023,01,01)
        );

        creditRatingRepository.save(creditRating);

        // when
        creditRating.setCreditRating(CreditRatingEnum.ONE);
        creditRating.setDateOfRating(LocalDate.now());
        creditRatingRepository.save(creditRating);

        // then
        assertEquals(CreditRatingEnum.ONE,creditRatingRepository
                .findAll()
                .get(0)
                .getCreditRating());
        assertEquals(LocalDate.now(), creditRatingRepository
                .findAll()
                .get(0)
                .getDateOfRating());
    }

    @Test
    void readCreditRating_shouldReturnAllData() {
        // given
        CreditRating creditRating = new CreditRating(
               CreditRatingEnum.ONE,
                LocalDate.now()
        );

        creditRatingRepository.save(creditRating);

        // when & then
        assertEquals(LocalDate.now(), creditRatingRepository
                .findAll()
                .get(0)
                .getDateOfRating());
        assertEquals(CreditRatingEnum.ONE, creditRatingRepository
                .findAll()
                .get(0)
                .getCreditRating());
    }
}