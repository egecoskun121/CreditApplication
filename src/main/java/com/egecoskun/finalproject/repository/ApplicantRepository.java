package com.egecoskun.finalproject.repository;

import com.egecoskun.finalproject.model.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ApplicantRepository extends JpaRepository<Applicant,Long> {

    @Query(value = "SELECT * FROM applicant WHERE applicant.identification_number=:identificationNumber",nativeQuery = true)
    Optional<Applicant> getApplicantByIdentificationNumber(@Param("identificationNumber") Long identificationNumber);

}
