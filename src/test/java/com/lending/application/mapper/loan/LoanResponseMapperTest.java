package com.lending.application.mapper.loan;

import com.lending.application.domain.loan.Loan;
import com.lending.application.domain.loan.LoanResponseDTO;
import loan.LoanResponseMapperImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {LoanResponseMapperImpl.class})
class LoanResponseMapperTest {
    @Autowired
    LoanResponseMapper responseMapper;

    private Loan prepareLoan() {
        return new Loan(
                BigDecimal.valueOf(1000),
                BigDecimal.valueOf(200),
                22.00F,
                LocalDate.now(),
                5);
    }

    private List<Loan> prepareLoanList() {
        Loan loan1 = new Loan(
                BigDecimal.valueOf(1000),
                BigDecimal.valueOf(200),
                22.00F,
                LocalDate.now(),
                5
        );
        loan1.setLoanId(1L);

        Loan loan2 = new Loan(
                BigDecimal.valueOf(2000),
                BigDecimal.valueOf(500),
                33.00F,
                LocalDate.now(),
                6
        );
        loan2.setLoanId(2L);

        return List.of(loan1, loan2);
    }

    @Test
    void map_to_loan_dto_should_return_loan_response_dto() {
        // given
        Loan loan = prepareLoan();
        loan.setLoanId(1L);

        // when
        LoanResponseDTO retrievedLoanResponseDTO = responseMapper.mapToLoanDTO(loan);

        // then
        assertNotNull(retrievedLoanResponseDTO);
        assertEquals(loan.getLoanAmount(), retrievedLoanResponseDTO.getLoanAmount());
        assertEquals(loan.getMonthlyPayment(), retrievedLoanResponseDTO.getMonthlyPayment());
        assertEquals(loan.getInterest(), retrievedLoanResponseDTO.getInterest());
        assertEquals(loan.getLoanStartDate(), retrievedLoanResponseDTO.getLoanStartDate());
        assertEquals(loan.getRepaymentPeriod(), retrievedLoanResponseDTO.getRepaymentPeriod());
    }

    @Test
    void map_to_loan_dto_list_should_return_list_of_loan_response_dto() {
        // given
        List<Loan> loanList = prepareLoanList();

        // when
        List<LoanResponseDTO> retrievedLoanResponseDTOList = responseMapper.mapToListLoanDTO(loanList);

        // then
        assertNotNull(retrievedLoanResponseDTOList);
        assertEquals(2, retrievedLoanResponseDTOList.size());

        assertEquals(loanList.get(0).getLoanId(), retrievedLoanResponseDTOList.get(0).getLoanId());
        assertEquals(loanList.get(0).getLoanAmount(), retrievedLoanResponseDTOList.get(0).getLoanAmount());
        assertEquals(loanList.get(0).getMonthlyPayment(), retrievedLoanResponseDTOList.get(0).getMonthlyPayment());
        assertEquals(loanList.get(0).getInterest(), retrievedLoanResponseDTOList.get(0).getInterest());
        assertEquals(loanList.get(0).getLoanStartDate(), retrievedLoanResponseDTOList.get(0).getLoanStartDate());
        assertEquals(loanList.get(0).getRepaymentPeriod(), retrievedLoanResponseDTOList.get(0).getRepaymentPeriod());

        assertEquals(loanList.get(1).getLoanId(), retrievedLoanResponseDTOList.get(1).getLoanId());
        assertEquals(loanList.get(1).getLoanAmount(), retrievedLoanResponseDTOList.get(1).getLoanAmount());
        assertEquals(loanList.get(1).getMonthlyPayment(), retrievedLoanResponseDTOList.get(1).getMonthlyPayment());
        assertEquals(loanList.get(1).getInterest(), retrievedLoanResponseDTOList.get(1).getInterest());
        assertEquals(loanList.get(1).getLoanStartDate(), retrievedLoanResponseDTOList.get(1).getLoanStartDate());
        assertEquals(loanList.get(1).getRepaymentPeriod(), retrievedLoanResponseDTOList.get(1).getRepaymentPeriod());
    }
}