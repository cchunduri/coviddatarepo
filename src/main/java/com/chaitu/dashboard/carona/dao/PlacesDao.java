package com.chaitu.dashboard.carona.dao;

import com.chaitu.dashboard.carona.dao.models.PlacesModel;
import com.chaitu.dashboard.carona.dao.models.StatesModel;
import com.chaitu.dashboard.carona.dao.repos.WorldRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@Repository
public class PlacesDao {

    private final WorldRepository worldRepository;
    private final CountriesDao countriesDao;

    public PlacesDao(WorldRepository worldRepository, CountriesDao countriesDao) {
        this.worldRepository = worldRepository;
        this.countriesDao = countriesDao;
    }

    public PlacesModel getLatestWorldData() {
        return worldRepository.findTopByOrderByPlaceIdDesc();
    }

    public Boolean saveWorldDataToDb(PlacesModel placesModel) {
        try {
            worldRepository.save(placesModel);
        } catch (Exception sqlException) {
            log.error("Got Exception", sqlException);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public Boolean saveDataByCountry(@NotNull List<StatesModel> statesList) {
        try {
            countriesDao.updateStatesByCountry(statesList, getLatestWorldData());
        } catch (Exception sqlException) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
