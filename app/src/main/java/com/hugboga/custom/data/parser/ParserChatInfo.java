package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.ChatInfo;

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
        return chatInfo;
    }
}
