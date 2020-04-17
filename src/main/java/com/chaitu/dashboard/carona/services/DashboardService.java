package com.chaitu.dashboard.carona.services;

import com.chaitu.dashboard.carona.dao.CountriesDao;
import com.chaitu.dashboard.carona.dao.PlacesDao;
import com.chaitu.dashboard.carona.dao.StatesDao;
import com.chaitu.dashboard.carona.dao.models.CountryModel;
import com.chaitu.dashboard.carona.dao.models.PlacesModel;
import com.chaitu.dashboard.carona.dao.models.StatesModel;
import com.chaitu.dashboard.carona.dto.Place;
import com.chaitu.dashboard.carona.helpers.IndiaStatesDataHelper;
import com.chaitu.dashboard.carona.helpers.WorldDataHelper;
import com.chaitu.dashboard.carona.utils.HtmlUtils;
import com.chaitu.dashboard.carona.utils.RestClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class DashboardService {

    @Value("${corona.datapoint.one}")
    private String indianHealthMinistryUrl;

    @Value("${corona.datapoint.two}")
    private String worldHealthMinistryUrl;

    private final PlacesDao placesDao;
    private final HtmlUtils htmlUtils;
    private final RestClientUtils restClientUtils;
    private final CountriesDao countriesDao;
    private final StatesDao statesDao;

    private final WorldDataHelper worldDataHelper;

    private final IndiaStatesDataHelper indiaStatesDataHelper;

    public DashboardService(PlacesDao placesDao, HtmlUtils htmlUtils,
                            RestClientUtils restClientUtils,
                            CountriesDao countriesDao, StatesDao statesDao,
                            WorldDataHelper worldDataHelper,
                            IndiaStatesDataHelper indiaStatesDataHelper) {
        this.placesDao = placesDao;
        this.htmlUtils = htmlUtils;
        this.restClientUtils = restClientUtils;
        this.countriesDao = countriesDao;
        this.statesDao = statesDao;
        this.worldDataHelper = worldDataHelper;
        this.indiaStatesDataHelper = indiaStatesDataHelper;
    }

    public Boolean getIndianStatesData() {
        var htmlDocumentOptional = htmlUtils.parseHtmlByUrl(indianHealthMinistryUrl);
        if (htmlDocumentOptional.isPresent()) {
            var htmlDocument = htmlDocumentOptional.get();
            Elements elementsList = htmlDocument.getElementById("state-data").getElementsByTag("table").select("tr");
            List<StatesModel> placesModelList = elementsList
                    .stream()
                    .skip(1)
                    .map(indiaStatesDataHelper::getStatesModel)
                    .collect(Collectors.toList());
            return placesDao.saveDataByCountry(placesModelList);
        }
        return Boolean.FALSE;
    }

    public Boolean getWorldData(Boolean alternative) {
        List<CountryModel> countryList;
        if (!alternative) {
            var htmlDocumentOptional = htmlUtils.parseHtmlByUrl(worldHealthMinistryUrl);
            LocalDate presentTime = LocalDate.now();
            if (htmlDocumentOptional.isPresent()) {
                var htmlDocument = htmlDocumentOptional.get();
                PlacesModel placesModel = new PlacesModel();
                countryList = htmlDocument.getElementById("main_table_countries_today")
                        .select("tr")
                        .stream()
                        .skip(8)
                        .map(tr -> worldDataHelper.getCountryModel(presentTime, placesModel, tr))
                        .collect(Collectors.toList());
                placesModel.setTimeOfUpdate(presentTime);
                placesModel.setCountryList(countryList);
                return placesDao.saveWorldDataToDb(placesModel);
            }
        } else {
            Map<String, List<Map<String, Object>>> timeSeriesData = restClientUtils.getWorldData();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-M-dd");
            List<CountryModel> countryModelsList = timeSeriesData.entrySet()
                    .stream()
                    .map(entiry -> {
                        List<Map<String, Object>> countryCompleteData = entiry.getValue();
                        Map<String, Object> latestData = countryCompleteData.get(countryCompleteData.size() - 1);
                        CountryModel countryModel = new CountryModel();
                        countryModel.setCountryName(entiry.getKey());
                        countryModel.setUpdatedTime(LocalDate.parse((CharSequence) latestData.get("date"), dateTimeFormatter));
                        countryModel.setNumberOfConfirmed((Integer) latestData.get("confirmed"));
                        countryModel.setNumberOfDeaths((Integer) latestData.get("deaths"));
                        countryModel.setNumberOfRecovered((Integer) latestData.get("recovered"));
                        return countryModel;
                    })
                    .collect(Collectors.toList());
            PlacesModel placesModel = new PlacesModel();
            placesModel.setTimeOfUpdate(LocalDate.now());
            placesModel.setCountryList(countryModelsList);
            return placesDao.saveWorldDataToDb(placesModel);
        }
        return Boolean.FALSE;
    }

    public Optional<Place> getLatestDataByCountry(String countryName) {
        Optional<CountryModel> latestDataByCountryName = countriesDao.getLatestDataByCountryName(countryName);
        if (latestDataByCountryName.isPresent()) {
            CountryModel countryModel = latestDataByCountryName.get();
            Place world = new Place();
            world.setNumberOfConfirmed(countryModel.getNumberOfConfirmed());
            world.setNumberOfDeaths(countryModel.getNumberOfDeaths());
            world.setNumberOfRecovered(countryModel.getNumberOfRecovered());
            world.setNameOfThePlace(countryModel.getCountryName());
            return Optional.of(world);
        }
        return Optional.empty();
    }

    public Place getLatestDataByState(String stateName) {
        Optional<StatesModel> latestDataByState = statesDao.getLatestDataByState(stateName);
        if (latestDataByState.isPresent()) {
            StatesModel statesModel = latestDataByState.get();
            Place world = new Place();
            world.setNumberOfConfirmed(statesModel.getNumberOfConfirmed());
            world.setNumberOfDeaths(statesModel.getNumberOfDeaths());
            world.setNumberOfRecovered(statesModel.getNumberOfRecovered());
            world.setNameOfThePlace(statesModel.getNameOfTheState());
            return world;
        }

        return null;
    }

    public PlacesModel getAllCountries() {
        return placesDao.getLatestWorldData();
    }

    public Place getLatestWorldData() {
        PlacesModel placesModel = placesDao.getLatestWorldData();
        Place world = new Place();
        world.setNumberOfConfirmed(placesModel.getTotalNumberOfConfirmed());
        world.setNumberOfDeaths(placesModel.getTotalNumberOfDeaths());
        world.setNumberOfRecovered(placesModel.getTotalNumberOfRecovered());
        return world;
    }
}
