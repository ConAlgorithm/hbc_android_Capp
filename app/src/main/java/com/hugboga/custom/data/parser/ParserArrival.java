package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.ArrivalBean;

import org.json.JSONObject;

/**
 * Created by admin on 2016/3/7.
 */
public class ParserArrival extends ImplParser {

    @Override
    public Object parseObject(JSONObject jsonObj) throws Throwable {
        ArrivalBean bean = new ArrivalBean();
        bean.placeName = jsonObj.optString("placeName");
        bean.placeDetail = jsonObj.optString("placeAddress");
        bean.lng = jsonObj.optDouble("placeLng");
        bean.lat = jsonObj.optDouble("placeLat");
        bean.location = bean.lat+","+bean.lng;
        return null;
    }
}
