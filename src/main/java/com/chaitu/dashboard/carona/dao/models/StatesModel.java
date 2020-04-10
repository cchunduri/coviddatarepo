package com.chaitu.dashboard.carona.dao.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity(name = "states")
public class StatesModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long stateId;

    private int numberOfConfirmed;
    private int numberOfDeaths;
    private int numberOfRecovered;
    private String nameOfTheState;
}
