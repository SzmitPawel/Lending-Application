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
    private CreditRatingMapper creditRatingMapper;
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
        CreditRatingDto creditRatingDto = new CreditRatingDto();
        CreditRating creditRating = new CreditRating();

        when(creditRatingMapper.mapToCreditRating(creditRatingDto)).thenReturn(creditRating);
        when(creditRatingRepository.saveAndFlush(creditRating)).thenReturn(creditRating);
        when(creditRatingMapper.mapToCreditRatingDto(creditRating)).thenReturn(creditRatingDto);

        // when
        CreditRatingDto retrievedCreditRatingDto = creditRatingService.createCreditRating(creditRatingDto);

        // then
        verify(creditRatingRepository, times(1)).saveAndFlush(any(CreditRating.class));
        verify(creditRatingMapper,times(1)).mapToCreditRating(any(CreditRatingDto.class));
        verify(creditRatingMapper,times(1)).mapToCreditRatingDto(any(CreditRating.class));

        assertNotNull(retrievedCreditRatingDto);
    }

    @Test
    void testGetCreditRatingById() throws CreditRatingNotFoundException {
        // given
        CreditRating creditRating = new CreditRating();
        CreditRatingDto creditRatingDto = new CreditRatingDto();

        when(creditRatingRepository.findById(1L)).thenReturn(Optional.of(creditRating));
        when(creditRatingMapper.mapToCreditRatingDto(creditRating)).thenReturn(creditRatingDto);

        // when
        CreditRatingDto retrievedCreditRatingDto = creditRatingService.getCreditRatingById(1L);

        // then
        verify(creditRatingRepository, times(1)).findById(any());
        verify(creditRatingMapper, times(1)).mapToCreditRatingDto(any(CreditRating.class));

        assertNotNull(retrievedCreditRatingDto);
    }

    @Test
    void testUpdateCreditRating() throws CreditRatingNotFoundException {
        // given
        CreditRatingDto creditRatingDto = new CreditRatingDto();
        creditRatingDto.setRatingId(1L);
        CreditRating creditRating = new CreditRating();

        when(creditRatingRepository.findById(1L)).thenReturn(Optional.of(creditRating));
        when(creditRatingRepository.saveAndFlush(creditRating)).thenReturn(creditRating);
        when(creditRatingMapper.mapToCreditRatingDto(creditRating)).thenReturn(creditRatingDto);

        // when
        CreditRatingDto updateCreditRatingDto = creditRatingService.updateCreditRating(creditRatingDto);

        // then
        verify(creditRatingRepository, times(1)).findById(any());
        verify(creditRatingRepository, times(1)).saveAndFlush(any(CreditRating.class));
        verify(creditRatingMapper,times(1)).mapToCreditRatingDto(any(CreditRating.class));

        assertNotNull(updateCreditRatingDto);
    }

    @Test
    void deleteCreditRatingById() throws CreditRatingNotFoundException {
        // given
        CreditRating creditRating = new CreditRating();

        when(creditRatingRepository.findById(1L)).thenReturn(Optional.of(creditRating));

        // when
        creditRatingService.deleteCreditRatingById(1L);

        // then
        verify(creditRatingRepository, times(1)).deleteById(any());
    }
}