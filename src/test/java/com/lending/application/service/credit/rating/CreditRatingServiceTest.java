package com.lending.application.service.credit.rating;

import com.lending.application.domain.credit.rating.CreditRating;
import com.lending.application.domain.credit.rating.CreditRatingEnum;
import com.lending.application.exception.CreditRatingNotFoundException;
import com.lending.application.repository.CreditRatingRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CreditRatingServiceTest {
    @Autowired
    private CreditRatingService creditRatingService;
    @Autowired
    private CreditRatingRepository creditRatingRepository;

    private CreditRating prepareCreditRating() {
        CreditRating creditRating = new CreditRating();
        creditRating.setCreditRating(CreditRatingEnum.ONE);
        creditRating.setDateOfRating(LocalDate.now());

        return creditRating;
    }

    @Test
    void save_credit_rating_should_save_and_return_credit_rating() {
        // given
        CreditRating creditRating = prepareCreditRating();

        // when
        CreditRating retrievedCreditRating = creditRatingService.saveCreditRating(creditRating);

        // then;
        assertNotNull(retrievedCreditRating);
        assertEquals(creditRating.getCreditRating(), retrievedCreditRating.getCreditRating());
        assertEquals(creditRating.getDateOfRating(), retrievedCreditRating.getDateOfRating());
    }

    @Test
    void get_credit_rating_by_id_should_throw_exception_credit_rating_not_found_if_rating_not_found() {
        // given
        Long creditRatingId = 999L;

        // when & then
        assertThrows(CreditRatingNotFoundException.class,
                () -> creditRatingService.getCreditRatingById(creditRatingId));
    }

    @Test
    void get_credit_rating_by_id_should_return_credit_rating_if_found()
            throws CreditRatingNotFoundException {
        // given
        CreditRating creditRating = creditRatingRepository.saveAndFlush(prepareCreditRating());

        // when
        CreditRating retrievedCreditRating = creditRatingService.getCreditRatingById(creditRating.getRatingId());

        // then
        assertNotNull(retrievedCreditRating);
        assertEquals(creditRating.getCreditRating(), retrievedCreditRating.getCreditRating());
        assertEquals(creditRating.getDateOfRating(), retrievedCreditRating.getDateOfRating());
    }

    @Test
    void delete_credit_rating_by_id_should_throw_exception_credit_rating_not_found_if_rating_not_found() {
        // given
        Long creditRatingId = 999L;

        // when & then
        assertThrows(CreditRatingNotFoundException.class,
                () -> creditRatingService.deleteCreditRatingById(creditRatingId));
    }

    @Test
    void delete_credit_rating_by_id_should_delete_credit_rating_if_found()
            throws CreditRatingNotFoundException {
        // given
        CreditRating creditRating = creditRatingRepository.saveAndFlush(prepareCreditRating());

        // when
        creditRatingService.deleteCreditRatingById(creditRating.getRatingId());

        // then
        assertThrows(CreditRatingNotFoundException.class,
                () -> creditRatingService.getCreditRatingById(creditRating.getRatingId()));
    }
}