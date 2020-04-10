package com.chaitu.dashboard.carona.dao.repos;

import com.chaitu.dashboard.carona.dao.models.PlacesModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorldRepository extends CrudRepository<PlacesModel, Long> {
    PlacesModel findTopByOrderByPlaceIdDesc();
}
