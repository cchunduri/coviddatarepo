package com.chaitu.dashboard.carona.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class AppUtils {

    public int cleanText(String number) {
        return Integer.parseInt(number.split(" ")[0].trim());
    }

    public int escapeCommas(String num) {
        if (StringUtils.isEmpty(num)) {
            return 0;
        } else {
            return Integer.parseInt(num.replace(",", ""));
        }
    }
}
