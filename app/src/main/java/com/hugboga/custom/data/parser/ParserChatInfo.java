package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.ChatInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 2016/3/10.
 */
public class ParserChatInfo extends ImplParser {
    @Override
    public ChatInfo parseObject(JSONObject jsonObj) throws Throwable {
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.isChat = jsonObj.optBoolean("isChat");
        chatInfo.userId = jsonObj.optString("userId");
        chatInfo.userAvatar = jsonObj.optString("userAvatar");
        chatInfo.title = jsonObj.optString("title");
        chatInfo.targetType = jsonObj.optString("targetType");
        return chatInfo;
    }

    public String toJsonString(ChatInfo chatInfo){
        JSONObject obj = new JSONObject();
        try {
            obj.put("isChat", chatInfo.isChat);
            obj.put("userId", chatInfo.userId);
            obj.put("userAvatar", chatInfo.userAvatar);
            obj.put("title", chatInfo.title);
            obj.put("targetType", chatInfo.targetType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }
}
