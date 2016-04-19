package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.AirPort;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/22.
 */
public class ParserAirPort extends ImplParser {
    @Override
    public Object parseObject(JSONObject obj) throws Throwable {
        return null;
    }

    @Override
    public Object parseArray(JSONArray array) throws Exception {
        ArrayList<AirPort> airportList = new ArrayList<AirPort>();
        JSONObject obj;
        AirPort bean;
        for (int i = 0; i < array.length(); i++) {
            obj = array.optJSONObject(i);
            ParserAirPortBean parserAirPortBean = new ParserAirPortBean();
            bean = parserAirPortBean.parseObject(obj);
            airportList.add(bean);
        }
        return airportList;
    }
}
