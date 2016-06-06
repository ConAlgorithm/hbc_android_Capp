package com.hugboga.custom.data.parser;

import com.google.gson.Gson;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.DeductionBean;

import org.json.JSONObject;

public class ParseDeduction extends ImplParser {

    @Override
    public DeductionBean parseObject(JSONObject obj) throws Throwable {
        Gson gson = new Gson();
        DeductionBean bean = gson.fromJson(obj.toString(),DeductionBean.class);
        return bean;
    }
}

