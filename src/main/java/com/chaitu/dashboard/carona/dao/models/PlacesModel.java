package com.chaitu.dashboard.carona.dao.models;

import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@Data
@Entity(name = "world_places")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
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
