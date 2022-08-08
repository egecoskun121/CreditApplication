package com.egecoskun.finalproject.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "credit")
public class Credit {

    @Id
    private Long id;
    private int creditBalance;
    private String creditResult;

}
