package com.lending.application.repository;

import com.lending.application.domain.Account;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface AccountRepository extends JpaRepository<Account,Long> {
    List<Account> findAll();
}
