package com.chaitu.dashboard.carona.dao.models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@Entity(name = "countries")
public class CountryModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long countryId;

    private int numberOfConfirmed;
    private int numberOfDeaths;
    private int numberOfRecovered;
    private LocalDateTime updatedTime;
    private String countryName;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "country_id")
    private List<StatesModel> statesList;
}
