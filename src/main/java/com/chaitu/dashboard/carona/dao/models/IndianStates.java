package com.chaitu.dashboard.carona.dao.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity(name = "indian_state")
public class IndianStates {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int numberOfConfirmed;
    private int numberOfDeaths;
    private int numberOfRecovered;
    private String nameOfThePlace;
    private String country;
}
