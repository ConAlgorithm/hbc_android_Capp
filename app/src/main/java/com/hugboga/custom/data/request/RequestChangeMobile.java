package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.net.HbcParamsBuilder;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.net.UrlLibs;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/3/12.
 */
@HttpRequest(path = UrlLibs.SERVER_IP_MOBILE_UPDATE, builder = HbcParamsBuilder.class)
public class RequestChangeMobile extends BaseRequest {
    public String areaCode;
    public String mobile;

    public RequestChangeMobile(Context context,String areaCode,String mobile,String verity) {
        super(context);
        this.areaCode = areaCode;
        this.mobile = mobile;
        map = new HashMap<String,Object>();
        try {
            map.put("areaCode",areaCode);
            map.put("mobile",mobile);
            map.put("captcha",verity);
        } catch (Exception e) {
            e.printStackTrace();
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
}
