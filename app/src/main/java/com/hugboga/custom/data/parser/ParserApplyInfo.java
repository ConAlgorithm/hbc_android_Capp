package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.OrderCostApplyInfo;

import org.json.JSONObject;

/**
 * Created by admin on 2016/3/24.
 */
public class ParserApplyInfo extends ImplParser {
    @Override
    public OrderCostApplyInfo parseObject(JSONObject jsonObj) throws Throwable {
        OrderCostApplyInfo applyInfo = new OrderCostApplyInfo();
        applyInfo.dailyDate = jsonObj.optString("dailyDate");
        applyInfo.overTime = jsonObj.optString("overTime");
        applyInfo.overTimePrice = jsonObj.optString("overTimePrice");
        applyInfo.overDistance = jsonObj.optString("overDistance");
        applyInfo.overDistancePrice = jsonObj.optString("overDistancePrice");
        applyInfo.overDay = jsonObj.optString("overDay");
        applyInfo.overDayPrice = jsonObj.optString("overDayPrice");
        applyInfo.prePaymentPrice = jsonObj.optString("prePaymentPrice");
        return applyInfo;
    }
}
