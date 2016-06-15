package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by qingcha on 16/5/24.
 */
@HttpRequest(path = UrlLibs.COLLECT_GUIDES_MOBILE, builder = NewParamsBuilder.class)
public class RequestCollectGuidesMobile extends BaseRequest {

    public RequestCollectGuidesMobile(Context context) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("source", 1);
//        map.put("area_code", );//用户ID
//        map.put("mobile", );//手机号
//        map.put("guideId", );//司导ID
//        map.put("sharingUserId", );//分享的用户ID
//        map.put("openType", );//开放类型
//        map.put("openCode", );//开放code

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
        return "40024";
    }
}