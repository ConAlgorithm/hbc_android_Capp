package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.net.HbcParamsBuilder;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.CheckOpenIdBean;
import com.hugboga.custom.data.bean.UserBean;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParserLogin;
import com.hugboga.custom.data.parser.ParserLoginCheckOpenId;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.Map;
import java.util.TreeMap;

@HttpRequest(path = UrlLibs.GET_ACCESS_TOKEN, builder = HbcParamsBuilder.class)
public class RequestLoginCheckOpenId extends BaseRequest<UserBean> {
    public String code;

    public RequestLoginCheckOpenId(Context context, String code) {
        super(context);
        this.code = code;
    }

    @Override
    public Map<String, Object> getDataMap() {
        TreeMap map = new TreeMap<String, Object>();
        map.put("code", code);
        map.put("source", 1);
        return map;
    }

    @Override
    public ImplParser getParser() {
        return new ParserLogin();
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }
}
