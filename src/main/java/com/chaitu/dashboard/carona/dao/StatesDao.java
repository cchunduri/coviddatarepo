package com.chaitu.dashboard.carona.dao;

import com.chaitu.dashboard.carona.dao.models.StatesModel;
import com.chaitu.dashboard.carona.dao.repos.StatesRepo;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class StatesDao {
    private final StatesRepo statesRepo;

    public StatesDao(StatesRepo statesRepo) {
        this.statesRepo = statesRepo;
    }

    public Optional<StatesModel> getLatestDataByState(String stateName) {
        return Optional.of(statesRepo.findTopByNameOfTheStateOrderByStateIdDesc(stateName));
    }
}
