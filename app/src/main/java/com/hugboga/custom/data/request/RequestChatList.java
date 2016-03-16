package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.net.HbcParamsBuilder;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.ChatBean;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParserChatList;

import org.xutils.http.annotation.HttpRequest;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * 聊天列表
 * Created by admin on 2016/3/8.
 */
@HttpRequest(path = UrlLibs.SERVER_IP_CHAT_LIST,builder = HbcParamsBuilder.class)
public class RequestChatList extends BaseRequest<ArrayList<ChatBean>> {


    public RequestChatList(Context context,int offset,int limit) {
        super(context);
        map = new TreeMap<String,Object>();
        map.put("offset",offset);
        map.put("limit",limit);
    }


    @Override
    public ImplParser getParser() {
        return new ParserChatList();
    }
}