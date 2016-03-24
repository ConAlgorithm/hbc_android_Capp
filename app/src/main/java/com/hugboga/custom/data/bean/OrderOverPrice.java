package com.hugboga.custom.data.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZHZEPHI on 2015/7/20.
 */
public class OrderOverPrice implements IBaseBean {

    public boolean alreadyPay;
    public Integer applyPrice;
    public Integer shouldPay;
    public Integer actualPay;
    public String payMode;
    public List<OrderCostApplyInfo> orderCostApplyInfos;

    public void parser(JSONObject jsonObj) throws JSONException {
        if(jsonObj==null)return;
        alreadyPay = jsonObj.optBoolean("alreadyPay");
        applyPrice = jsonObj.optInt("applyPrice");
        shouldPay = jsonObj.optInt("shouldPay");
        actualPay = jsonObj.optInt("actualPay");
        payMode = jsonObj.optString("payMode");
        JSONArray costObj = jsonObj.optJSONArray("costApplyInfo");
        if(costObj!=null) {
            orderCostApplyInfos= new ArrayList<OrderCostApplyInfo>();
            for (int i = 0; i < costObj.length(); i++) {
                OrderCostApplyInfo orderCostApplyInfo = new OrderCostApplyInfo();
                orderCostApplyInfo.parser(costObj.optJSONObject(i));
                orderCostApplyInfos.add(orderCostApplyInfo);
            }
        }
    }
}
