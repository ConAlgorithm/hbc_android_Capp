package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.utils.LogUtils;

import org.json.JSONObject;
import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Administrator on 2016/8/26.
 */

@HttpRequest(path = UrlLibs.REMOVE_NIM_CHAT, builder = NewParamsBuilder.class)
public class RequestNIMRemoveChat extends BaseRequest {

    public String removeUserId;


    public RequestNIMRemoveChat(Context context, String removeUserId) {
        super(context);
        this.removeUserId = removeUserId;

    }

    @Override
    public Map<String, Object> getDataMap() {
        TreeMap map = new TreeMap<String, Object>();
        map.put("removeUserId", removeUserId);
        return map;
    }

    @Override
    public ImplParser getParser() {
        return new ImplParser() {
            @Override
            public Object parseObject(JSONObject obj) throws Throwable {
                LogUtils.json(obj.toString());
                return null;
            }
        };
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public String getUrlErrorCode() {
        return "40061";
    }
}
