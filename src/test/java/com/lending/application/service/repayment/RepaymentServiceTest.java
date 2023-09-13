package com.lending.application.service.repayment;

import com.lending.application.domain.Repayment;
import com.lending.application.exception.RepaymentNotFoundException;
import com.lending.application.repository.RepaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RepaymentServiceTest {
    @InjectMocks
    private RepaymentService repaymentService;
    @Mock
    private RepaymentRepository repaymentRepository;


    @Test
    void testSaveRepayment() {
        // given
        Repayment repayment = new Repayment();

        when(repaymentRepository.saveAndFlush(any(Repayment.class))).thenReturn(repayment);

        // when
        Repayment retrievedRepayment = repaymentService.saveRepayment(repayment);

        // then
        verify(repaymentRepository,times(1)).saveAndFlush(any());

        assertNotNull(retrievedRepayment);
    }

    @Test
    void testDeleteRepaymentById_shouldThrowRepaymentNotFoundException() {
        // given
        when(repaymentRepository.findById(any())).thenReturn(Optional.empty());

        // when & then
        assertThrows(RepaymentNotFoundException.class, () -> repaymentService.deleteRepaymentById(1L));
    }

    @Test
    void testGetRepaymentById() throws RepaymentNotFoundException {
        // given
        Repayment repayment = new Repayment();

        when(repaymentRepository.findById(any())).thenReturn(Optional.of(repayment));

        // when
        Repayment retrievedRepayment = repaymentService.getRepaymentById(1L);

        // then
        verify(repaymentRepository, times(1)).findById(any());

        assertNotNull(retrievedRepayment);
    }

    @Test
    void testGetAllRepayments_shouldReturnRepaymentDtoList() {
        // given
        Repayment repayment1 = new Repayment();
        Repayment repayment2 = new Repayment();

        List<Repayment> repaymentList = List.of(repayment1,repayment2);

        when(repaymentRepository.findAll()).thenReturn(repaymentList);

        // when
        List<Repayment> retrievedRepaymentList = repaymentService.getAllRepayments();

        // then
        verify(repaymentRepository, times(1)).findAll();

        assertNotNull(retrievedRepaymentList);
    }

    @Test
    void testGetRepaymentById_shouldThrowRepaymentNotFoundException() {
        // given
        when(repaymentRepository.findById(any())).thenReturn(Optional.empty());

        // when & then
        assertThrows(RepaymentNotFoundException.class, () -> repaymentService.getRepaymentById(1L));
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