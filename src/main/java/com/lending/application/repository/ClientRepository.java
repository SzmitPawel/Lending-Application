package com.lending.application.repository;

import com.lending.application.domain.Client;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface ClientRepository extends CrudRepository<Client,Long> {
    List<Client> findAll();
}
