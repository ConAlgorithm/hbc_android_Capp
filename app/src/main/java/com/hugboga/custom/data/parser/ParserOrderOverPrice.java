package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.OrderCostApplyInfo;
import com.hugboga.custom.data.bean.OrderOverPrice;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 2016/3/24.
 */
public class ParserOrderOverPrice extends ImplParser {
    @Override
    public Object parseObject(JSONObject jsonObj) throws Throwable {
        OrderOverPrice orderOverPrice = new OrderOverPrice();
        if(jsonObj==null)return orderOverPrice;
        orderOverPrice.alreadyPay = jsonObj.optBoolean("alreadyPay");
        orderOverPrice.applyPrice = jsonObj.optInt("applyPrice");
        orderOverPrice.shouldPay = jsonObj.optInt("shouldPay");
        orderOverPrice.actualPay = jsonObj.optInt("actualPay");
        orderOverPrice.payMode = jsonObj.optString("payMode");
        JSONArray costObj = jsonObj.optJSONArray("costApplyInfo");
        if(costObj!=null) {
            orderOverPrice.orderCostApplyInfos= new ArrayList<OrderCostApplyInfo>();
            ParserApplyInfo parserApplyInfo = new ParserApplyInfo();
            for (int i = 0; i < costObj.length(); i++) {
                orderOverPrice.orderCostApplyInfos.add(parserApplyInfo.parseObject(costObj.optJSONObject(i)));
            }
        }
        return null;
    }
}
