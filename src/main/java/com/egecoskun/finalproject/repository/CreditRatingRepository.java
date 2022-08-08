package com.egecoskun.finalproject.repository;


import com.egecoskun.finalproject.model.CreditRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditRatingRepository extends JpaRepository<CreditRating,Long> {
}
