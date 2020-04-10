package com.chaitu.dashboard.carona.dao.repos;

import com.chaitu.dashboard.carona.dao.models.StatesModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatesRepo extends CrudRepository<StatesModel, Long> {
    StatesModel findTopByNameOfTheStateOrderByStateIdDesc(String stateName);
}
