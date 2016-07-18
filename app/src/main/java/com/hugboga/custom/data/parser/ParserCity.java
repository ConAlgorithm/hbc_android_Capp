package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.CityBean;

import org.json.JSONObject;

/**
 * city解析器
 * Created by admin on 2016/3/23.
 */
public class ParserCity extends ImplParser {
    @Override
    public CityBean parseObject(JSONObject jsonObj) throws Throwable {
        CityBean cityBean = new CityBean();
        cityBean.cityId = jsonObj.optInt("cityId");
        cityBean.areaCode = jsonObj.optString("areaCode");
        cityBean.name = jsonObj.optString("name");
        cityBean.placeName = jsonObj.optString("placeName");
        cityBean.firstLetter = jsonObj.optString("cityInitial");
        cityBean.enName = jsonObj.optString("enName");
        cityBean.location = jsonObj.optString("location");
        cityBean.stayDay = jsonObj.optInt("stayDay");
        cityBean.description = jsonObj.optString("description");
        return cityBean;
    }
}
