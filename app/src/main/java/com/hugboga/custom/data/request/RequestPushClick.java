package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.net.HbcParamsBuilder;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.net.UrlLibs;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.net.URL;
import java.util.TreeMap;

/**
 * Created by admin on 2016/3/29.
 */
@HttpRequest(path = UrlLibs.SERVER_IP_PUSH_CLICK,builder = HbcParamsBuilder.class)
public class RequestPushClick extends BaseRequest {
    public RequestPushClick(Context context,String pushId) {
        super(context);
        map = new TreeMap();
        map.put("pushId",pushId);
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.POST;
    }

    @Override
    public ImplParser getParser() {
        return null;
    }
}
