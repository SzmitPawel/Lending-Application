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
    private PenaltyService penaltyService;
    @Mock
    private PenaltyMapper penaltyMapper;
    @Mock
    private PenaltyRepository penaltyRepository;

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
        PenaltyDto penaltyDto = new PenaltyDto();
        Penalty penalty = new Penalty();

        when(penaltyMapper.mapToPenalty(penaltyDto)).thenReturn(penalty);
        when(penaltyMapper.mapToPenaltyDto(penalty)).thenReturn(penaltyDto);
        when(penaltyRepository.saveAndFlush(penalty)).thenReturn(penalty);

        // when
        PenaltyDto retrievedPenaltyDto = penaltyService.createPenalty(penaltyDto);

        // then
        verify(penaltyRepository,times(1)).saveAndFlush(any(Penalty.class));
        verify(penaltyMapper,times(1)).mapToPenalty(any(PenaltyDto.class));
        verify(penaltyMapper,times(1)).mapToPenaltyDto(any(Penalty.class));

        assertNotNull(retrievedPenaltyDto);
    }

    @Test
    void testGetPenaltyById() throws PenaltyNotFoundException {
        // given
        Penalty penalty = new Penalty();
        PenaltyDto penaltyDto = new PenaltyDto();

        when(penaltyRepository.findById(1L)).thenReturn(Optional.of(penalty));
        when(penaltyMapper.mapToPenaltyDto(penalty)).thenReturn(penaltyDto);

        // when
        PenaltyDto retrievedPenaltyDto = penaltyService.getPenaltyById(1L);

        // then
        verify(penaltyRepository,times(1)).findById(any());
        verify(penaltyMapper,times(1)).mapToPenaltyDto(any(Penalty.class));

        assertNotNull(retrievedPenaltyDto);
    }

    @Test
    void testUpdatePenalty() throws PenaltyNotFoundException {
        // given
        PenaltyDto penaltyDto = new PenaltyDto();
        penaltyDto.setPenaltyId(1L);
        Penalty penalty = new Penalty();

        when(penaltyRepository.findById(1L)).thenReturn(Optional.of(penalty));
        when(penaltyRepository.saveAndFlush(penalty)).thenReturn(penalty);
        when(penaltyMapper.mapToPenaltyDto(penalty)).thenReturn(penaltyDto);

        // when
        PenaltyDto retrievedPenaltyDto = penaltyService.updatePenalty(penaltyDto);

        // then
        verify(penaltyRepository, times(1)).findById(any());
        verify(penaltyRepository, times(1)).saveAndFlush(any(Penalty.class));
        verify(penaltyMapper,times(1)).mapToPenaltyDto(any(Penalty.class));

        assertNotNull(retrievedPenaltyDto);
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