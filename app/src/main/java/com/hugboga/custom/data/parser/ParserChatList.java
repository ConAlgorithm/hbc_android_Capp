package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.ChatBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/3/8.
 */
public class ParserChatList extends ImplParser {
    @Override
    public Object[] parseObject(JSONObject obj) throws Throwable {
        Object[] objs = new Object[2];
        objs[0] = obj.optInt("totalSize");
        JSONArray jsonArray = obj.optJSONArray("resultBean");
        List<ChatBean> list = new ArrayList<ChatBean>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            ChatBean chatBean = new ChatBean();
            chatBean.parseObject(jsonObject);
            list.add(chatBean);
        }
        objs[1] = list;
        return objs;
    }

}
