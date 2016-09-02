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
 * Created by Administrator on 2016/3/14.
 */
@HttpRequest(path = UrlLibs.SERVER_IP_FEEDBACK_SAVE, builder = NewParamsBuilder.class)
public class RequestCallBack extends BaseRequest {
    public RequestCallBack(Context context, String content) {
        super(context);
        map = new HashMap<String, Object>();
        try {
            map.put("content", content);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        return "40011";
    }
}
