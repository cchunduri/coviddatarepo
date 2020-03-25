package com.chaitu.dashboard.carona.controllers;

import com.chaitu.dashboard.carona.dao.repos.WorldRepository;
import com.chaitu.dashboard.carona.dao.models.PlacesModel;
import com.chaitu.dashboard.carona.services.DashboardService;
import com.chaitu.dashboard.carona.dto.Place;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
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

    @GetMapping("/getCasesByCountry")
    private List<Place> getCasesByCountry() {
        return null;
    }

    @GetMapping("/getIndianStatesData")
    private List<Place> getIndianStatesData() {
        return dashboardService.getIndianStatesDataFromDb();
    }

    @GetMapping("/getAllWorldData")
    private List<PlacesModel> getWorldData() {
        return  (List<PlacesModel>) worldRepository.findAll();
    }

    @Scheduled(cron = "${data.retrieval.schedule}")
    @GetMapping("/retrieveDataFromSources")
    private void retrieveDataFromDataSources() {
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();
        completableFuture.completeAsync(() -> {
            log.info("Retrieving Indian data");
            return dashboardService.getIndianStatesData();
        }).completeAsync(() -> {
            log.info("Retrieving World Data");
            return dashboardService.getWorldData();
        }).thenAccept(result -> {
            log.info("Completed Retrieving data...{}", result);
        });
    }
}
