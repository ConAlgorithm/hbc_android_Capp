package com.huangbaoche.hbcframe.data.net;

import android.content.Context;
import android.text.TextUtils;

import com.huangbaoche.hbcframe.HbcConfig;
import com.huangbaoche.hbcframe.data.bean.UserSession;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;

import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpRequest;
import org.xutils.http.app.ParamsBuilder;

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
        //先清空，会在初始化自动从成员变量里赋值的内容
        params.clearParams();
        // 添加公共参数
        if(params instanceof BaseRequest){

            BaseRequest request = (BaseRequest)params;
            Context context = request.getContext();
            params.setHeader(KEY_HEADER_AK, UserSession.getUser().getAccessKey(context));
            if(UserSession.getUser().getUserToken(context)!=null)
            params.setHeader(KEY_HEADER_UT, UserSession.getUser().getUserToken(context));
            Map<String,Object> map = request.getDataMap();
            request.setMethod(request.getHttpMethod());
            StringBuffer sb = new StringBuffer();
            if(map!=null) {
                if (request.getHttpMethod().equals(HttpMethod.GET)) {
                    for (Map.Entry<String, Object> entity : map.entrySet()) {
                        if (entity.getValue() != null) {
                            params.addQueryStringParameter(entity.getKey(), String.valueOf(entity.getValue()));
                            sb.append(entity.getKey() + "=" + entity.getValue() + "&");
                        }
                    }
                } else {
                    for (Map.Entry<String, Object> entity : map.entrySet()) {
                        if (entity.getValue() != null) {
                            params.addBodyParameter(entity.getKey(), String.valueOf(entity.getValue()));
                            sb.append(entity.getKey() + "=" + entity.getValue() + "&");
                        }
                    }
                }
            }
            MLog.e("URL = " + params.getUri());
            for (int i=0;i<params.getHeaders().size();i++) {
                MLog.e("header = " +params.getHeaders().get(i).key+":"+params.getHeaders().get(i).value);
            }
            MLog.e(request.getHttpMethod()+" params = " + sb.toString());
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
