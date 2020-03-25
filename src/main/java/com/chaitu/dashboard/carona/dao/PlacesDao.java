package com.chaitu.dashboard.carona.dao;

import com.chaitu.dashboard.carona.dao.models.IndianStates;
import com.chaitu.dashboard.carona.dao.models.PlacesModel;
import com.chaitu.dashboard.carona.dao.repos.IndianStateRepo;
import com.chaitu.dashboard.carona.dao.repos.WorldRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@Repository
public class PlacesDao {

    private final WorldRepository worldRepository;
    private final IndianStateRepo indianStateRepo;

    public PlacesDao(WorldRepository worldRepository, IndianStateRepo indianStateRepo) {
        this.worldRepository = worldRepository;
        this.indianStateRepo = indianStateRepo;
    }

    public void updateWorldPlaces(PlacesModel placesModel) {
        PlacesModel dataFromDb = worldRepository.findByCountry(placesModel.getCountry());
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

    public void updateIndianState(IndianStates indianStatesModel) {
        IndianStates dataFromDb = indianStateRepo.findByNameOfThePlace(indianStatesModel.getNameOfThePlace());
        if (null == dataFromDb) {
            IndianStates indianStates = new IndianStates();
            indianStates.setCountry("India");
            indianStates.setNumberOfConfirmed(indianStatesModel.getNumberOfConfirmed());
            indianStates.setNumberOfDeaths(indianStatesModel.getNumberOfDeaths());
            indianStates.setNumberOfRecovered(indianStatesModel.getNumberOfRecovered());
            indianStates.setNameOfThePlace(indianStatesModel.getNameOfThePlace());
            indianStateRepo.save(indianStates);
        } else {
            indianStateRepo.save(dataFromDb);
        }
    }

    public Boolean saveWorldDataToDb(List<PlacesModel> placesList) {
        try {
            placesList.forEach(this::updateWorldPlaces);
        } catch (Exception sqlException) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public Boolean saveIndianDataToDb(@NotNull List<IndianStates> indianStatesList) {
        try {
            indianStatesList.forEach(this::updateIndianState);
        } catch (Exception sqlException) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public List<IndianStates> getIndianStatesData() {
        return indianStateRepo.findAll();
    }
}
