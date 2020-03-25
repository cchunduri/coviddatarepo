package com.chaitu.dashboard.carona.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CountryData {
    private LocalDate date;
    private Integer confirmed;
    private Integer deaths;
    private Integer recovered;
}
