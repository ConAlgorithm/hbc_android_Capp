package com.hugboga.custom.data.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ZHZEPHI on 2015/7/20.
 */
public class OrderPriceInfo implements IBaseBean {

    public double orderPrice;
    public double shouldPay;
    public double actualPay;
    public double refundPrice;		// 退款金额
    public double refundablePrice;	// 可退款金额
    public double checkInPrice;
    public double cancelFee;//退改费用

    public void parser(JSONObject jsonObj) throws JSONException {
        if(jsonObj==null)return;
        orderPrice = jsonObj.optDouble("orderPrice");
        shouldPay = jsonObj.optDouble("shouldPay");
        actualPay = jsonObj.optDouble("actualPay");
        checkInPrice = jsonObj.optDouble("checkInPrice");
        refundPrice = jsonObj.optDouble("refundPrice");
        refundablePrice = jsonObj.optDouble("refundablePrice");
        cancelFee = jsonObj.optDouble("cancelFee",0);
    }
}
