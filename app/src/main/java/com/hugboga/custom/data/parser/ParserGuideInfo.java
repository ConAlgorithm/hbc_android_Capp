package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.OrderGuideInfo;

import org.json.JSONObject;

/**
 * 车导解析器
 * Created by admin on 2016/3/23.
 */
public class ParserGuideInfo extends ImplParser {
    @Override
    public OrderGuideInfo parseObject(JSONObject jsonObj) throws Throwable {
        OrderGuideInfo orderGuideInfo = null;
        if (jsonObj != null && !jsonObj.equals("")) {
            orderGuideInfo = new OrderGuideInfo();
            orderGuideInfo.guideAvatar = jsonObj.optString("guideAvatar");
            orderGuideInfo.guideName = jsonObj.optString("guideName");
            orderGuideInfo.guideID = jsonObj.optString("guideId");
            orderGuideInfo.guideTel = jsonObj.optString("guideTel");
            orderGuideInfo.guideStarLevel = jsonObj.optDouble("guideStarLevel", 0);
            orderGuideInfo.car = jsonObj.optString("guideCar");
            orderGuideInfo.car = jsonObj.optString("CarNumber");
        }
        return orderGuideInfo;
    }
}
