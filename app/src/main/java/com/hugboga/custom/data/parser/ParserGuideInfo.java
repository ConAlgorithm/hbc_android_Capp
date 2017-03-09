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
            orderGuideInfo.guideCar = jsonObj.optString("guideCar");
            orderGuideInfo.carNumber = jsonObj.optString("carNumber");
            orderGuideInfo.storeStatus = jsonObj.optInt("storeStatus", 0);
            orderGuideInfo.guideCarId = jsonObj.optString("guideCarId");
            orderGuideInfo.flag = jsonObj.optString("flag");
            orderGuideInfo.timediff = jsonObj.optInt("timediff", 0);
            orderGuideInfo.timezone = jsonObj.optInt("timezone", 0);
            orderGuideInfo.cityName = jsonObj.optString("cityName");
            orderGuideInfo.cityId = jsonObj.optInt("cityId", 0);
            orderGuideInfo.countryName = jsonObj.optString("countryName");
            orderGuideInfo.countryId = jsonObj.optInt("countryId", 0);
            orderGuideInfo.contact = jsonObj.optString("contact");
        }

        return orderGuideInfo;
    }
}
