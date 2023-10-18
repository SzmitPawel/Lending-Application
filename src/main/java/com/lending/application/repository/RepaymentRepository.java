package com.lending.application.repository;

import com.lending.application.domain.Repayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface RepaymentRepository extends JpaRepository<Repayment,Long> {
}
