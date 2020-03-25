package com.chaitu.dashboard.carona.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class RestClientUtils {

    @Value("${corona.datapoint.three}")
    private String worldDataTimeSeries;

    final RestTemplate restTemplate;

    public RestClientUtils(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getWorldData() {
        String result = restTemplate.getForObject(worldDataTimeSeries, String.class);
        return result;
    }
}
