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

    private Context mContext;

    private T data;

    public Map<String,Object> map;

    public BaseRequest(Context context){
        this.mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    public  Map<String,Object> getDataMap(){
        return map;
    }

    public abstract ImplParser getParser() ;

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
