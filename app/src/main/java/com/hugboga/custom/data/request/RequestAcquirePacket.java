package com.hugboga.custom.data.request;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.utils.Common;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by qingcha on 16/12/10.
 */
@HttpRequest(path = UrlLibs.ACQUIRE_PACKET, builder = NewParamsBuilder.class)
public class RequestAcquirePacket extends BaseRequest {

    private static final String SIGN_KEY = "$RFVbgt5";

    public RequestAcquirePacket(String mobile, String areaCode) {
        super(MyApplication.getAppContext());
        map = new HashMap<String, Object>();
        map.put("mobile", mobile);
        map.put("areaCode", areaCode);
        map.put("sign", Common.md5(Common.md5(areaCode) + SIGN_KEY));
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public ImplParser getParser() {
        return null;
    }

    @Override
    public String getUrlErrorCode() {
        return "40110";
    }

}