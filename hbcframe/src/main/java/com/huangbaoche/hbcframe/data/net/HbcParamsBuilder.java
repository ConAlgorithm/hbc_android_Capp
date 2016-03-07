package com.huangbaoche.hbcframe.data.net;

import android.content.Context;
import android.content.Entity;
import android.text.TextUtils;

import com.huangbaoche.hbcframe.HbcConfig;
import com.huangbaoche.hbcframe.data.bean.UserEntity;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;

import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpRequest;
import org.xutils.http.app.ParamsBuilder;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLSocketFactory;

/**
 *  默认 Builder
 */
public class HbcParamsBuilder implements ParamsBuilder {

    public static String KEY_HEADER_AK ="ak";//AccessKey
    public static String KEY_HEADER_UT ="ut";//UserToken

    @Override
    public String buildUri(RequestParams params, HttpRequest httpRequest) {
        String url = getHost(httpRequest.host());
        url += httpRequest.path();
        return url;
    }

    @Override
    public String buildCacheKey(RequestParams params, String[] cacheKeys) {
        return null;
    }

    @Override
    public SSLSocketFactory getSSLSocketFactory() {
        return null;
    }

    @Override
    public void buildParams(RequestParams params) {
        // 添加公共参数
        if(params instanceof BaseRequest){

            BaseRequest request = (BaseRequest)params;
            Context context = request.getContext();
            params.setHeader(KEY_HEADER_AK, UserEntity.getUser().getAccessKey(context));
            params.setHeader(KEY_HEADER_UT, UserEntity.getUser().getUserToken(context));
            Map<String,Object> map = request.getDataMap();
            request.setMethod(request.getHttpMethod());
            if(map!=null) {
                if (request.getHttpMethod() == HttpMethod.GET) {
                    for (Map.Entry<String, Object> entity : map.entrySet()) {
                        if (entity.getValue() != null)
                            params.addQueryStringParameter(entity.getKey(), String.valueOf(entity.getValue()));
                    }
                } else {
                    for (Map.Entry<String, Object> entity : map.entrySet()) {
                        if (entity.getValue() != null)
                            params.addBodyParameter(entity.getKey(), String.valueOf(entity.getValue()));
                    }
                }
            }
            MLog.e("buildParams url= " + params.getUri());
        }else{
            throw new RuntimeException("params must instanceof BaseRequest");
        }
    }

    @Override
    public void buildSign(RequestParams params, String[] signs) {
    }


    private String getHost(String host) {
        return !TextUtils.isEmpty(host) ? host : HbcConfig.serverHost;
    }
}
