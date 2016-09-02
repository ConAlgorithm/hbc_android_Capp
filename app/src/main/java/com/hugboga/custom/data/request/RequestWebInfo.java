package com.hugboga.custom.data.request;

import android.content.Context;
import android.net.Uri;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.net.WebParamsBuilder;
import com.hugboga.custom.data.parser.ParserWebInfo;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.TreeMap;

/**
 * webView 的请求器
 * Created by Administrator on 2016/3/11.
 */
@HttpRequest(path = "", builder = WebParamsBuilder.class)
public class RequestWebInfo extends BaseRequest<String> {
    public String url;
    public String method;
    public String questBody;
    public String successCallBack;
    public String failCallBack;

    public RequestWebInfo(Context context, String url, String method, String questBody, String successCallBack, String failCallBack) {
        super(context);
        this.url = url;
        this.method = method;
        this.questBody = questBody;
        this.successCallBack = successCallBack;
        this.failCallBack = failCallBack;
        map = getQueryParameter(questBody);
    }

    @Override
    public ImplParser getParser() {
        return new ParserWebInfo();
    }

    @Override
    public String getUrlErrorCode() {
        return "40099";
    }

    @Override
    public HttpMethod getHttpMethod() {
        HttpMethod requestMethod = HttpMethod.valueOf(method.toUpperCase());
        if (requestMethod == null) requestMethod = HttpMethod.POST;
        return requestMethod;
    }

    public String getUrl() {
        return url;
    }


    public TreeMap<String, Object> getQueryParameter(String params) {
        TreeMap<String, Object> map = new TreeMap<>();
        int start = 0;
        do {
            int next = params.indexOf('&', start);
            int end = (next == -1) ? params.length() : next;

            int separator = params.indexOf('=', start);
            if (separator > end || separator == -1) {
                separator = end;
            }

            String name = params.substring(start, separator);
            String value = params.substring(separator + 1, end);
            map.put(name, Uri.decode(value));
            start = end + 1;
        } while (start < params.length());

        return map;
    }


}
