package com.lending.application.repository;

import com.lending.application.domain.CreditRating;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface CreditRatingRepository extends JpaRepository<CreditRating,Long> {
    List<CreditRating> findAll();
}
