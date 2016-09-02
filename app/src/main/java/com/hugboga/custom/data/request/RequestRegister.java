package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.data.bean.UserBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParserRegister;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/3/16.
 */
@HttpRequest(path = UrlLibs.SERVER_IP_REGISTER, builder = NewParamsBuilder.class)
public class RequestRegister extends BaseRequest<UserBean> {
    public RequestRegister(Context context, String areaCode, String mobile, String password, String verity, String referrerId, int channel) {
        super(context);
        map = new HashMap<String, Object>();
        try {
            map.put("areaCode", areaCode);
            map.put("mobile", mobile);
            map.put("password", password);
            map.put("captcha", verity);
            map.put("referrerId", referrerId);
            map.put("source", 1);
            map.put("fromChannel", channel);
        } catch (Exception e) {
            MLog.e(e.toString());
        }
    }

    @Override
    public ImplParser getParser() {
        return new ParserRegister();
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public String getUrlErrorCode() {
        return "40074";
    }
}
