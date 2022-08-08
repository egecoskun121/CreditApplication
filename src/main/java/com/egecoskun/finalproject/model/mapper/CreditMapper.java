package com.egecoskun.finalproject.model.mapper;


import com.egecoskun.finalproject.model.Credit;
import com.egecoskun.finalproject.model.DTO.CreditDTO;
import org.springframework.stereotype.Component;

@Component
public class CreditMapper {
    public static CreditDTO toDTO(Credit credit){

        CreditDTO creditDTO = new CreditDTO();
        creditDTO.setCreditResult(credit.getCreditResult());
        creditDTO.setCreditBalance(credit.getCreditBalance());


        return creditDTO;
    }

    public static Credit toEntity(CreditDTO creditDTO){

        Credit credit= new Credit();
        credit.setCreditResult(creditDTO.getCreditResult());
        credit.setCreditBalance(creditDTO.getCreditBalance());

        return credit;
    }
}
