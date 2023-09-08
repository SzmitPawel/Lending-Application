package com.lending.application.repository;

import com.lending.application.domain.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan,Long> {
    Loan saveAndFlush(Loan loan);
}
