package com.lending.application.service.penalty;

import com.lending.application.domain.Penalty;
import com.lending.application.domain.dto.PenaltyDto;
import com.lending.application.exception.PenaltyNotFoundException;
import com.lending.application.mapper.PenaltyMapper;
import com.lending.application.repository.PenaltyRepository;
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
class PenaltyServiceTest {
    @InjectMocks
    PenaltyService penaltyService;
    @Mock
    PenaltyMapper penaltyMapper;
    @Mock
    PenaltyRepository penaltyRepository;

    @Test
    void testGetPenaltyById_shouldThrowPenaltyNotFoundException() {
        // given
        when(penaltyRepository.findById(any())).thenReturn(Optional.empty());

        // when & then
        assertThrows(PenaltyNotFoundException.class, () -> penaltyService.getPenaltyById(1L));
    }

    @Test
    void testDeletePenaltyById_shouldThrowPenaltyNotFoundException() {
        // given
        when(penaltyRepository.findById(any())).thenReturn(Optional.empty());

        // when & then
        assertThrows(PenaltyNotFoundException.class, () -> penaltyService.deletePenaltyById(1L));
    }

    @Test
    void testCreatePenalty() {
        // given
        PenaltyDto penaltyDto = new PenaltyDto(
                1L,
                5,
                LocalDate.now()
        );

        Penalty penalty = new Penalty();

        when(penaltyMapper.mapToPenalty(penaltyDto)).thenReturn(penalty);

        // when
        penaltyService.createPenalty(penaltyDto);

        // then
        verify(penaltyRepository, times(1)).saveAndFlush(penalty);
    }

    @Test
    void testGetPenaltyById() throws PenaltyNotFoundException {
        // given
        Penalty penalty = new Penalty();
        penalty.setPenaltyPercentage(5);
        penalty.setPenaltyDate(LocalDate.now());

        when(penaltyRepository.findById(any())).thenReturn(Optional.of(penalty));
        when(penaltyMapper.mapToPenaltyDto(penalty)).thenCallRealMethod();

        // when
        PenaltyDto retrievedPenaltyDto = penaltyService.getPenaltyById(1L);

        // then
        verify(penaltyRepository, times(1)).findById(1L);
        verify(penaltyMapper, times(1)).mapToPenaltyDto(penalty);
        assertEquals(5, retrievedPenaltyDto.getPenaltyPercentage());
        assertEquals(LocalDate.now(), retrievedPenaltyDto.getPenaltyDate());
    }

    @Test
    void testUpdatePenalty() throws PenaltyNotFoundException {
        // given
        PenaltyDto penaltyDto = new PenaltyDto(
                1L,
                5,
                LocalDate.now()
        );

        Penalty penalty = new Penalty();

        when(penaltyRepository.findById(any())).thenReturn(Optional.of(penalty));

        // when
        penaltyService.updatePenalty(penaltyDto);

        // then
        verify(penaltyRepository, times(1)).findById(1L);
        verify(penaltyRepository, times(1)).saveAndFlush(penalty);
        assertEquals(5, penalty.getPenaltyPercentage());
        assertEquals(LocalDate.now(), penalty.getPenaltyDate());
    }

    @Test
    void testDeletePenaltyById() throws PenaltyNotFoundException {
        // given
        Penalty penalty = new Penalty();

        when(penaltyRepository.findById(any())).thenReturn(Optional.of(penalty));

        // when
        penaltyService.deletePenaltyById(1L);

        // then
        verify(penaltyRepository, times(1)).deleteById(1L);
    }
}