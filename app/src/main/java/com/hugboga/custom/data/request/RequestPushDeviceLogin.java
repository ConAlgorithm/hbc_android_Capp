package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.utils.PhoneInfo;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

//http://wiki.hbc.tech/pages/viewpage.action?title=PushApi&spaceKey=developer
@HttpRequest(path = UrlLibs.API_PUSH_DEVICE_LOGIN, builder = NewParamsBuilder.class)
public class RequestPushDeviceLogin extends BaseRequest {

    public RequestPushDeviceLogin(Context context) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("deviceToken", PhoneInfo.getIMEI(context));
        map.put("appType", 2);
        map.put("userType", 2);
        map.put("userId", UserEntity.getUser().getUserId(context));
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public ImplParser getParser() {
        return null;
    }

    @Override
    public String getUrlErrorCode() {
        return "40203";
    }

}