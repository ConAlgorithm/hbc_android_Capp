package com.huangbaoche.hbcframe.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;

import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 *
 * 请求器基类
 * 泛型T是getData 需要的类型，需要与Parser 解析返回的类型相同
 * Created by admin on 2016/2/25.
 */

public abstract class BaseRequest<T> extends RequestParams implements InterfaceRequest {

    public static final int ERROR_TYPE_DEFAULT = 1;            // toast弹出错误提示
    public static final int ERROR_TYPE_SHOW_DIALOG = 2;        // dialog弹出错误提示
    public static final int ERROR_TYPE_IGNORE = 3;             // 传到下层处理
    public static final int ERROR_TYPE_PROCESSED = 4;          // 已处理

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

    public String bodyEntity;

    public Map<String, List<String>> responseHeaders;

    public int errorType = ERROR_TYPE_DEFAULT;

    public void setErrorType(int errorType) {
        this.errorType = errorType;
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
     * 需要重新build 参数时，调用此方法；比如列表翻页
     */
    public void needRebuild(){
        try {
            for(Class cls =this.getClass();cls !=Object.class;cls = cls.getSuperclass()){
                if(cls==RequestParams.class) {
                    Field field = cls.getDeclaredField("buildUri");
                    field.setAccessible(true);
                    field.set(this, "");
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public int getOffset() {
        int offset = 0;
        if (map != null && map.containsKey("offset") && map.get("offset") != null) {
            Object offsetObj = map.get("offset");
            if (offsetObj instanceof String) {
                try {
                    offset = Integer.valueOf((String)offsetObj);
                } catch(Exception e) {
                    offset = 0;
                }
            } else if (offsetObj instanceof Integer) {
                offset = (Integer) offsetObj;
            }
        }
        return offset;
    }

}
