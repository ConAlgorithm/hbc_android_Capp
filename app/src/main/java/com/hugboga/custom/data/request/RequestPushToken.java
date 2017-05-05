package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.TreeMap;

/**
 * Created by admin on 2016/3/29.
 */

@HttpRequest(path = UrlLibs.SERVER_IP_PUSH_TOKEN ,builder = NewParamsBuilder.class)
public class RequestPushToken extends BaseRequest {

    public RequestPushToken(Context context,String pushToken,String realToken,String appVersion,String deviceId,String osVersion) {
        super(context);
        map = new TreeMap();
        map.put("pushToken",pushToken);
        map.put("realToken",realToken);
        map.put("os","1");//android
        map.put("appVersion",appVersion);
        map.put("deviceId",deviceId);
        map.put("osVersion",osVersion);
        if (UserEntity.getUser().isLogin(context)) {
            map.put("id",UserEntity.getUser().getUserId(context));
        }
//        map.put("success",osVersion);//是否注册成功，1-成功，0-失败
//        map.put("regId",osVersion);//极光、小米或信鸽返回的标识ID
//        map.put("desc",osVersion);//注册失败的描述，错误码等
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.POST;
    }

    @Override
    public ImplParser getParser() {
        return null;
    }

    @Override
    public String getUrlErrorCode() {
        return "40073";
    }
}
