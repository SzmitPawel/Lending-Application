package com.lending.application.repository;

import com.lending.application.domain.loan.Loan;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface LoanRepository extends JpaRepository<Loan,Long> {
}
