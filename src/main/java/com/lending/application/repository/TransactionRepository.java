package com.lending.application.repository;

import com.lending.application.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
