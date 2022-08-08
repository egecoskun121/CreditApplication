package com.egecoskun.finalproject.model.DTO;

import lombok.Data;

import java.math.BigInteger;

@Data
public class ApplicantDTO {

    private BigInteger identificationNumber;
    private String firstName;
    private String lastName;
    private double monthlyIncome;
    private String phoneNumber;

}
