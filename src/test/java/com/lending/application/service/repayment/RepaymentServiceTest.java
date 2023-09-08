package com.lending.application.service.repayment;

import com.lending.application.domain.Repayment;
import com.lending.application.domain.dto.RepaymentDto;
import com.lending.application.exception.RepaymentNotFoundException;
import com.lending.application.mapper.RepaymentMapper;
import com.lending.application.repository.RepaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RepaymentServiceTest {
    @InjectMocks
    private RepaymentService repaymentService;
    @Mock
    private RepaymentMapper repaymentMapper;
    @Mock
    private RepaymentRepository repaymentRepository;

    @Test
    void testGetRepaymentById_shouldThrowRepaymentNotFoundException() {
        // given
        when(repaymentRepository.findById(any())).thenReturn(Optional.empty());

        // when & then
        assertThrows(RepaymentNotFoundException.class, () -> repaymentService.getRepaymentById(1L));
    }

    @Test
    void testDeleteRepaymentById_shouldThrowRepaymentNotFoundException() {
        // given
        when(repaymentRepository.findById(any())).thenReturn(Optional.empty());

        // when & then
        assertThrows(RepaymentNotFoundException.class, () -> repaymentService.deleteRepaymentById(1L));
    }

    @Test
    void testCreateRepayment() {
        // given
        RepaymentDto repaymentDto = new RepaymentDto();
        Repayment repayment = new Repayment();

        when(repaymentMapper.mapToRepayment(repaymentDto)).thenReturn(repayment);
        when(repaymentRepository.saveAndFlush(repayment)).thenReturn(repayment);
        when(repaymentMapper.mapToRepaymentDto(repayment)).thenReturn(repaymentDto);

        // when
        RepaymentDto retrievedRepaymentDto = repaymentService.createRepayment(repaymentDto);

        // then
        verify(repaymentMapper,times(1)).mapToRepayment(any(RepaymentDto.class));
        verify(repaymentRepository,times(1)).saveAndFlush(any());
        verify(repaymentMapper,times(1)).mapToRepaymentDto(any(Repayment.class));

        assertNotNull(retrievedRepaymentDto);
    }

    @Test
    void testGetRepaymentById() throws RepaymentNotFoundException {
        // given
        Repayment repayment = new Repayment();
        RepaymentDto repaymentDto = new RepaymentDto();

        when(repaymentMapper.mapToRepaymentDto(repayment)).thenReturn(repaymentDto);
        when(repaymentRepository.findById(any())).thenReturn(Optional.of(repayment));

        // when
        RepaymentDto retrievedRepaymentDto = repaymentService.getRepaymentById(1L);

        // then
        verify(repaymentMapper,times(1)).mapToRepaymentDto(any(Repayment.class));
        verify(repaymentRepository, times(1)).findById(any());

        assertNotNull(retrievedRepaymentDto);
    }

    @Test
    void testGetAllRepayments_shouldReturnRepaymentDtoList() {
        // given
        Repayment repayment1 = new Repayment();
        Repayment repayment2 = new Repayment();

        List<Repayment> repaymentList = List.of(repayment1,repayment2);

        RepaymentDto repaymentDto1 = new RepaymentDto();
        RepaymentDto repaymentDto2 = new RepaymentDto();

        List<RepaymentDto> repaymentDtoList = List.of(repaymentDto1,repaymentDto2);

        when(repaymentMapper.mapToRepaymentDtoList(repaymentList)).thenReturn(repaymentDtoList);
        when(repaymentRepository.findAll()).thenReturn(repaymentList);

        // when
        List<RepaymentDto> retrievedRepaymentDtoList = repaymentService.getAllRepayments();

        // then
        verify(repaymentMapper, times(1)).mapToRepaymentDtoList(anyList());
        verify(repaymentRepository, times(1)).findAll();

        assertNotNull(retrievedRepaymentDtoList);
    }

    @Test
    void testUpdateRepayment() throws RepaymentNotFoundException {
        // given
        RepaymentDto repaymentDto = new RepaymentDto();
        repaymentDto.setRepaymentId(1L);
        Repayment repayment = new Repayment();

        when(repaymentRepository.findById(1L)).thenReturn(Optional.of(repayment));
        when(repaymentRepository.saveAndFlush(repayment)).thenReturn(repayment);
        when(repaymentMapper.mapToRepaymentDto(repayment)).thenReturn(repaymentDto);

        // when
        RepaymentDto retrievedRepaymentDto = repaymentService.updateRepayment(repaymentDto);

        // then
        verify(repaymentRepository, times(1)).findById(any());
        verify(repaymentRepository, times(1)).saveAndFlush(any(Repayment.class));

        assertNotNull(retrievedRepaymentDto);
    }

    @Test
    void testDeleteRepaymentById() throws RepaymentNotFoundException {
        // given
        Repayment repayment = new Repayment();

        when(repaymentRepository.findById(any())).thenReturn(Optional.of(repayment));

        // when
        repaymentService.deleteRepaymentById(1L);

        // then
        verify(repaymentRepository, times(1)).findById(1L);
        verify(repaymentRepository, times(1)).deleteById(1L);
    }
}