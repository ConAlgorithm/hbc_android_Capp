package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.ChatOrderBean;

import org.json.JSONObject;

/**
 * Created by admin on 2016/3/8.
 */
public class ParserChatOrder extends ImplParser {
    @Override
    public ChatOrderBean parseObject(JSONObject obj) throws Throwable {
        ChatOrderBean contactBean = new ChatOrderBean();
        contactBean.orderNo = obj.optString("orderNo");
        contactBean.status = obj.optString("status");
        contactBean.orderTypeStr = obj.optString("type");
        contactBean.startAddress = obj.optString("startAddress");
        contactBean.destAddress = obj.optString("destAddress");
        contactBean.serviceTime = obj.optString("serviceTime");
        contactBean.serviceEndTime = obj.optString("serviceEndTime");
        return contactBean;
    }
}
