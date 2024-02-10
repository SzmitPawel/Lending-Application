package com.lending.application.mapper.credit.rating;

import com.lending.application.domain.credit.rating.CreditRating;
import com.lending.application.domain.credit.rating.CreditRatingResponseDTO;
import org.mapstruct.Mapper;

@Mapper(implementationPackage = "rating")
public interface CreditRatingResponseMapper {

    CreditRatingResponseDTO mapToCreditRatingDTO(CreditRating creditRating);
}
