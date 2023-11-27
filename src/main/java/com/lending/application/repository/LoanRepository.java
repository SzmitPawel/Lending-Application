package com.lending.application.repository;

import com.lending.application.domain.Loan;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface LoanRepository extends JpaRepository<Loan,Long> {
}
