package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParserChatList;

import org.xutils.http.annotation.HttpRequest;

import java.util.TreeMap;

/**
 * Created by Administrator on 2016/8/26.
 */
@HttpRequest(path = UrlLibs.SERVER_IP_NIMCHAT_LIST, builder = NewParamsBuilder.class)
public class RequestNIMChatList extends BaseRequest {
    public RequestNIMChatList(Context context) {
        super(context);
        map = new TreeMap<String, Object>();
        map.put("userId", UserEntity.getUser().getUserId(context));
    }

    @Override
    public ImplParser getParser() {
        return new ParserChatList();
    }

    @Override
    public String getUrlErrorCode() {
        return "40314";
    }
}
