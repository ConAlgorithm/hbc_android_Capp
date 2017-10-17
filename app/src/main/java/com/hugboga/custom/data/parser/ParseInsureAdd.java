package com.hugboga.custom.data.parser;

import com.google.gson.Gson;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.InsureResultBean;

import org.json.JSONObject;

/**
 * Created  on 16/4/23.
 */
public class ParseInsureAdd extends ImplParser {
    @Override
    public InsureResultBean parseObject(JSONObject jsonObj) throws Throwable {
        Gson gson = new Gson();
        InsureResultBean bean =  gson.fromJson(jsonObj.toString(),InsureResultBean.class);
        return bean;
    }
}