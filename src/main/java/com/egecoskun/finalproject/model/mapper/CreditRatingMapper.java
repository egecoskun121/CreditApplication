package com.egecoskun.finalproject.model.mapper;

import com.egecoskun.finalproject.model.Applicant;
import com.egecoskun.finalproject.model.CreditRating;
import com.egecoskun.finalproject.model.DTO.ApplicantDTO;
import com.egecoskun.finalproject.model.DTO.CreditRatingDTO;

public class CreditRatingMapper {

    public static CreditRatingDTO toDTO(CreditRating creditRating){

        CreditRatingDTO creditRatingDTO = new CreditRatingDTO();
        creditRatingDTO.setCreditRating(creditRating.getCreditRating());


        return creditRatingDTO;
    }

    public static CreditRating toEntity(CreditRatingDTO creditRatingDTO){

        CreditRating creditRating= new CreditRating();
        creditRating.setCreditRating(creditRatingDTO.getCreditRating());


        return creditRating;
    }
}
