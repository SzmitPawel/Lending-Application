package com.lending.application.service.credit.rating.calculate.rating;

import com.lending.application.domain.credit.rating.CreditRatingEnum;
import com.lending.application.exception.ClientNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CreditRatingEvaluator {
    private final CalculateCreditRating calculateCreditRating;

    public CreditRatingEnum getCreditRatingEnum(
            final Long clientId,
            final BigDecimal customerMonthlyExpenses,
            final BigDecimal customerMonthlyIncome) throws ClientNotFoundException {

        int creditRating = calculateCreditRating
                .getCreditRating(clientId, customerMonthlyIncome, customerMonthlyExpenses);

        if (creditRating >= 0 && creditRating <= 20) {
            return CreditRatingEnum.ONE;
        } else if (creditRating <= 40) {
            return CreditRatingEnum.TWO;
        } else if (creditRating <= 60) {
            return CreditRatingEnum.THREE;
        } else if (creditRating <= 80) {
            return CreditRatingEnum.FOUR;
        } else {
            return CreditRatingEnum.FIVE;
        }
    }

    /*
        1: 0-20% (the best)
        2: 21-40%
        3: 41-60%
        4: 61-80%
        5: 81-100% (the worst)
    */
}