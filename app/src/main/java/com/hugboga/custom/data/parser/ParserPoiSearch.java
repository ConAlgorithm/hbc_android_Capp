package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.NewPoiBean;
import com.hugboga.custom.data.bean.PoiBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/21.
 */
public class ParserPoiSearch extends ImplParser {
    public NewPoiBean newPoiBean = null;
    @Override
    public Object parseObject(JSONObject obj) throws Throwable {
        JSONArray contentArray = obj.optJSONArray("places");
        newPoiBean = new NewPoiBean();
        newPoiBean.count = obj.optInt("count");
        if (contentArray != null) {
            newPoiBean.listDate = new ArrayList<PoiBean>();
            JSONObject segObj;
            for (int i = 0; i < contentArray.length(); i++) {
                segObj = contentArray.optJSONObject(i);
                ParserPoiBean parserPoiBean = new ParserPoiBean();
                PoiBean poiBean = parserPoiBean.parseObject(segObj);
                if (poiBean != null) {
                    newPoiBean.listDate.add(poiBean);
                }
            }
        }
        return newPoiBean;
    }
}
