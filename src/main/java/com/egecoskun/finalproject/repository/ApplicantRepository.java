package com.egecoskun.finalproject.repository;

import com.egecoskun.finalproject.model.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicantRepository extends JpaRepository<Applicant,Long> {
}
