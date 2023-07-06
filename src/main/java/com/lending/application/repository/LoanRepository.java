package com.lending.application.repository;

import com.lending.application.domain.Loan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LoanRepository extends CrudRepository<Loan,Long> {
    List<Loan> findAll();
}
