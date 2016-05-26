package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.CarBean;
import com.hugboga.custom.data.bean.CarListBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 选车 查价 列表解析器
 * Created by admin on 2016/3/20.
 */
public class ParserCarList extends ImplParser {
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
                bean.originalPrice = jsonObj.optInt("price");
                bean.checkInPrice = jsonObj.optInt("checkInPrice", 0);
                bean.urgentFlag = jsonObj.optInt("urgentFlag", 0);

                bean.capOfLuggage = jsonObj.optInt("capOfLuggage", 0);
                bean.capOfPerson = jsonObj.optInt("capOfPerson", 0);
                bean.price = jsonObj.optInt("price", 0);
                bean.localPrice = jsonObj.optInt("localPrice", 0);
                carListBean.carList.add(bean);
            }
        }

        return carListBean;
    }
}
