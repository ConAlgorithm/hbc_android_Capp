package com.hugboga.custom.data.bean;

import org.json.JSONException;
import org.json.JSONObject;

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

    }
}
