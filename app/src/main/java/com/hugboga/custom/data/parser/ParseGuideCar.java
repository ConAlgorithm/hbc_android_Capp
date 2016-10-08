package com.hugboga.custom.data.parser;

import com.google.gson.Gson;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.GuideCarBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created on 2016/10/8.
 */

public class ParseGuideCar extends ImplParser {
    @Override
    public GuideCarBean parseObject(JSONObject obj) throws Throwable {
        return null;
    }

    @Override
    public ArrayList<GuideCarBean> parseArray(JSONArray array) throws Throwable {
        Gson gson = new Gson();
        ArrayList<GuideCarBean> guideCarList = gson.fromJson(array.toString(),ArrayList.class);
        return guideCarList;
    }
}
