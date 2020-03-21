package com.chaitu.dashboard.carona.dao;

import com.chaitu.dashboard.carona.dao.models.PlacesModel;
import com.chaitu.dashboard.carona.dao.repos.WorldRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class PlacesDao {

    private final WorldRepository worldRepository;

    public PlacesDao(WorldRepository worldRepository) {
        this.worldRepository = worldRepository;
    }

    public void updateWorldPlaces(PlacesModel placesModel, boolean isIndianData) {
        PlacesModel dataFromDb;
        if (isIndianData)
            dataFromDb = worldRepository.findByNameOfThePlace(placesModel.getNameOfThePlace());
        else
            dataFromDb = worldRepository.findByCountry(placesModel.getCountry());

        if (null != dataFromDb) {
            dataFromDb.setCountry(placesModel.getCountry());
            dataFromDb.setNumberOfConfirmed(placesModel.getNumberOfConfirmed());
            dataFromDb.setNumberOfDeaths(placesModel.getNumberOfDeaths());
            dataFromDb.setNumberOfRecovered(placesModel.getNumberOfRecovered());
            dataFromDb.setNameOfThePlace(placesModel.getNameOfThePlace());
            worldRepository.save(dataFromDb);
        } else {
            worldRepository.save(placesModel);
        }
    }

    public void saveDataToDb(List<PlacesModel> placesList, boolean isIndian) {
        placesList.forEach(placesModel -> updateWorldPlaces(placesModel, isIndian));
    }

    public List<PlacesModel> getIndianStatesData() {
        return worldRepository.findAllIndianData();
    }
}
