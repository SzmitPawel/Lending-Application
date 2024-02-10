package com.lending.application.service.credit.rating.calculate.rating;

import com.lending.application.domain.credit.rating.CreditRatingEnum;
import com.lending.application.exception.ClientNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreditRatingEvaluatorTest {
    @InjectMocks
    private CreditRatingEvaluator creditRatingEvaluator;
    @Mock
    private CalculateCreditRating calculateCreditRating;

    @Test
    void testGetCreditRatingEnum_shouldReturnOne_1() throws ClientNotFoundException {
        // given
        int creditRating = 0;
        when(calculateCreditRating
                .getCreditRating(any(),any(BigDecimal.class),any(BigDecimal.class)))
                .thenReturn(creditRating);

        // when
        CreditRatingEnum creditRatingEnum = creditRatingEvaluator
                .getCreditRatingEnum(1L,BigDecimal.valueOf(1),BigDecimal.valueOf(1));

        // then
        assertEquals(CreditRatingEnum.ONE, creditRatingEnum);
    }

    @Test
    void testGetCreditRatingEnum_shouldReturnOne_2() throws ClientNotFoundException {
        // given
        int creditRating = 20;
        when(calculateCreditRating
                .getCreditRating(any(),any(BigDecimal.class),any(BigDecimal.class)))
                .thenReturn(creditRating);

        // when
        CreditRatingEnum creditRatingEnum = creditRatingEvaluator
                .getCreditRatingEnum(1L,BigDecimal.valueOf(1),BigDecimal.valueOf(1));

        // then
        assertEquals(CreditRatingEnum.ONE, creditRatingEnum);
    }

    @Test
    void testGetCreditRatingEnum_shouldReturnTwo_1() throws ClientNotFoundException {
        // given
        int creditRating = 21;
        when(calculateCreditRating
                .getCreditRating(any(),any(BigDecimal.class),any(BigDecimal.class)))
                .thenReturn(creditRating);

        // when
        CreditRatingEnum creditRatingEnum = creditRatingEvaluator
                .getCreditRatingEnum(1L,BigDecimal.valueOf(1),BigDecimal.valueOf(1));

        // then
        assertEquals(CreditRatingEnum.TWO, creditRatingEnum);
    }

    @Test
    void testGetCreditRatingEnum_shouldReturnTwo_2() throws ClientNotFoundException {
        // given
        int creditRating = 40;
        when(calculateCreditRating
                .getCreditRating(any(),any(BigDecimal.class),any(BigDecimal.class)))
                .thenReturn(creditRating);

        // when
        CreditRatingEnum creditRatingEnum = creditRatingEvaluator
                .getCreditRatingEnum(1L,BigDecimal.valueOf(1),BigDecimal.valueOf(1));

        // then
        assertEquals(CreditRatingEnum.TWO, creditRatingEnum);
    }

    @Test
    void testGetCreditRatingEnum_shouldReturnThree_1() throws ClientNotFoundException {
        // given
        int creditRating = 41;
        when(calculateCreditRating
                .getCreditRating(any(),any(BigDecimal.class),any(BigDecimal.class)))
                .thenReturn(creditRating);

        // when
        CreditRatingEnum creditRatingEnum = creditRatingEvaluator
                .getCreditRatingEnum(1L,BigDecimal.valueOf(1),BigDecimal.valueOf(1));

        // then
        assertEquals(CreditRatingEnum.THREE, creditRatingEnum);
    }

    @Test
    void testGetCreditRatingEnum_shouldReturnThree_2() throws ClientNotFoundException {
        // given
        int creditRating = 60;
        when(calculateCreditRating
                .getCreditRating(any(),any(BigDecimal.class),any(BigDecimal.class)))
                .thenReturn(creditRating);

        // when
        CreditRatingEnum creditRatingEnum = creditRatingEvaluator
                .getCreditRatingEnum(1L,BigDecimal.valueOf(1),BigDecimal.valueOf(1));

        // then
        assertEquals(CreditRatingEnum.THREE, creditRatingEnum);
    }

    @Test
    void testGetCreditRatingEnum_shouldReturnFour_1() throws ClientNotFoundException {
        // given
        int creditRating = 61;
        when(calculateCreditRating
                .getCreditRating(any(),any(BigDecimal.class),any(BigDecimal.class)))
                .thenReturn(creditRating);

        // when
        CreditRatingEnum creditRatingEnum = creditRatingEvaluator
                .getCreditRatingEnum(1L,BigDecimal.valueOf(1),BigDecimal.valueOf(1));

        // then
        assertEquals(CreditRatingEnum.FOUR, creditRatingEnum);
    }

    @Test
    void testGetCreditRatingEnum_shouldReturnFour_2() throws ClientNotFoundException {
        // given
        int creditRating = 80;
        when(calculateCreditRating
                .getCreditRating(any(),any(BigDecimal.class),any(BigDecimal.class)))
                .thenReturn(creditRating);

        // when
        CreditRatingEnum creditRatingEnum = creditRatingEvaluator
                .getCreditRatingEnum(1L,BigDecimal.valueOf(1),BigDecimal.valueOf(1));

        // then
        assertEquals(CreditRatingEnum.FOUR, creditRatingEnum);
    }

    @Test
    void testGetCreditRatingEnum_shouldReturnFive_1() throws ClientNotFoundException {
        // given
        int creditRating = 81;
        when(calculateCreditRating
                .getCreditRating(any(), any(BigDecimal.class), any(BigDecimal.class)))
                .thenReturn(creditRating);

        // when
        CreditRatingEnum creditRatingEnum = creditRatingEvaluator
                .getCreditRatingEnum(1L, BigDecimal.valueOf(1), BigDecimal.valueOf(1));

        // then
        assertEquals(CreditRatingEnum.FIVE, creditRatingEnum);
    }

    @Test
    void testGetCreditRatingEnum_shouldReturnFive_2() throws ClientNotFoundException {
        // given
        int creditRating = 100;
        when(calculateCreditRating
                .getCreditRating(any(), any(BigDecimal.class), any(BigDecimal.class)))
                .thenReturn(creditRating);

        // when
        CreditRatingEnum creditRatingEnum = creditRatingEvaluator
                .getCreditRatingEnum(1L, BigDecimal.valueOf(1), BigDecimal.valueOf(1));

        // then
        assertEquals(CreditRatingEnum.FIVE, creditRatingEnum);
    }
}