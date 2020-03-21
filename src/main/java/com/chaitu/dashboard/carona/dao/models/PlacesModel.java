package com.chaitu.dashboard.carona.dao.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "world_places")
public class PlacesModel {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long id;

    private int numberOfConfirmed;
    private int numberOfDeaths;
    private int numberOfRecovered;
    private String nameOfThePlace;
    private String country;
}
