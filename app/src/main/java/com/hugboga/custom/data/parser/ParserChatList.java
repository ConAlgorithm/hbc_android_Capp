package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.ChatBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 2016/3/8.
 */
public class ParserChatList extends ImplParser {
    @Override
    public ArrayList<ChatBean> parseObject(JSONObject obj) throws Throwable {
        JSONArray jsonArray = obj.optJSONArray("resultBean");
        ArrayList<ChatBean> list = new ArrayList<ChatBean>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            ChatBean chatBean = new ChatBean();
            chatBean.targetAvatar = jsonObject.optString("targetAvatar");
            chatBean.targetName = jsonObject.optString("targetName");
            chatBean.targetId = jsonObject.optString("targetId");
            chatBean.targetType = jsonObject.optString("targetType");
            chatBean.imCount = jsonObject.optString("imCount");
            chatBean.message = jsonObject.optString("lastMsg");
            chatBean.timeStr = jsonObject.optString("lastTime");
            chatBean.userId = jsonObject.optString("userId");
            chatBean.userType = jsonObject.optString("userType");
            //解析IM订单
            JSONArray orderArray = jsonObject.optJSONArray("orderInfo");
            chatBean.orders = new ArrayList<>();
            if (orderArray != null) {
                ParserChatOrder mParserChatOrder = new ParserChatOrder();
                for (int j = 0; j < orderArray.length(); j++) {
                    chatBean.orders.add(mParserChatOrder.parseObject(orderArray.optJSONObject(j)));
                }
            }
            list.add(chatBean);
        }
        return list;
    }

}
