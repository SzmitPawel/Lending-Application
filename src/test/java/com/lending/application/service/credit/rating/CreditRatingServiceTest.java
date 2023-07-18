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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditRatingServiceTest {
    @InjectMocks
    CreditRatingService creditRatingService;
    @Mock
    CreditRatingMapper creditRatingMapper;
    @Mock
    CreditRatingRepository creditRatingRepository;

    @Test
    void testGetCreditRatingById_CreditRatingNotFoundException() {
        // given
        when(creditRatingRepository.findById(any())).thenReturn(Optional.empty());

        // when & then
        assertThrows(CreditRatingNotFoundException.class, () -> creditRatingService
                .getCreditRatingById(1L));
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
    void testCreateCreditRating() {
        // given
        CreditRatingDto creditRatingDto = new CreditRatingDto(
                1L,
                CreditRatingEnum.ONE,
                LocalDate.now()
        );

        CreditRating creditRating = new CreditRating();

        when(creditRatingMapper.mapToCreditRating(creditRatingDto)).thenReturn(creditRating);

        // when
        creditRatingService.createCreditRating(creditRatingDto);

        // then
        verify(creditRatingRepository, times(1)).saveAndFlush(creditRating);
    }

    @Test
    void testGetCreditRatingById() throws CreditRatingNotFoundException {
        // given
        CreditRating creditRating = new CreditRating();
        creditRating.setRatingId(1L);
        creditRating.setCreditRating(CreditRatingEnum.ONE);
        creditRating.setDateOfRating(LocalDate.now());

        when(creditRatingRepository.findById(any())).thenReturn(Optional.of(creditRating));
        when(creditRatingMapper.mapToCreditRatingDto(any())).thenCallRealMethod();

        // when
        CreditRatingDto retrievedCreditRatingDto = creditRatingService.getCreditRatingById(1L);

        // then
        verify(creditRatingRepository, times(1)).findById(1L);
        verify(creditRatingMapper, times(1)).mapToCreditRatingDto(creditRating);
        assertEquals(1L, retrievedCreditRatingDto.getRatingId());
        assertEquals(CreditRatingEnum.ONE, retrievedCreditRatingDto.getCreditRating());
        assertEquals(LocalDate.now(), retrievedCreditRatingDto.getDateOfRating());
    }

    @Test
    void testUpdateCreditRating() throws CreditRatingNotFoundException {
        // given
        CreditRatingDto creditRatingDto = new CreditRatingDto(
                1L,
                CreditRatingEnum.ONE,
                LocalDate.now()
        );

        CreditRating creditRating = new CreditRating();

        when(creditRatingRepository.findById(any())).thenReturn(Optional.of(creditRating));

        // when
        creditRatingService.updateCreditRating(creditRatingDto);

        // then
        verify(creditRatingRepository, times(1)).findById(1L);
        verify(creditRatingRepository, times(1)).saveAndFlush(creditRating);
        assertEquals(CreditRatingEnum.ONE, creditRating.getCreditRating());
        assertEquals(LocalDate.now(), creditRating.getDateOfRating());
    }

    @Test
    void deleteCreditRatingById() throws CreditRatingNotFoundException {
        // given
        CreditRating creditRating = new CreditRating();

        when(creditRatingRepository.findById(any())).thenReturn(Optional.of(creditRating));

        // when
        creditRatingService.deleteCreditRatingById(1L);

        // then
        verify(creditRatingRepository, times(1)).deleteById(1L);
    }
}