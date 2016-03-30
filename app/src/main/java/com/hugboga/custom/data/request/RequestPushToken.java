package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.net.HbcParamsBuilder;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.net.UrlLibs;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.TreeMap;

/**
 * Created by admin on 2016/3/29.
 */

@HttpRequest(path = UrlLibs.SERVER_IP_PUSH_TOKEN ,builder = HbcParamsBuilder.class)
public class RequestPushToken extends BaseRequest {

    public RequestPushToken(Context context,String pushToken,String realToken,String appVersion,String deviceId,String osVersion) {
        super(context);
        map = new TreeMap();
        map.put("pushToken",pushToken);
        map.put("realToken",realToken);
        map.put("os","android");
        map.put("appVersion",appVersion);
        map.put("deviceId",deviceId);
        map.put("osVersion",osVersion);
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
