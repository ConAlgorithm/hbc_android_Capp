package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.OrderBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by qingcha on 17/3/13.
 */

public class ParserSubOrderDetail extends ImplParser {

    @Override
    public OrderBean.SubOrderDetail parseObject(JSONObject obj) throws Throwable {
        OrderBean.SubOrderDetail subOrderDetail = new OrderBean.SubOrderDetail();
        subOrderDetail.totalCount = obj.optInt("totalCount");
        JSONArray subOrderList = obj.optJSONArray("subOrderList");
        if (subOrderList != null && subOrderList.length() > 0) {
            subOrderDetail.subOrderList = new ArrayList<OrderBean>(subOrderList.length());
            ParserOrder parserOrder = new ParserOrder();
            for (int i = 0; i < subOrderList.length(); i++) {
                subOrderDetail.subOrderList.add(parserOrder.parseObject(subOrderList.optJSONObject(i)));
            }
        }
        return subOrderDetail;
    }

}
