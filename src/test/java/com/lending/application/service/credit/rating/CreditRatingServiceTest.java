package com.lending.application.service.credit.rating;

import com.lending.application.domain.CreditRating;
import com.lending.application.domain.CreditRatingEnum;
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
    void testSaveCreditRating() {
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
    void testGetCreditRatingById_CreditRatingNotFoundException() {
        // given
        Long creditRatingId = 999L;

        // when & then
        assertThrows(CreditRatingNotFoundException.class,
                () -> creditRatingService.getCreditRatingById(creditRatingId));
    }

    @Test
    void testGetCreditRatingById() throws CreditRatingNotFoundException {
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
    void testDeleteCreditRatingById_CreditRatingNotFoundException() {
        // given
        Long creditRatingId = 999L;

        // when & then
        assertThrows(CreditRatingNotFoundException.class,
                () -> creditRatingService.deleteCreditRatingById(creditRatingId));
    }

    @Test
    void deleteCreditRatingById() throws CreditRatingNotFoundException {
        // given
        CreditRating creditRating = creditRatingRepository.saveAndFlush(prepareCreditRating());

        // when
        creditRatingService.deleteCreditRatingById(creditRating.getRatingId());

        // then
        assertThrows(CreditRatingNotFoundException.class,
                () -> creditRatingService.getCreditRatingById(creditRating.getRatingId()));
    }

    @Test
    void updateCreditRatingById_shouldThrowCreditRatingNotFoundException() {
        // given
        CreditRating creditRating = prepareCreditRating();
        creditRating.setRatingId(999L);

        // when & then
        assertThrows(CreditRatingNotFoundException.class,
                () -> creditRatingService.updateCreditRatingById(creditRating));
    }

    @Test
    void updateCreditRatingById_shouldReturnUpdatedCreditRating() throws CreditRatingNotFoundException {
        // given
        CreditRating creditRating = creditRatingRepository.saveAndFlush(prepareCreditRating());

        // when
        creditRating.setCreditRating(CreditRatingEnum.FIVE);
        CreditRating retrievedCreditRating = creditRatingService.updateCreditRatingById(creditRating);

        // then
        assertNotNull(retrievedCreditRating);
        assertEquals(creditRating.getCreditRating(), retrievedCreditRating.getCreditRating());
        assertEquals(creditRating.getDateOfRating(), retrievedCreditRating.getDateOfRating());
    }
}