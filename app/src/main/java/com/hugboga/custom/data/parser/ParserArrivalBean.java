package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.ArrivalBean;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/3/21.
 */
public class ParserArrivalBean extends ImplParser {
    @Override
    public ArrivalBean parseObject(JSONObject jsonObj) throws Throwable {
        ArrivalBean arrivalBean = new ArrivalBean();
        arrivalBean.placeName = jsonObj.optString("placeName");
        arrivalBean.placeDetail = jsonObj.optString("placeAddress");
        arrivalBean.lng = jsonObj.optDouble("placeLng");
        arrivalBean.lat = jsonObj.optDouble("placeLat");
        arrivalBean.location = arrivalBean.lat+","+arrivalBean.lng;
        return arrivalBean;
    }
}
