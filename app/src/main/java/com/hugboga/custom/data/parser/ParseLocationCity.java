package com.hugboga.custom.data.parser;

import com.google.gson.Gson;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.LocationData;

import org.json.JSONObject;

/**
 * Created  on 16/4/12.
 */

public class ParseLocationCity extends ImplParser {
    @Override
    public LocationData parseObject(JSONObject jsonObj) throws Throwable {
        Gson gson = new Gson();
        LocationData locationData = gson.fromJson(jsonObj.toString(),LocationData.class);
//        LocationCity cityBean = new LocationCity();
//        cityBean.cityId = jsonObj.optString("cityId");
//        cityBean.cityName = jsonObj.optString("cityName");
//        cityBean.countryId = jsonObj.optString("countryId");
//        cityBean.countryName = jsonObj.optString("countryName");
        return locationData;
    }
}