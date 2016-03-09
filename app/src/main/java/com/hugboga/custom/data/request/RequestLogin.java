package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.net.HbcParamsBuilder;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.UserBean;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParserLogin;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.Map;
import java.util.TreeMap;

/**
 *
 *
 * Created by admin on 2016/3/8.
 */
@HttpRequest(path = UrlLibs.SERVER_IP_LOGIN,builder = HbcParamsBuilder.class)
public class RequestLogin extends BaseRequest<UserBean> {
    private String areaCode;
    private String mobile;
    private String password;

    public RequestLogin(Context context,String areaCode, String mobile, String password) {
        super(context);
        this.areaCode = areaCode;
        this.mobile = mobile;
        this.password = password;
    }

    @Override
    public Map<String, Object> getDataMap() {
        TreeMap map = new TreeMap<String,Object>();
        map.put("areaCode", areaCode);
        map.put("mobile", mobile);
        map.put("password", password);
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
