package com.chaitu.dashboard.carona.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class RestClientUtils {

    @Value("${corona.datapoint.three}")
    private String worldDataTimeSeries;

    final RestTemplate restTemplate;

    public RestClientUtils(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, List<Map<String, Object>>> getWorldData() {
        ResponseEntity<Map> responseEntity =
                restTemplate.getForEntity(worldDataTimeSeries, Map.class);
        return (Map<String, List<Map<String, Object>>>)responseEntity.getBody();
    }
}
