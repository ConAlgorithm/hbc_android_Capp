package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.ChatOrderBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 解析IM聊天界面服务中订单
 * Created by ZHZEPHI on 2016/3/26.
 */
public class ParserChatOrders extends ImplParser {
    @Override
    public Object[] parseObject(JSONObject obj) throws Throwable {
        Object[] objs = new Object[2];
        objs[0] = obj.optInt("totalSize");
        //解析IM订单
        JSONArray orderArray = obj.optJSONArray("resultBean");
        List<ChatOrderBean> orders = new ArrayList<>();
        if (orderArray != null) {
            ParserChatOrder mParserChatOrder = new ParserChatOrder();
            for (int j = 0; j < orderArray.length(); j++) {
                orders.add(mParserChatOrder.parseObject(orderArray.optJSONObject(j)));
            }
        }
        objs[1] = orders;
        return objs;
    }
}
