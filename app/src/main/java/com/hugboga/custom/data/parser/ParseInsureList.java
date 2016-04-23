package com.hugboga.custom.data.parser;

import com.google.gson.Gson;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.CarInfoBean;
import com.hugboga.custom.data.bean.InsureBean;

import org.json.JSONObject;

/**
 * Created by dyt on 16/4/23.
 */
public class ParseInsureList extends ImplParser {
    @Override
    public InsureBean parseObject(JSONObject jsonObj) throws Throwable {
        Gson gson = new Gson();
        InsureBean bean =  gson.fromJson(jsonObj.toString(),InsureBean.class);
        return bean;
    }
}
