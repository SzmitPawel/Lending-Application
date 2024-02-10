package com.lending.application.mapper.credit.rating;

import com.lending.application.domain.credit.rating.CreditRating;
import com.lending.application.domain.credit.rating.CreditRatingRequestDTO;
import org.mapstruct.*;

@Mapper(
        implementationPackage = "rating",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CreditRatingRequestMapper {
    @Mapping(source = "creditRatingEnum", target = "creditRating")
    CreditRating mapToCreditRating(CreditRatingRequestDTO creditRatingRequestDTO);
}
