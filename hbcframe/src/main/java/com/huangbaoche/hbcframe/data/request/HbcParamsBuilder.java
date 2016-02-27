package com.huangbaoche.hbcframe.data.request;

import android.text.TextUtils;

import com.huangbaoche.hbcframe.HbcConfig;

import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpRequest;
import org.xutils.http.app.ParamsBuilder;
import org.xutils.x;

import java.util.HashMap;

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
        url += "/" + httpRequest.path();
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
//        params.addHeader(KEY_HEADER_AK, "xxxx");
//        params.addParameter(KEY_HEADER_UT, "xxxx");



    }

    @Override
    public void buildSign(RequestParams params, String[] signs) {
    }


    private String getHost(String host) {
        return !TextUtils.isEmpty(host) ? host : HbcConfig.serverHost;
    }
}
