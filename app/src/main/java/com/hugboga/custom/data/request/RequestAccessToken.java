package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.net.HbcParamsBuilder;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.AccessTokenBean;
import com.hugboga.custom.data.bean.UserBean;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParserAccessToken;
import com.hugboga.custom.data.parser.ParserLogin;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;
import org.xutils.http.app.DefaultParamsBuilder;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by admin on 2016/3/8.
 */
@HttpRequest(path = UrlLibs.GET_ACCESS_TOKEN, builder = DefaultParamsBuilder.class,host = "https://api.weixin.qq.com")
public class RequestAccessToken extends BaseRequest<AccessTokenBean> {
    public String appid;
    public String secret;
    public String code;
    public String grant_type;

    public RequestAccessToken(Context context, String appid, String secret, String code, String grant_type) {
        super(context);
        this.appid = appid;
        this.secret = secret;
        this.code = code;
        this.grant_type = grant_type;
    }

    @Override
    public Map<String, Object> getDataMap() {
        TreeMap map = new TreeMap<String, Object>();
        map.put("appid", appid);
        map.put("secret", secret);
        map.put("code", code);
        map.put("grant_type", grant_type);
        return map;
    }

    @Override
    public ImplParser getParser() {
        return new ParserAccessToken();
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getUrlErrorCode() {
        return "40002";
    }
}
