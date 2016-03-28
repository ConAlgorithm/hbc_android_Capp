package com.huangbaoche.hbcframe.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;

import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;

import java.util.Map;

/**
 *
 * 请求器基类
 * 泛型T是getData 需要的类型，需要与Parser 解析返回的类型相同
 * Created by admin on 2016/2/25.
 */

public abstract class BaseRequest<T> extends RequestParams implements InterfaceRequest {

    private Context mContext;

    private T data;
    /**
     * 上行的参数
     */
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

    /**
     * 动态返回相对的路径，如果不为空，注解的path失效
     * @return
     */
    public String getUrl(){
        return null;
    }
    /**
     * 获取解析器
     * @return 解析器
     */
    public abstract ImplParser getParser() ;

    /**
     * 请求的方法 默认 GET
     * @return
     */
    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }


    /**
     * 获取数据源,返回类型是 构造时 类型
     * @return
     */
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    /**
     * copy 新的自己
     * @return
     */
    public BaseRequest clone(){
        BaseRequest request = new BaseRequest(mContext) {
            @Override
            public ImplParser getParser() {
                return BaseRequest.this.getParser();
            }
            public String getUrl(){
                return BaseRequest.this.getUrl();
            }
            public HttpMethod getHttpMethod() {
                return BaseRequest.this.getHttpMethod();
            }
        };
        request.data = data;
        request.map = map;
    return  request;
    }
}
