package com.hugboga.custom.data.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 增项费用
 * Created by ZHZEPHI on 2015/7/20.
 */
public class OrderCostApplyInfo implements IBaseBean {

    public String dailyDate;//日期
    public String overTime;//超时时长
    public String overTimePrice;//超时费用
    public String overDistance;//超公里数
    public String overDistancePrice;//超公里费用
    public String overDay;//超天数
    public String overDayPrice;//超天数的费用
    public String prePaymentPrice;//垫付费用

    public void parser(JSONObject jsonObj) throws JSONException {
        dailyDate = jsonObj.optString("dailyDate");
        overTime = jsonObj.optString("overTime");
        overTimePrice = jsonObj.optString("overTimePrice");
        overDistance = jsonObj.optString("overDistance");
        overDistancePrice = jsonObj.optString("overDistancePrice");
        overDay = jsonObj.optString("overDay");
        overDayPrice = jsonObj.optString("overDayPrice");
        prePaymentPrice = jsonObj.optString("prePaymentPrice");
    }
}
