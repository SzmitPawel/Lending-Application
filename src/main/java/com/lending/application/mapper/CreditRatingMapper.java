package com.lending.application.mapper;

import com.lending.application.domain.credit.rating.CreditRating;
import com.lending.application.domain.credit.rating.CreditRatingResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class CreditRatingMapper {
    public CreditRatingResponseDTO mapToCreditRatingDto(final CreditRating creditRating) {
        return new CreditRatingResponseDTO(
                creditRating.getRatingId(),
                creditRating.getCreditRating(),
                creditRating.getDateOfRating()
        );
    }

    public CreditRating mapToCreditRating(final CreditRatingResponseDTO creditRatingResponseDto) {
        CreditRating creditRating = new CreditRating();
        creditRating.setRatingId(creditRatingResponseDto.getRatingId());
        creditRating.setCreditRating(creditRatingResponseDto.getCreditRating());
        creditRating.setDateOfRating(creditRatingResponseDto.getDateOfRating());

        return creditRating;
    }
}