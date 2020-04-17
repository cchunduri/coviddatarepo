package com.chaitu.dashboard.carona.dao.models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@Entity(name = "world_places")
public class PlacesModel {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long placeId;

    private int totalNumberOfConfirmed;
    private int totalNumberOfDeaths;
    private int totalNumberOfRecovered;
    private LocalDate timeOfUpdate;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "place_id")
    private List<CountryModel> countryList;
}
