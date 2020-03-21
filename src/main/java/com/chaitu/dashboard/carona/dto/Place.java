package com.chaitu.dashboard.carona.dto;

import lombok.Data;

@Data
public class Place {
    private int numberOfConfirmed;
    private int numberOfDeaths;
    private int numberOfRecovered;
    private String nameOfThePlace;
    private String country;
}
