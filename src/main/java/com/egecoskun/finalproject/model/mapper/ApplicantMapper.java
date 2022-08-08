package com.egecoskun.finalproject.model.mapper;

import com.egecoskun.finalproject.model.DTO.ApplicantDTO;
import com.egecoskun.finalproject.model.Applicant;

public class ApplicantMapper {

    public static ApplicantDTO toDTO(Applicant applicant){

        ApplicantDTO applicantDTO = new ApplicantDTO();
        applicantDTO.setFirstName(applicant.getFirstName());
        applicantDTO.setIdentificationNumber(applicant.getIdentificationNumber());
        applicantDTO.setLastName(applicant.getLastName());
        applicantDTO.setMonthlyIncome(applicant.getMonthlyIncome());
        applicantDTO.setPhoneNumber(applicant.getPhoneNumber());


        return applicantDTO;
    }

    public static Applicant toEntity(ApplicantDTO applicantDTO){

        Applicant applicant= new Applicant();
        applicant.setFirstName(applicantDTO.getFirstName());
        applicant.setIdentificationNumber(applicantDTO.getIdentificationNumber());
        applicant.setLastName(applicantDTO.getLastName());
        applicant.setMonthlyIncome(applicantDTO.getMonthlyIncome());
        applicant.setPhoneNumber(applicantDTO.getPhoneNumber());

        return applicant;
    }
}
