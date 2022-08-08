package com.egecoskun.finalproject.model.DTO;

import lombok.Data;


@Data
public class ApplicantDTO {

    private Long identificationNumber;
    private String firstName;
    private String lastName;
    private double monthlyIncome;
    private String phoneNumber;

}
