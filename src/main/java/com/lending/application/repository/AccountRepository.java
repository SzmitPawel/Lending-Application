package com.lending.application.repository;

import com.lending.application.domain.Account;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface AccountRepository extends JpaRepository<Account,Long> {
}
