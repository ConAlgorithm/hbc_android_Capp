package com.hugboga.custom.data.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 2015/7/22.
 */
public class CarBean implements IBaseBean {

    public int id;
    public int carType;//类型
    public int carSeat;//座位
    public String desc;//描述
    public String models;//代表车型
    public int originalPrice;//原价
    public int checkInPrice;//促销价
    public String pricemark;//价格戳
    public int urgentFlag;//是否急单，1是，0非

    @Override
    public void parser(JSONObject jsonObj) throws JSONException {
        carType = jsonObj.optInt("carType");
        carSeat = jsonObj.optInt("seatCategory");
        desc = jsonObj.optString("carDesc");
        models = jsonObj.optString("models");
        pricemark = jsonObj.optString("pricemark");
        originalPrice = jsonObj.optInt("price");
        checkInPrice = jsonObj.optInt("checkInPrice",0);
        urgentFlag = jsonObj.optInt("urgentFlag",0);
    }
}
