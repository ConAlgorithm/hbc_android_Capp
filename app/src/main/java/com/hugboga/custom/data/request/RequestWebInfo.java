package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.net.HbcParamsBuilder;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParserWebInfo;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

/**
 * Created by Administrator on 2016/3/11.
 */
@HttpRequest(path = "", builder = HbcParamsBuilder.class)
public class RequestWebInfo extends BaseRequest<String> {
    public String url;
    public String method;
    public String questBody;
    public String callBack;

    public RequestWebInfo(Context context,String url,String method,String questBody,String callBack) {
        super(context);
        this.url = url;
        this.method = method;
        this.questBody = questBody;
        this.callBack = callBack;
    }

    @Override
    public ImplParser getParser() {
        return new ParserWebInfo();
    }

    @Override
    public HttpMethod getHttpMethod() {
        HttpMethod requestMethod = HttpMethod.valueOf(method.toUpperCase());
        if (requestMethod == null) requestMethod = HttpMethod.POST;
        return requestMethod;
    }

}
