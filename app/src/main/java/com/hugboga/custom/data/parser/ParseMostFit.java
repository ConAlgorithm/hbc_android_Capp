package com.hugboga.custom.data.parser;

import com.google.gson.Gson;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.MostFitBean;

import org.json.JSONObject;

/**
 * Created by dyt on 16/5/29.
 */


public class ParseMostFit extends ImplParser {
    @Override
    public MostFitBean parseObject(JSONObject jsonObj) throws Throwable {
        Gson gson = new Gson();
        MostFitBean mostFitBean = gson.fromJson(jsonObj.toString(),MostFitBean.class);
        return mostFitBean;
    }
}
