package com.lending.application.service.repayment;

import com.lending.application.domain.Repayment;
import com.lending.application.exception.RepaymentNotFoundException;
import com.lending.application.repository.RepaymentRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
class RepaymentServiceTest {
    @Autowired
    private RepaymentService repaymentService;
    @Autowired
    private RepaymentRepository repaymentRepository;

    private Repayment prepareRepayment() {
        Repayment repayment = new Repayment();
        repayment.setRepaymentAmount(BigDecimal.valueOf(100.00));
        repayment.setRepaymentDate(LocalDate.now());

        return repayment;
    }

    @Test
    void testSaveRepayment() {
        // given
        Repayment repayment = prepareRepayment();

        // when
        Repayment retrievedRepayment = repaymentService.saveRepayment(repayment);

        // then
        assertNotNull(retrievedRepayment);
        assertEquals(repayment.getRepaymentAmount(),retrievedRepayment.getRepaymentAmount());
        assertEquals(repayment.getRepaymentDate(),retrievedRepayment.getRepaymentDate());
    }

    @Test
    void testDeleteRepaymentById_shouldThrowRepaymentNotFoundException() {
        // given
        Long repaymentId = 999L;

        // when & then
        assertThrows(RepaymentNotFoundException.class, () -> repaymentService.deleteRepaymentById(repaymentId));
    }

    @Test
    void testGetRepaymentById() throws RepaymentNotFoundException {
        // given
        Repayment repayment = repaymentRepository.saveAndFlush(prepareRepayment());

        // when
        Repayment retrievedRepayment = repaymentService.getRepaymentById(repayment.getRepaymentId());

        // then
        assertNotNull(retrievedRepayment);
        assertEquals(repayment.getRepaymentAmount(),retrievedRepayment.getRepaymentAmount());
        assertEquals(repayment.getRepaymentDate(),retrievedRepayment.getRepaymentDate());
    }

    @Test
    void testGetAllRepayments_shouldReturnRepaymentList() {
        // given
        Repayment repayment1 = prepareRepayment();
        Repayment repayment2 = prepareRepayment();
        Repayment repayment3 = prepareRepayment();

        repaymentRepository.saveAndFlush(repayment1);
        repaymentRepository.saveAndFlush(repayment2);
        repaymentRepository.saveAndFlush(repayment3);

        List<Repayment> expectedRepaymentsRepaymentList = List.of(repayment1,repayment2,repayment3);

        // when
        List<Repayment> retrievedRepaymentList = repaymentService.getAllRepayments();

        // then
        assertNotNull(retrievedRepaymentList);
        assertEquals(expectedRepaymentsRepaymentList.size(),retrievedRepaymentList.size());
        assertEquals(expectedRepaymentsRepaymentList.get(0).getRepaymentDate(),retrievedRepaymentList.get(0).getRepaymentDate());
        assertEquals(expectedRepaymentsRepaymentList.get(0).getRepaymentAmount(),retrievedRepaymentList.get(0).getRepaymentAmount());

        assertEquals(expectedRepaymentsRepaymentList.get(1).getRepaymentDate(),retrievedRepaymentList.get(1).getRepaymentDate());
        assertEquals(expectedRepaymentsRepaymentList.get(1).getRepaymentAmount(),retrievedRepaymentList.get(1).getRepaymentAmount());

        assertEquals(expectedRepaymentsRepaymentList.get(2).getRepaymentDate(),retrievedRepaymentList.get(2).getRepaymentDate());
        assertEquals(expectedRepaymentsRepaymentList.get(2).getRepaymentAmount(),retrievedRepaymentList.get(2).getRepaymentAmount());
    }

    @Test
    void testGetRepaymentById_shouldThrowRepaymentNotFoundException() {
        // given
        Long repaymentId = 999L;

        // when & then
        assertThrows(RepaymentNotFoundException.class, () -> repaymentService.getRepaymentById(repaymentId));
    }

    @Test
    void testDeleteRepaymentById() throws RepaymentNotFoundException {
        // given
        Repayment repayment = repaymentRepository.saveAndFlush(prepareRepayment());

        // when
        repaymentService.deleteRepaymentById(repayment.getRepaymentId());

        // then
        assertThrows(RepaymentNotFoundException.class,
                () -> repaymentService.getRepaymentById(repayment.getRepaymentId()));
    }
}