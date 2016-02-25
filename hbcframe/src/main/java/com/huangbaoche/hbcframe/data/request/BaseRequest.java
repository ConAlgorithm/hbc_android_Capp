package com.huangbaoche.hbcframe.data.request;

import com.huangbaoche.hbcframe.data.parser.ImplParser;

import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpRequest;
import org.xutils.http.app.DefaultParamsBuilder;

/**
 * Created by admin on 2016/2/25.
 */

public class BaseRequest extends RequestParams implements InterfaceRequest {

    private ImplParser parser;
    private Object data;

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    public ImplParser getParser() {
        return parser;
    }

    public void setParser(ImplParser parser) {
        this.parser = parser;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
