package com.hugboga.custom.data.parser;

import com.google.gson.Gson;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.CarInfoBean;

import org.json.JSONObject;

/**
 * Created  on 16/4/16.
 */
public class ParseGetCarInfo extends ImplParser {
    @Override
    public CarInfoBean parseObject(JSONObject jsonObj) throws Throwable {
        Gson gson = new Gson();
        CarInfoBean bean =  gson.fromJson(jsonObj.toString(),CarInfoBean.class);
        return bean;
    }
}
