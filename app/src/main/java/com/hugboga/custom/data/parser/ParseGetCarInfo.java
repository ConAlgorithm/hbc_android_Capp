package com.hugboga.custom.data.parser;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.CarInfoBean;
import com.hugboga.custom.data.bean.UserCouponBean;

import org.json.JSONObject;

/**
 * Created by dyt on 16/4/16.
 */
public class ParseGetCarInfo extends ImplParser {
    @Override
    public CarInfoBean parseObject(JSONObject jsonObj) throws Throwable {
        Gson gson = new Gson();
        CarInfoBean bean =  gson.fromJson(jsonObj.toString(),CarInfoBean.class);
        return bean;
    }
}
