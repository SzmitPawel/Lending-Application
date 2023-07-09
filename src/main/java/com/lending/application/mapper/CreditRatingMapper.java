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
        return new CreditRating(
                creditRatingDto.getRatingId(),
                creditRatingDto.getCreditRating(),
                creditRatingDto.getDateOfRating()
        );
    }
}
