package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.ChatBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParserChatList;

import org.xutils.http.annotation.HttpRequest;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * 聊天列表
 * Created by admin on 2016/3/8.
 */
@HttpRequest(path = UrlLibs.SERVER_IP_CHAT_LIST, builder = NewParamsBuilder.class)
public class RequestChatList extends BaseRequest<ArrayList<ChatBean>> {

    public RequestChatList(Context context) {
        super(context);
        map = new TreeMap<String, Object>();
        map.put("userId", UserEntity.getUser().getUserId(context));
    }

    @Override
    public ImplParser getParser() {
        return new ParserChatList();
    }
}
