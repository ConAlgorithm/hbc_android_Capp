package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/3/28.
 */
@HttpRequest(path = UrlLibs.SERVER_IP_PASSWORD_RESET, builder = NewParamsBuilder.class)
public class RequestForgetPwd extends BaseRequest {

    public RequestForgetPwd(Context context, String areaCode, String mobile, String password, String verity) {
        super(context);
        map = new HashMap<String,Object>();
        try {
            map.put("areaCode", areaCode);
            map.put("mobile", mobile);
            map.put("password", password);
            map.put("captcha", verity);
        }catch (Exception e){
            MLog.e(e.toString());
        }
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
        return "40035";
    }
}
