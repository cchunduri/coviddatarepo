package com.chaitu.dashboard.carona.services;

import com.chaitu.dashboard.carona.dao.PlacesDao;
import com.chaitu.dashboard.carona.dao.models.PlacesModel;
import com.chaitu.dashboard.carona.dto.Place;
import com.chaitu.dashboard.carona.utils.HtmlUtils;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DashboardService {

    @Value("${corona.datapoint.one}")
    private String indianHealthMinistryUrl;

    @Value("${corona.datapoint.two}")
    private String worldHealthMinistryUrl;

    private final PlacesDao placesDao;
    private final HtmlUtils htmlUtils;

    public DashboardService(PlacesDao placesDao, HtmlUtils htmlUtils) {
        this.placesDao = placesDao;
        this.htmlUtils = htmlUtils;
    }

    public void getCasesByCountry(String country) {

    }

    public void getIndianStatesData() {
        var htmlDocumentOptional = htmlUtils.parseHtmlByUrl(indianHealthMinistryUrl);
        if (htmlDocumentOptional.isPresent()) {
            var htmlDocument = htmlDocumentOptional.get();
            List<PlacesModel> placesModelList = htmlDocument.getElementsByTag("table")
                    .select("tr")
                    .stream()
                    .skip(1)
                    .map(tr -> {
                        List<String> columns = tr.select("td").eachText();
                        PlacesModel place = new PlacesModel();
                        for (int i = 1; i < columns.size(); i++) {
                            switch (i) {
                                case 1:
                                    place.setNameOfThePlace(columns.get(i));
                                    break;
                                case 3:
                                    int stateConfirmation = cleanText(columns.get(i - 1));
                                    int foreignConfirmation = cleanText(columns.get(i));
                                    place.setNumberOfConfirmed(stateConfirmation + foreignConfirmation);
                                    break;
                                case 4:
                                    place.setNumberOfRecovered(cleanText(columns.get(i)));
                                    break;
                                case 5:
                                    place.setNumberOfDeaths(cleanText(columns.get(i)));
                                    break;
                            }
                        }
                        place.setCountry("India");
                        return place;
                    })
                    .collect(Collectors.toList());
            placesDao.saveDataToDb(placesModelList, true);
        }
    }

    private int cleanText(String number) {
        return Integer.parseInt(number.split(" ")[0].trim());
    }

    private int escapeCommas(String num) {
        if (StringUtils.isEmpty(num)) {
            return 0;
        } else {
            return Integer.parseInt(num.replace(",", ""));
        }
    }

    public void getWorldData() {
        List<PlacesModel> placesList = new ArrayList<>();
        var htmlDocumentOptional = htmlUtils.parseHtmlByUrl(worldHealthMinistryUrl);
        if (htmlDocumentOptional.isPresent()) {
            var htmlDocument = htmlDocumentOptional.get();
            placesList = htmlDocument.getElementById("main_table_countries_today")
                    .select("tr")
                    .stream()
                    .skip(1)
                    .map(tr -> {
                        List<String> columns = tr.select("td")
                                .stream()
                                .map(Element::text)
                                .collect(Collectors.toList());

                        PlacesModel place = new PlacesModel();
                        for (int i = 0; i < columns.size(); i++) {
                            switch (i) {
                                case 0:
                                    place.setCountry(columns.get(i));
                                    break;
                                case 1:
                                    place.setNumberOfConfirmed(escapeCommas(columns.get(i)));
                                    break;
                                case 3:
                                    place.setNumberOfDeaths(escapeCommas(columns.get(i)));
                                    break;
                                case 5:
                                    place.setNumberOfRecovered(escapeCommas(columns.get(i)));
                                    break;
                            }
                        }
                        return place;
                    })
                    .collect(Collectors.toList());
            placesDao.saveDataToDb(placesList, false);
        }
    }

    public List<Place> getIndianStatesDataFromDb() {
        return placesDao.getIndianStatesData()
                .stream()
                .map(placesModel -> {
                    Place place = new Place();
                    place.setCountry(placesModel.getCountry());
                    place.setNumberOfConfirmed(placesModel.getNumberOfConfirmed());
                    place.setNumberOfDeaths(placesModel.getNumberOfDeaths());
                    place.setNumberOfRecovered(placesModel.getNumberOfRecovered());
                    place.setNameOfThePlace(placesModel.getNameOfThePlace());
                    return place;
                })
                .collect(Collectors.toList());
    }
}
