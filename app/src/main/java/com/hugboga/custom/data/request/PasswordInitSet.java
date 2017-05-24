package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.Map;
import java.util.TreeMap;


/**
 * Created by zhangqiang on 17/5/20.
 */
@HttpRequest(path = UrlLibs.SERVER_IP_PASSWORD_SET, builder = NewParamsBuilder.class)
public class PasswordInitSet extends BaseRequest {

    String pwd;
    public PasswordInitSet(Context context,String pwd) {
        super(context);
        this.pwd = pwd;
    }

    @Override
    public Map<String, Object> getDataMap() {
        TreeMap map = new TreeMap<String, Object>();
        map.put("password", pwd);
        return map;
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
        return "410146";
    }
}
