package com.lending.application.mapper.loan;

import com.lending.application.domain.loan.Loan;
import com.lending.application.domain.loan.LoanResponseDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(implementationPackage = "loan")
public interface LoanResponseMapper {
    LoanResponseDTO mapToLoanDTO(Loan loan);
    List<LoanResponseDTO> mapToListLoanDTO(List<Loan> loanList);
}