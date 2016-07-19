package com.hugboga.custom.data.parser;

import com.google.gson.Gson;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.ADPictureBean;

import org.json.JSONObject;


public class ParseGetAD extends ImplParser {

    @Override
    public ADPictureBean parseObject(JSONObject obj) throws Throwable {
        Gson gson = new Gson();
        ADPictureBean bean = gson.fromJson(obj.toString(),ADPictureBean.class);
        return bean;
    }
}

