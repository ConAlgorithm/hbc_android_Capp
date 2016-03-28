package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.FlightBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/22.
 */
public class ParserFlightByNo extends ImplParser {
    @Override
    public Object parseObject(JSONObject obj) throws Throwable {
        return null;
    }

    @Override
    public Object parseArray(JSONArray array) throws Exception {
        ArrayList<FlightBean> listDate;
        JSONObject segObj;
        FlightBean bean;
        listDate = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            segObj = array.optJSONObject(i);
            ParserFlightBean parserFlightBean = new ParserFlightBean();
            bean = parserFlightBean.parseObject(segObj);
            listDate.add(bean);
        }
        return listDate;
    }
}
