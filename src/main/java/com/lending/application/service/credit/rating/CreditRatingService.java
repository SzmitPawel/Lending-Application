package com.lending.application.service.credit.rating;

import com.lending.application.domain.CreditRating;
import com.lending.application.exception.CreditRatingNotFoundException;
import com.lending.application.repository.CreditRatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreditRatingService {
    private final CreditRatingRepository creditRatingRepository;

    public CreditRating saveCreditRating(final CreditRating creditRating) {
        return creditRatingRepository.saveAndFlush(creditRating);
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
}