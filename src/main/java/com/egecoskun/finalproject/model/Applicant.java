package com.egecoskun.finalproject.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigInteger;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "applicant")
public class Applicant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigInteger identificationNumber;
    private String firstName;
    private String lastName;
    private double monthlyIncome;
    private String phoneNumber;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "credit_rating_id", referencedColumnName = "id")
    private CreditRating creditRating;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name = "applicant_credit",
            joinColumns = {@JoinColumn(name = "applicant_id")},
            inverseJoinColumns = {@JoinColumn(name = "credit_id")}
    )
    private Credit credit;





}
