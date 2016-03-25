package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.PoiBean;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/3/21.
 */
public class ParserPoiBean extends ImplParser {
    @Override
    public PoiBean parseObject(JSONObject jsonObj) throws Throwable {
        PoiBean poiBean = new PoiBean();
        poiBean.placeName = jsonObj.optString("placeName");
        poiBean.placeDetail = jsonObj.optString("placeAddress");
        poiBean.lng = jsonObj.optDouble("placeLng");
        poiBean.lat = jsonObj.optDouble("placeLat");
        poiBean.location = poiBean.lat+","+ poiBean.lng;
        return poiBean;
    }
}
