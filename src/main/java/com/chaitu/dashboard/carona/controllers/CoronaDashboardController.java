package com.chaitu.dashboard.carona.controllers;

import com.chaitu.dashboard.carona.dao.repos.WorldRepository;
import com.chaitu.dashboard.carona.dao.models.PlacesModel;
import com.chaitu.dashboard.carona.dto.Place;
import com.chaitu.dashboard.carona.services.DashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@CrossOrigin
public class CoronaDashboardController {

    final DashboardService dashboardService;
    final WorldRepository worldRepository;

    public CoronaDashboardController(DashboardService dashboardService,
                                     WorldRepository worldRepository) {
        this.dashboardService = dashboardService;
        this.worldRepository = worldRepository;
    }

    @GetMapping("/")
    private String helloDashBoard() {
        return "Welcome to Dashboard";
    }

    @GetMapping("/getLatestByCountry/{countryName}")
    private Place getLatestDataByCountry(@PathVariable String countryName) {
        return dashboardService.getLatestDataByCountry(countryName);
    }

    @GetMapping("/getLatestByState/{stateName}")
    private Place getLatestDataByState(@PathVariable String stateName) {
        return dashboardService.getLatestDataByState(stateName);
    }

    @GetMapping("/getAllCountriesData")
    private PlacesModel getAllCountries() {
        return dashboardService.getAllCountries();
    }

    @GetMapping("/getWorldData")
    private Place getWorldData() {
        return dashboardService.getLatestWorldData();
    }

    @Scheduled(cron = "${data.retrieval.schedule}")
    @GetMapping("/retrieveDataFromSources")
    private void retrieveDataFromDataSources() {
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();
        completableFuture.completeAsync(() -> {
            log.info("Retrieving World Data");
            return dashboardService.getWorldData();
        }).thenApply(result -> {
            if (result) {
                log.info("Retrieving Indian data");
                return dashboardService.getIndianStatesData();
            }
            return false;
        }).thenAccept(result -> {
            if (result)
                log.info("Completed Retrieving data successfully {}", LocalDateTime.now());
            else
                log.info("Failed retrieving data, refer to logs {}", LocalDateTime.now());
        });
    }
}
