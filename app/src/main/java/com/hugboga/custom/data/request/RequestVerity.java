package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/3/14.
 */
@HttpRequest(path = UrlLibs.SERVER_IP_CAPTCHA, builder = NewParamsBuilder.class)
public class RequestVerity extends BaseRequest {
    public String areaCode;
    public String mobile;

    public RequestVerity(Context context, String areaCode, String mobile, int type) {
        super(context);
        this.areaCode = areaCode;
        this.mobile = mobile;
        map = new HashMap<String, Object>();
        map.put("areaCode", areaCode);
        map.put("mobile", mobile);
        map.put("captchaType", type);
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
        return "40084";
    }

}
