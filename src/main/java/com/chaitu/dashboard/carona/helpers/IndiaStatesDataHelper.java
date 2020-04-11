package com.chaitu.dashboard.carona.helpers;

import com.chaitu.dashboard.carona.dao.models.StatesModel;
import com.chaitu.dashboard.carona.utils.AppUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IndiaStatesDataHelper {

    private final AppUtils appUtils;

    public IndiaStatesDataHelper(AppUtils appUtils) {
        this.appUtils = appUtils;
    }

    public StatesModel getStatesModel(Element statesColumn) {
        List<String> columns = statesColumn.select("td").eachText();
        if (StringUtils.isNumeric(columns.get(0))) {
            StatesModel state = new StatesModel();
            for (int i = 1; i < columns.size(); i++) {
                switch (i) {
                    case 1:
                        state.setNameOfTheState(columns.get(i));
                        break;
                    case 2:
                        state.setNumberOfConfirmed( appUtils.cleanText(columns.get(i)));
                        break;
                    case 3:
                        state.setNumberOfRecovered(appUtils.cleanText(columns.get(i)));
                        break;
                    case 4:
                        state.setNumberOfDeaths(appUtils.cleanText(columns.get(i)));
                        break;
                }
            }
            return state;
        }
        return null;
    }
}
