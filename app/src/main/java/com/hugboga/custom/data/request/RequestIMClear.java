package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.net.HbcParamsBuilder;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.net.UrlLibs;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * 清空IM消息接口
 * Created by ZHZEPHI on 2016/3/26.
 */
@HttpRequest(path = UrlLibs.SERVER_IP_IM_UPDATE, builder = HbcParamsBuilder.class)
public class RequestIMClear extends BaseRequest {

    public RequestIMClear(Context context, String targetId, String targetType) {
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
}
