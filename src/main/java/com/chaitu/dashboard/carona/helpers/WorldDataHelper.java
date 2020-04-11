package com.chaitu.dashboard.carona.helpers;

import com.chaitu.dashboard.carona.dao.models.CountryModel;
import com.chaitu.dashboard.carona.dao.models.PlacesModel;
import com.chaitu.dashboard.carona.utils.AppUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WorldDataHelper {

    private final AppUtils appUtils;

    public WorldDataHelper(AppUtils appUtils) {
        this.appUtils = appUtils;
    }

    public CountryModel getCountryModel(LocalDateTime presentTime, PlacesModel placesModel, Element countryColumn) {
        List<String> columns = countryColumn.select("td")
                .stream()
                .map(Element::text)
                .collect(Collectors.toList());
        CountryModel countryModel = new CountryModel();
        if(StringUtils.equals(columns.get(0), "World")) {
            placesModel.setTotalNumberOfConfirmed(appUtils.escapeCommas(columns.get(1)));
            placesModel.setTotalNumberOfDeaths(appUtils.escapeCommas(columns.get(3)));
            placesModel.setTotalNumberOfRecovered(appUtils.escapeCommas(columns.get(5)));
        } else {
            for (int i = 0; i < columns.size(); i++) {
                switch (i) {
                    case 0:
                        countryModel.setCountryName(columns.get(i));
                        break;
                    case 1:
                        countryModel.setNumberOfConfirmed(appUtils.escapeCommas(columns.get(i)));
                        break;
                    case 3:
                        countryModel.setNumberOfDeaths(appUtils.escapeCommas(columns.get(i)));
                        break;
                    case 5:
                        countryModel.setNumberOfRecovered(appUtils.escapeCommas(columns.get(i)));
                        break;
                }
            }
            countryModel.setUpdatedTime(presentTime);
            if (!countryModel.getCountryName().equals("Total:")) {
                return countryModel;
            }
        }
        return null;
    }
}
