package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.CollectGuideBean;
import com.hugboga.custom.data.bean.CouponBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by qingcha on 16/5/23.
 */
public class ParserCollectGuideList extends ImplParser {

    @Override
    public Object parseObject(JSONObject obj) throws Throwable {
        ArrayList<CollectGuideBean> listDate = new ArrayList<CollectGuideBean>();
        JSONArray jsonArray = obj.optJSONArray("guides");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject segObj = jsonArray.optJSONObject(i);
            ParserCollectGuideBean parserCollectGuideBean = new ParserCollectGuideBean();
            CollectGuideBean collectGuideBean = parserCollectGuideBean.parseObject(segObj);
            if (collectGuideBean != null) {
                listDate.add(collectGuideBean);
            }
        }
        return listDate;
    }
}


