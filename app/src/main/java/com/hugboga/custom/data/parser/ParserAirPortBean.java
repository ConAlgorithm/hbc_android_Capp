package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.AirPort;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/3/22.
 */
public class ParserAirPortBean extends ImplParser {
    @Override
    public AirPort parseObject(JSONObject jsonObj) throws Exception {
        AirPort airPortBean = new AirPort();
        airPortBean.airportId = jsonObj.optInt("airportId");
        airPortBean.cityId = jsonObj.optInt("cityId");
        airPortBean.airportName = jsonObj.optString("airportName");
        airPortBean.areaCode = jsonObj.optString("areaCode");
        airPortBean.cityFirstLetter = jsonObj.optString("cityInitial");
        airPortBean.cityName = jsonObj.optString("cityName");
        airPortBean.placeName = jsonObj.optString("placeName");
        airPortBean.airportCode = jsonObj.optString("airportCode");
        airPortBean.location = jsonObj.optString("location");
        return airPortBean;
    }
}
