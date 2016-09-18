package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/8/26.
 */
@HttpRequest(path = UrlLibs.SERVER_IP_NIM_UPDATE, builder = NewParamsBuilder.class)
public class RequestNIMClear extends BaseRequest {


    public RequestNIMClear(Context context, String targetId, String targetType) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("targetId", targetId);
        map.put("targetType", targetType);
    }

    @Override
    public ImplParser getParser() {
        return null;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public String getUrlErrorCode() {
        return "40060";
    }
}
