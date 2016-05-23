package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.LocationCity;

import org.json.JSONObject;

/**
 * Created  on 16/5/23.
 */
public class ParseLocationCityV10 extends ImplParser {
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