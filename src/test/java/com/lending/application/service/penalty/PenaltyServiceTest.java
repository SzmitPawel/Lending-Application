package com.lending.application.service.penalty;

import com.lending.application.domain.Penalty;
import com.lending.application.exception.PenaltyNotFoundException;
import com.lending.application.repository.PenaltyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PenaltyServiceTest {
    @InjectMocks
    private PenaltyService penaltyService;
    @Mock
    private PenaltyRepository penaltyRepository;

    @Test
    void testSavePenalty() {
        // given
        Penalty penalty = new Penalty();

        when(penaltyRepository.saveAndFlush(any(Penalty.class))).thenReturn(penalty);

        // when
        Penalty retrievedPenalty = penaltyService.savePenalty(penalty);

        // then
        verify(penaltyRepository,times(1)).saveAndFlush(any(Penalty.class));

        assertNotNull(retrievedPenalty);
    }

    @Test
    void testDeletePenaltyById_shouldThrowPenaltyNotFoundException() {
        // given
        when(penaltyRepository.findById(any())).thenReturn(Optional.empty());

        // when & then
        assertThrows(PenaltyNotFoundException.class, () -> penaltyService.deletePenaltyById(1L));
    }

    @Test
    void testGetPenaltyById() throws PenaltyNotFoundException {
        // given
        Penalty penalty = new Penalty();

        when(penaltyRepository.findById(any())).thenReturn(Optional.of(penalty));

        // when
        Penalty retrievedPenalty = penaltyService.getPenaltyById(1L);

        // then
        verify(penaltyRepository,times(1)).findById(any());

        assertNotNull(retrievedPenalty);
    }

    @Test
    void testGetPenaltyById_shouldThrowPenaltyNotFoundException() {
        // given
        when(penaltyRepository.findById(any())).thenReturn(Optional.empty());

        // when & then
        assertThrows(PenaltyNotFoundException.class, () -> penaltyService.getPenaltyById(1L));
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