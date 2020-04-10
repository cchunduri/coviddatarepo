package com.chaitu.dashboard.carona.dao.repos;

import com.chaitu.dashboard.carona.dao.models.CountryModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountriesRepo extends CrudRepository<CountryModel, Long> {
    CountryModel findTopByCountryNameOrderByCountryIdDesc(String countryName);
}
