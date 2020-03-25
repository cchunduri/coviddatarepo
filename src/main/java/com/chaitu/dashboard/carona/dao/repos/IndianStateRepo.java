package com.chaitu.dashboard.carona.dao.repos;

import com.chaitu.dashboard.carona.dao.models.IndianStates;
import com.chaitu.dashboard.carona.dao.models.PlacesModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndianStateRepo extends CrudRepository<IndianStates, Long> {

    IndianStates findByNameOfThePlace(String nameOfThePlace);
    List<IndianStates> findAll();
}
