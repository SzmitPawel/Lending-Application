package com.lending.application.service.credit.rating;

import com.lending.application.domain.CreditRating;
import com.lending.application.domain.CreditRatingEnum;
import com.lending.application.domain.dto.CreditRatingDto;
import com.lending.application.exception.CreditRatingNotFoundException;
import com.lending.application.mapper.CreditRatingMapper;
import com.lending.application.repository.CreditRatingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditRatingServiceTest {
    @InjectMocks
    private CreditRatingService creditRatingService;
    @Mock
    private CreditRatingRepository creditRatingRepository;

    @Test
    void testGetCreditRatingById_CreditRatingNotFoundException() {
        // given
        when(creditRatingRepository.findById(any())).thenReturn(Optional.empty());

        // when & then
        assertThrows(CreditRatingNotFoundException.class, () -> creditRatingService
                .getCreditRatingById(1L));
    }

    @Test
    void testSaveCreditRating() {
        // given
        CreditRating creditRating = new CreditRating();

        when(creditRatingRepository.saveAndFlush(any(CreditRating.class))).thenReturn(creditRating);

        // when
        CreditRating retrievedCreditRating = creditRatingService.saveCreditRating(creditRating);

        // then
        verify(creditRatingRepository, times(1)).saveAndFlush(any(CreditRating.class));

        assertNotNull(retrievedCreditRating);
    }

    @Test
    void testGetCreditRatingById() throws CreditRatingNotFoundException {
        // given
        CreditRating creditRating = new CreditRating();

        when(creditRatingRepository.findById(any())).thenReturn(Optional.of(creditRating));

        // when
        CreditRating retrievedCreditRating = creditRatingService.getCreditRatingById(1L);

        // then
        verify(creditRatingRepository, times(1)).findById(any());

        assertNotNull(retrievedCreditRating);
    }

    @Test
    void testDeleteCreditRatingById_CreditRatingNotFoundException() {
        // given
        when(creditRatingRepository.findById(any())).thenReturn(Optional.empty());

        // when & then
        assertThrows(CreditRatingNotFoundException.class, () -> creditRatingService
                .deleteCreditRatingById(1L));
    }

    @Test
    void deleteCreditRatingById() throws CreditRatingNotFoundException {
        // given
        CreditRating creditRating = new CreditRating();

        when(creditRatingRepository.findById(any())).thenReturn(Optional.of(creditRating));

        // when
        creditRatingService.deleteCreditRatingById(1L);

        // then
        verify(creditRatingRepository, times(1)).deleteById(any());
    }
}