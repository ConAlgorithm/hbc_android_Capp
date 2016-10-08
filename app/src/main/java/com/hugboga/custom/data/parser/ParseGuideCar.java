package com.hugboga.custom.data.parser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.GuideCarBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created on 2016/10/8.
 */

public class ParseGuideCar extends ImplParser {
    @Override
    public GuideCarBean parseObject(JSONObject obj) throws Throwable {
        return null;
    }

    @Override
    public Object parseArray(JSONArray array) throws Throwable {
        Gson gson = new Gson();
        ArrayList<GuideCarBean> guideCarList = gson.fromJson(array.toString(),new TypeToken<List<GuideCarBean>>(){}.getType());
        return guideCarList;
    }
}
