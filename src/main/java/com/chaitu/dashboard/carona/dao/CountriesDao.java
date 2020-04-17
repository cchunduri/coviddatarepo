package com.chaitu.dashboard.carona.dao;

import com.chaitu.dashboard.carona.dao.models.CountryModel;
import com.chaitu.dashboard.carona.dao.models.PlacesModel;
import com.chaitu.dashboard.carona.dao.models.StatesModel;
import com.chaitu.dashboard.carona.dao.repos.CountriesRepo;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class CountriesDao {

    private final CountriesRepo countriesRepo;

    public CountriesDao(CountriesRepo countriesRepo) {
        this.countriesRepo = countriesRepo;
    }

    public Optional<CountryModel> saveByCountry(CountryModel countryModel) {
        return Optional.of(countriesRepo.save(countryModel));
    }

    public Optional<CountryModel> getLatestDataByCountryName(String countryName) {
        return Optional.ofNullable(countriesRepo.findTopByCountryNameOrderByCountryIdDesc(countryName));
    }

    public void updateStatesByCountry(List<StatesModel> statesList, PlacesModel worldData) {
        worldData.getCountryList()
                .stream()
                .filter(countryModel -> countryModel.getCountryName().equals("India"))
                .findFirst()
                .ifPresent(indiaDetails -> {
                    indiaDetails.setStatesList(statesList);
                    indiaDetails.setUpdatedTime(LocalDate.now());
                    saveByCountry(indiaDetails);
                });
    }
}
