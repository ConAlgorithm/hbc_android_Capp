package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.ChatBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ChatBeanParser;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Administrator on 2016/11/25.
 */
@HttpRequest(path = UrlLibs.API_SINGLE_CHAT_ORDER_DETAIL, builder = NewParamsBuilder.class)
public class RequestChatOrderDetail extends BaseRequest<ChatBean> {

    private String targetId;
    public RequestChatOrderDetail(Context context,String targetId){
        super(context);
        this.targetId = targetId;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public Map<String, Object> getDataMap() {
        TreeMap map = new TreeMap<String, Object>();
        map.put("targetId", targetId.toUpperCase());
        return map;
    }

    @Override
    public ImplParser getParser() {
        return  new ChatBeanParser();
    }

    @Override
    public String getUrlErrorCode() {
        return "40102";
    }
}
