package com.lending.application.service.credit.rating;

import com.lending.application.domain.CreditRating;
import com.lending.application.domain.dto.CreditRatingDto;
import com.lending.application.exception.CreditRatingNotFoundException;
import com.lending.application.mapper.CreditRatingMapper;
import com.lending.application.repository.CreditRatingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CreditRatingService {
    CreditRatingRepository creditRatingRepository;
    CreditRatingMapper creditRatingMapper;

    public void createCreditRating(final CreditRatingDto creditRatingDto) {
        creditRatingRepository.saveAndFlush(creditRatingMapper.mapToCreditRating(creditRatingDto));
    }

    public CreditRatingDto getCreditRatingById(final Long creditRatingId) throws CreditRatingNotFoundException{
        CreditRating creditRating = creditRatingRepository
                .findById(creditRatingId)
                .orElseThrow(CreditRatingNotFoundException::new);

        return creditRatingMapper.mapToCreditRatingDto(creditRating);
    }

    public void updateCreditRating(final CreditRatingDto creditRatingDto) throws CreditRatingNotFoundException{
        CreditRating creditRating = creditRatingRepository
                .findById(creditRatingDto.getRatingId())
                .orElseThrow(CreditRatingNotFoundException::new);

        creditRating.setCreditRating(creditRatingDto.getCreditRating());
        creditRating.setDateOfRating(creditRatingDto.getDateOfRating());

        creditRatingRepository.saveAndFlush(creditRating);
    }

    public void deleteCreditRatingById(final Long creditRatingId) throws CreditRatingNotFoundException {
        if (creditRatingRepository.findById(creditRatingId).isPresent()) {
            creditRatingRepository.deleteById(creditRatingId);
        } else {
            throw new CreditRatingNotFoundException();
        }
    }
}