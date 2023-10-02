package com.lending.application.service.credit.rating;

import com.lending.application.domain.CreditRating;
import com.lending.application.domain.dto.CreditRatingDto;
import com.lending.application.exception.CreditRatingNotFoundException;
import com.lending.application.repository.CreditRatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreditRatingService {
    private final CreditRatingRepository creditRatingRepository;

    public CreditRating saveCreditRating(final CreditRating creditRating) {
        CreditRating retrievedCreditRating = creditRatingRepository.saveAndFlush(creditRating);

        return retrievedCreditRating;
    }

    public CreditRating getCreditRatingById(final Long creditRatingId) throws CreditRatingNotFoundException {
        return creditRatingRepository.findById(creditRatingId).orElseThrow(CreditRatingNotFoundException::new);
    }

    public void deleteCreditRatingById(final Long creditRatingId) throws CreditRatingNotFoundException {
        if (creditRatingRepository.findById(creditRatingId).isPresent()) {
            creditRatingRepository.deleteById(creditRatingId);
        } else {
            throw new CreditRatingNotFoundException();
        }
    }

    public CreditRating updateCreditRatingById(final CreditRating creditRating) throws CreditRatingNotFoundException {
        CreditRating retrievedCreditRating = creditRatingRepository
                .findById(creditRating.getRatingId()).orElseThrow(CreditRatingNotFoundException::new);

        retrievedCreditRating.setCreditRating(creditRating.getCreditRating());
        retrievedCreditRating.setDateOfRating(creditRating.getDateOfRating());
        creditRatingRepository.saveAndFlush(retrievedCreditRating);

        return  retrievedCreditRating;
    }
}