package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.LocationCity;

import org.json.JSONObject;

/**
 * Created by dyt on 16/4/12.
 */

public class ParseLocationCity extends ImplParser {
    @Override
    public LocationCity parseObject(JSONObject jsonObj) throws Throwable {
        LocationCity cityBean = new LocationCity();
        cityBean.cityId = jsonObj.optString("cityId");
        cityBean.cityName = jsonObj.optString("cityName");
        cityBean.countryId = jsonObj.optString("countryId");
        cityBean.countryName = jsonObj.optString("countryName");
        return cityBean;
    }
}