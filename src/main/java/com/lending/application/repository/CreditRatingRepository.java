package com.lending.application.repository;

import com.lending.application.domain.credit.rating.CreditRating;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface CreditRatingRepository extends JpaRepository<CreditRating, Long> {
}
