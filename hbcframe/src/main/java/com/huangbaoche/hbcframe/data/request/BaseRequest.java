package com.huangbaoche.hbcframe.data.request;

import com.huangbaoche.hbcframe.data.parser.ImplParser;

import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
/**
 * Created by admin on 2016/2/25.
 */

public class BaseRequest<T> extends RequestParams implements InterfaceRequest {

    private ImplParser parser;
    private T data;

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
