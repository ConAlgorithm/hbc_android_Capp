package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.HomeBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 2016/3/2.
 */
public class ParserHome extends ImplParser {

    @Override
    public ArrayList<HomeBean> parseObject(JSONObject obj) throws Throwable {
        ArrayList<HomeBean> dataList = new ArrayList<>();
        JSONArray array = obj.optJSONArray("listData");
        if (array != null) {
            HomeBean bean;
            for (int i = 0; i < array.length(); i++) {
                bean = new HomeBean();
                JSONObject item = array.optJSONObject(i);
                bean.cityId = item.optString("cityId");
                bean.mainTitle = item.optString("mainTitle");
                bean.subTitle = item.optString("subTitle");
                bean.picture = item.optString("picture");
                dataList.add(bean);
            }
        }
        return dataList;
    }
}
