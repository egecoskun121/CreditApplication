package com.egecoskun.finalproject.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "credit_rating")
public class CreditRating {

    @Id
    private Long id;
    private int creditRating;

}
