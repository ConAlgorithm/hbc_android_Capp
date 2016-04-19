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
 * Created by Administrator on 2016/3/12.
 */
@HttpRequest(path = UrlLibs.SERVER_IP_LOGOUT, builder = HbcParamsBuilder.class)
public class RequestLogout extends BaseRequest {

    public RequestLogout(Context context) {
        super(context);
        map = new HashMap<String, Object>();
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
