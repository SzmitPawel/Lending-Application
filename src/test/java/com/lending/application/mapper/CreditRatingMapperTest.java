package com.lending.application.mapper;

import com.lending.application.domain.CreditRating;
import com.lending.application.domain.CreditRatingEnum;
import com.lending.application.domain.dto.CreditRatingDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CreditRatingMapperTest {
    @Autowired
    CreditRatingMapper creditRatingMapper;

    @Test
    void testMapToCreditRatingDto() {
        // given
        CreditRating creditRating = new CreditRating(
                CreditRatingEnum.ONE,
                LocalDate.now());
        creditRating.setRatingId(1L);

        // when
        CreditRatingDto retrievedCreditRatingDto = creditRatingMapper.mapToCreditRatingDto(creditRating);

        // then
        assertEquals(1L, retrievedCreditRatingDto.getRatingId());
        assertEquals(CreditRatingEnum.ONE, retrievedCreditRatingDto.getCreditRating());
        assertEquals(LocalDate.now(), retrievedCreditRatingDto.getDateOfRating());
    }

    @Test
    void testMapToCreditRating() {
        // given
        CreditRatingDto creditRatingDto = new CreditRatingDto(
                1L,
                CreditRatingEnum.FOUR,
                LocalDate.now()
        );

        // when
        CreditRating retrievedCreditRating = creditRatingMapper.mapToCreditRating(creditRatingDto);

        // then
        assertEquals(1L, retrievedCreditRating.getRatingId());
        assertEquals(CreditRatingEnum.FOUR, retrievedCreditRating.getCreditRating());
        assertEquals(LocalDate.now(), retrievedCreditRating.getDateOfRating());
    }
}