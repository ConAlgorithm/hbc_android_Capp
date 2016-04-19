package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.CarBean;
import com.hugboga.custom.data.bean.CarListBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/21.
 */
public class ParserCheckPrice extends ImplParser {

//    public double distance;//预估路程（单位：公里）
//    public int interval;//预估时间（单位：分钟）
//    public ArrayList<CarBean> carList;

    @Override
    public CarListBean parseObject(JSONObject obj) throws Throwable {
        CarListBean carListBean = new CarListBean();
        carListBean.distance = obj.optDouble("distance", 0);
        carListBean.interval = obj.optInt("estTime", 0);
        JSONArray priceList = obj.optJSONArray("cars");
        CarBean bean;
        if (priceList != null) {
            carListBean.carList = new ArrayList<CarBean>();
            JSONObject jsonObj = null;
            for (int i = 0; i < priceList.length(); i++) {
                jsonObj = priceList.optJSONObject(i);
                bean = new CarBean();
                bean.carType = jsonObj.optInt("carType");
                bean.carSeat = jsonObj.optInt("seatCategory");
                bean.desc = jsonObj.optString("carDesc");
                bean.models = jsonObj.optString("models");
                bean.pricemark = jsonObj.optString("pricemark");
                bean.urgentCutdownTip = jsonObj.optString("urgentCutdownTip");
                bean.originalPrice = jsonObj.optInt("price");
                bean.checkInPrice = jsonObj.optInt("checkInPrice", 0);
                bean.urgentFlag = jsonObj.optInt("urgentFlag", 0);
                carListBean.carList.add(bean);
            }
        }

        return carListBean;
    }
}
