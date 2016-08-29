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
        chatInfo.inBlack = jsonObj.optInt("inBlack");
        chatInfo.isHideMoreBtn = jsonObj.optInt("isHideMoreBtn");
        return chatInfo;
    }

    public String toJsonString(ChatInfo chatInfo){
        JSONObject obj = new JSONObject();
        try {
            obj.put("isChat", chatInfo.isChat);
            obj.put("userId", chatInfo.userId);
            obj.put("imUserId",chatInfo.imUserId);
            obj.put("userAvatar", chatInfo.userAvatar);
            obj.put("title", chatInfo.title);
            obj.put("targetType", chatInfo.targetType);
            obj.put("inBlack", chatInfo.inBlack);
            obj.put("isHideMoreBtn", chatInfo.isHideMoreBtn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }
}
