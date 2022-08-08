package com.egecoskun.finalproject.repository;

import com.egecoskun.finalproject.model.Applicant;
import com.egecoskun.finalproject.model.CreditRating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditRatingRepository extends JpaRepository<CreditRating,Long> {
}
