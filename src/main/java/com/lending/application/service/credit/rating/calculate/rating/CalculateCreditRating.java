package com.lending.application.service.credit.rating.calculate.rating;

import com.lending.application.exception.ClientNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@RequiredArgsConstructor
class CalculateCreditRating {
    private final CalculateMonthlyTotalExpenses calculateMonthlyTotalExpenses;

    public int getCreditRating(
            final Long clientId,
            final BigDecimal clientMonthlyIncome,
            final BigDecimal clientMonthlyExpenses) throws ClientNotFoundException {

        if (clientMonthlyIncome.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("Customer monthly cannot be zero.");
        }

        BigDecimal totalExpenses = calculateMonthlyTotalExpenses
                .getTotalMonthlyExpenses(clientId,clientMonthlyExpenses)
                .add(clientMonthlyExpenses);

        return totalExpenses
                .divide(clientMonthlyIncome,2,RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .intValue();
    }
}