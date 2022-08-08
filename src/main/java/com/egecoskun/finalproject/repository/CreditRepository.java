package com.egecoskun.finalproject.repository;


import com.egecoskun.finalproject.model.Credit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditRepository extends JpaRepository<Credit,Long> {
}
