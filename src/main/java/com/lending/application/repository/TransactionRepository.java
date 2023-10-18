package com.lending.application.repository;

import com.lending.application.domain.Transaction;
import com.lending.application.domain.TransactionMethodEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByAccount_AccountId(Long accountId);
    List<Transaction> findTransactionsByTransactionMethodEnumIsAndAccount_AccountId(TransactionMethodEnum methodEnum, Long accountId);
}
