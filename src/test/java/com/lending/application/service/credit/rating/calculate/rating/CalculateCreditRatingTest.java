package com.lending.application.service.credit.rating.calculate.rating;

import com.lending.application.exception.ClientNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalculateCreditRatingTest {
    @InjectMocks
    private CalculateCreditRating calculateCreditRating;
    @Mock
    private CalculateMonthlyTotalExpenses calculateMonthlyTotalExpenses;

    @Test
    void testGetCreditRating_shouldThrowIllegalArgumentException() {
        // given
        Long clientId = 1L;
        BigDecimal customerMonthlyIncome = BigDecimal.valueOf(0.00);
        BigDecimal customerMonthlyExpenses = BigDecimal.valueOf(300.00);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> calculateCreditRating
                .getCreditRating(clientId,customerMonthlyIncome,customerMonthlyExpenses));
    }

    @Test
    void testGetCreditRating_shouldReturnCreditRating() throws ClientNotFoundException {
        // given
        Long clientId = 1L;
        BigDecimal customerMonthlyIncome = BigDecimal.valueOf(2342.33);
        BigDecimal customerMonthlyExpenses = BigDecimal.valueOf(313.23);
        BigDecimal totalMonthlyExpenses = BigDecimal.valueOf(513.23);

        when(calculateMonthlyTotalExpenses
                .getTotalMonthlyExpenses(clientId,customerMonthlyExpenses))
                .thenReturn(totalMonthlyExpenses);

        // when
        int creditRating = calculateCreditRating
                .getCreditRating(clientId, customerMonthlyIncome, customerMonthlyExpenses);

        // then
        assertEquals(35, creditRating);
    }
}