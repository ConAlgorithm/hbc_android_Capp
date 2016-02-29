package com.huangbaoche.hbcframe.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;

import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;

import java.util.Map;

/**
 * Created by admin on 2016/2/25.
 */

public abstract class BaseRequest<T> extends RequestParams implements InterfaceRequest {

    protected Context mContext;

    private ImplParser parser;
    private T data;


    public BaseRequest(Context context){
        this.mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    public abstract Map<String,Object> getDataMap();

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
