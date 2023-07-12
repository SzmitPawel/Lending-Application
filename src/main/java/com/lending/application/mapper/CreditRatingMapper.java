package com.lending.application.mapper;

import com.lending.application.domain.CreditRating;
import com.lending.application.domain.dto.CreditRatingDto;
import org.springframework.stereotype.Component;

@Component
public class CreditRatingMapper {
    public CreditRatingDto mapToCreditRatingDto(final CreditRating creditRating) {
        return new CreditRatingDto(
                creditRating.getRatingId(),
                creditRating.getCreditRating(),
                creditRating.getDateOfRating()
        );
    }

    public CreditRating mapToCreditRating(final CreditRatingDto creditRatingDto) {
        CreditRating creditRating = new CreditRating();
        creditRating.setRatingId(creditRatingDto.getRatingId());
        creditRating.setCreditRating(creditRatingDto.getCreditRating());
        creditRating.setDateOfRating(creditRatingDto.getDateOfRating());

        return creditRating;
    }
}