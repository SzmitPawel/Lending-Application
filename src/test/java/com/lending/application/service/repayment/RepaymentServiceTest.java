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
    RepaymentService repaymentService;
    @Mock
    RepaymentMapper repaymentMapper;
    @Mock
    RepaymentRepository repaymentRepository;

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
        RepaymentDto repaymentDto = new RepaymentDto(
                1L,
                new BigDecimal(10),
                LocalDate.now()
        );

        Repayment repayment = new Repayment();

        when(repaymentMapper.mapToRepayment(repaymentDto)).thenReturn(repayment);

        // when
        repaymentService.createRepayment(repaymentDto);

        // then
        verify(repaymentRepository, times(1)).saveAndFlush(repayment);
    }

    @Test
    void testGetRepaymentById() throws RepaymentNotFoundException {
        // given
        Repayment repayment = new Repayment();
        repayment.setRepaymentAmount(new BigDecimal(10));
        repayment.setRepaymentDate(LocalDate.now());

        when(repaymentMapper.mapToRepaymentDto(repayment)).thenCallRealMethod();
        when(repaymentRepository.findById(any())).thenReturn(Optional.of(repayment));

        // when
        RepaymentDto retrievedRepaymentDto = repaymentService.getRepaymentById(1L);

        // then
        verify(repaymentMapper, times(1)).mapToRepaymentDto(repayment);
        verify(repaymentRepository, times(1)).findById(1L);
        assertEquals(new BigDecimal(10), retrievedRepaymentDto.getRepaymentAmount());
        assertEquals(LocalDate.now(), retrievedRepaymentDto.getRepaymentDate());
    }

    @Test
    void testGetAllRepayments_shouldReturnRepaymentDtoList() {
        // given
        Repayment repayment1 = new Repayment();
        repayment1.setRepaymentAmount(new BigDecimal(10));
        repayment1.setRepaymentDate(LocalDate.now());

        Repayment repayment2 = new Repayment();
        repayment2.setRepaymentAmount(new BigDecimal(20));
        repayment2.setRepaymentDate(LocalDate.now());

        List<Repayment> repaymentList = List.of(repayment1,repayment2);

        when(repaymentMapper.mapToRepaymentDtoList(repaymentList)).thenCallRealMethod();
        when(repaymentRepository.findAll()).thenReturn(repaymentList);

        // when
        List<RepaymentDto> retrievedRepaymentDtoList = repaymentService.getAllRepayments();

        // then
        verify(repaymentMapper, times(1)).mapToRepaymentDtoList(repaymentList);
        verify(repaymentRepository, times(1)).findAll();
        assertEquals(2, retrievedRepaymentDtoList.size());
    }

    @Test
    void testUpdateRepayment() throws RepaymentNotFoundException {
        // given
        RepaymentDto repaymentDto = new RepaymentDto(
                1L,
                new BigDecimal(10),
                LocalDate.now()
        );

        Repayment repayment = new Repayment();

        when(repaymentRepository.findById(any())).thenReturn(Optional.of(repayment));

        // when
        repaymentService.updateRepayment(repaymentDto);

        // then
        verify(repaymentRepository, times(1)).findById(1L);
        verify(repaymentRepository, times(1)).saveAndFlush(repayment);
        assertEquals(new BigDecimal(10), repayment.getRepaymentAmount());
        assertEquals(LocalDate.now(), repayment.getRepaymentDate());
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