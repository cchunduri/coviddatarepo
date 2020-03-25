package com.chaitu.dashboard.carona.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TimeSeries {
    Map<String, List<CountryData>> country;
}
