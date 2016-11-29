package com.hugboga.custom.data.parser;

import com.google.gson.Gson;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.BarginBean;
import com.hugboga.custom.data.bean.ChatBean;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/11/25.
 */
public class ChatBeanParser extends ImplParser {

    @Override
    public ChatBean parseObject(JSONObject obj) throws Throwable {
        ChatBean bean = new ChatBean();
        bean.parseObject(obj);
        return bean;
    }
}
