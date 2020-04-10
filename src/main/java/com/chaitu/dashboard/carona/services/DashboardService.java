package com.chaitu.dashboard.carona.services;

import com.chaitu.dashboard.carona.dao.CountriesDao;
import com.chaitu.dashboard.carona.dao.PlacesDao;
import com.chaitu.dashboard.carona.dao.StatesDao;
import com.chaitu.dashboard.carona.dao.models.CountryModel;
import com.chaitu.dashboard.carona.dao.models.PlacesModel;
import com.chaitu.dashboard.carona.dao.models.StatesModel;
import com.chaitu.dashboard.carona.helpers.IndiaStatesDataHelper;
import com.chaitu.dashboard.carona.helpers.WorldDataHelper;
import com.chaitu.dashboard.carona.utils.HtmlUtils;
import com.chaitu.dashboard.carona.utils.RestClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalTime;
import java.util.List;
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

    public Boolean getWorldData() {
        List<CountryModel> countryList;
        var htmlDocumentOptional = htmlUtils.parseHtmlByUrl(worldHealthMinistryUrl);
        LocalTime presentTime = LocalTime.now();
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
        } else {
            String timeSeriesData = restClientUtils.getWorldData();
            log.info("{}", timeSeriesData);
            //TODO
        }
        return Boolean.FALSE;
    }

    public Optional<CountryModel> getLatestDataByCountry(String countryName) {
        return countriesDao.getLatestDataByCountryName(countryName);
    }

    public Optional<StatesModel> getLatestDataByState(String stateName) {
        return statesDao.getLatestDataByState(stateName);
    }

    public PlacesModel getLatestWorldData() {
        return placesDao.getLatestWorldData();
    }
}
