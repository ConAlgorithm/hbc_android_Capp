package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.Map;
import java.util.TreeMap;

@HttpRequest(path = UrlLibs.REMOVE_BLACK, builder = NewParamsBuilder.class)
public class RequestUnBlackMan extends BaseRequest {
    public String targetUserId;


    public RequestUnBlackMan(Context context, String targetUserId) {
        super(context);
        this.targetUserId = targetUserId;

    }

    @Override
    public Map<String, Object> getDataMap() {
        TreeMap map = new TreeMap<String, Object>();
        map.put("targetUserId", targetUserId);
        return map;
    }

    @Override
    public ImplParser getParser() {
        return null;
    }

//    {
//        "message": "SUCCESS",
//            "status": 200
//}
    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public String getUrlErrorCode() {
        return "40091";
    }
}