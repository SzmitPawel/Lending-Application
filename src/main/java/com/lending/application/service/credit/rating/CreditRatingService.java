package com.lending.application.service.credit.rating;

import com.lending.application.domain.CreditRating;
import com.lending.application.domain.dto.CreditRatingDto;
import com.lending.application.exception.CreditRatingNotFoundException;
import com.lending.application.mapper.CreditRatingMapper;
import com.lending.application.repository.CreditRatingRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreditRatingService {
    private final CreditRatingRepository creditRatingRepository;
    private final CreditRatingMapper creditRatingMapper;

    public CreditRatingDto createCreditRating(final CreditRatingDto creditRatingDto) {
        CreditRating creditRating = creditRatingMapper.mapToCreditRating(creditRatingDto);
        CreditRating createdCreditRating = creditRatingRepository.saveAndFlush(creditRating);

        return creditRatingMapper.mapToCreditRatingDto(createdCreditRating);
    }

    public CreditRatingDto getCreditRatingById(final Long creditRatingId) throws CreditRatingNotFoundException{
        CreditRating creditRating = creditRatingRepository
                .findById(creditRatingId)
                .orElseThrow(CreditRatingNotFoundException::new);

        return creditRatingMapper.mapToCreditRatingDto(creditRating);
    }

    public CreditRatingDto updateCreditRating(final CreditRatingDto creditRatingDto) throws CreditRatingNotFoundException{
        CreditRating retrievedCreditRating = creditRatingRepository
                .findById(creditRatingDto.getRatingId())
                .orElseThrow(CreditRatingNotFoundException::new);

        retrievedCreditRating.setCreditRating(creditRatingDto.getCreditRating());
        retrievedCreditRating.setDateOfRating(creditRatingDto.getDateOfRating());

        CreditRating updatedCreditRating = creditRatingRepository.saveAndFlush(retrievedCreditRating);

        return creditRatingMapper.mapToCreditRatingDto(updatedCreditRating);
    }

    public void deleteCreditRatingById(final Long creditRatingId) throws CreditRatingNotFoundException {
        if (creditRatingRepository.findById(creditRatingId).isPresent()) {
            creditRatingRepository.deleteById(creditRatingId);
        } else {
            throw new CreditRatingNotFoundException();
        }
    }
}