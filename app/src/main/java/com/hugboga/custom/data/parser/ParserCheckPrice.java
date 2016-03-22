package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.CarBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/21.
 */
public class ParserCheckPrice extends ImplParser {

    public double distance;//预估路程（单位：公里）
    public int interval;//预估时间（单位：分钟）
    public ArrayList<CarBean> carList;

    @Override
    public Object parseObject(JSONObject obj) throws Throwable {
        distance = obj.optDouble("distance",0);
        interval = obj.optInt("estTime", 0);
        JSONArray priceList = obj.optJSONArray("cars");
        CarBean bean;
        if (priceList != null) {
            carList = new ArrayList<CarBean>();
            JSONObject carObj = null;
            for (int i = 0; i < priceList.length(); i++) {
                carObj = priceList.optJSONObject(i);
                bean = new CarBean();
                bean.parser(carObj);
                carList.add(bean);
            }
        }
        return
    }
}
