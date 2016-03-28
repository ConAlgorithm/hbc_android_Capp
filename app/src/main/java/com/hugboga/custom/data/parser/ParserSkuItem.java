package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.PoiBean;
import com.hugboga.custom.data.bean.SkuItemBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 2016/3/3.
 */
public class ParserSkuItem extends ImplParser {
    @Override
    public SkuItemBean parseObject(JSONObject obj) throws Throwable {
        SkuItemBean bean = new SkuItemBean();
        bean.arrCityId = obj.optInt("arrCityId");
        bean.arrCityName = obj.optString("arrCityName");
        bean.depCityId = obj.optInt("depCityId");
        bean.depCityName = obj.optString("depCityName");
        bean.goodsMinPrice = obj.optString("goodsMinPrice");
        bean.goodsName = obj.optString("goodsName");
        bean.goodsNo = obj.optString("goodsNo");
        bean.goodsPicture = obj.optString("goodsPicture");
        bean.goodsType = obj.optInt("goodsType");
        bean.saleAmount = obj.optInt("saleAmount");
        bean.salePoints = obj.optString("salePoints");
        bean.guideAmount = obj.optInt("guideAmount");
        bean.daysCount = obj.optInt("daysCount");
        bean.skuDetailUrl = obj.optString("skuDetailUrl");
        bean.shareURL = obj.optString("shareURL");
        bean.passCityList = new ArrayList<CityBean>();
        JSONArray passCityJArray = obj.optJSONArray("passbyCitys");
        CityBean cityBean;
        if (passCityJArray != null) {
            JSONObject cityObject;
            for (int i = 0; i < passCityJArray.length(); i++) {
                cityObject = passCityJArray.optJSONObject(i);
                cityBean = new CityBean();
                cityBean.cityId = cityObject.optInt("passbyId");
                cityBean.name = cityObject.optString("passbyName");
                bean.passCityList.add(cityBean);
            }
        }
        JSONArray poiJArray = obj.optJSONArray("passbyPois");
        bean.passPoiList = new ArrayList<>();
        PoiBean poiBean;
        if (poiJArray != null) {
            JSONObject poiObject;
            bean.passPoiListStr = poiJArray.toString();
            for (int i = 0; i < poiJArray.length(); i++) {
                poiObject = poiJArray.optJSONObject(i);
                poiBean = new PoiBean();
                poiBean.id = poiObject.optInt("passbyId");
                poiBean.placeName = poiObject.optString("passbyName");
                bean.passPoiList.add(poiBean);
            }
        }
        return bean;
    }
}
