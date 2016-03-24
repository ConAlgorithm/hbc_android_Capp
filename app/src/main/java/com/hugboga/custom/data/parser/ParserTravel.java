package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.OrderBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 2016/3/23.
 */
public class ParserTravel extends ImplParser {
    @Override
    public ArrayList<OrderBean> parseObject(JSONObject obj) throws Throwable {

        JSONArray jsonArray= obj.optJSONArray("resultBean");
        ArrayList<OrderBean> listData = new ArrayList<OrderBean>();
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject segObj = jsonArray.optJSONObject(i);
                ParserOrder parserOrder = new ParserOrder();
                listData.add(parserOrder.parseObject(segObj));
            }
        }
        return listData;
    }
}
