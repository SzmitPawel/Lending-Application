package com.lending.application.repository;

import com.lending.application.domain.Client;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface ClientRepository extends JpaRepository<Client,Long> {
    List<Client> findAll();
}
