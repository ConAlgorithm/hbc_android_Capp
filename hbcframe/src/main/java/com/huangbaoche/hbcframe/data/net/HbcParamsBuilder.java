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

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.net.ssl.SSLSocketFactory;



/**
 *  默认 Builder
 */
public class HbcParamsBuilder implements ParamsBuilder {

    public static String KEY_HEADER_AK ="ak";//AccessKey
    public static String KEY_HEADER_UT ="ut";//UserToken

    private Context mContext;
    @Override
    public String buildUri(RequestParams params, HttpRequest httpRequest) {
        String host = getHost(httpRequest.host());
        String path = httpRequest.path();
        if(params instanceof BaseRequest){
            BaseRequest request = (BaseRequest)params;
            mContext = request.getContext();
            if(!TextUtils.isEmpty(request.getUrl()))
            path = request.getUrl() ;
        }
        return host+path;
    }

    @Override
    public String buildCacheKey(RequestParams params, String[] cacheKeys) {
        return null;
    }

    @Override
    public SSLSocketFactory getSSLSocketFactory() {
        return DefaultSSLSocketFactory.getSocketFactory(mContext);
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
                map.put("channelId", "18");
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
                            if(entity.getValue() instanceof File){
                                params.setMultipart(true);
                                params.addBodyParameter(entity.getKey(), (File) entity.getValue());
                            }else {
                                params.addBodyParameter(entity.getKey(), String.valueOf(entity.getValue()));
                            }
                            sb.append(entity.getKey() + "=" + entity.getValue() + "&");
                        }
                    }
                }
            }
            if (!TextUtils.isEmpty(request.bodyEntity)) {
                try {
                    params.setBodyContent(URLEncoder.encode(request.bodyEntity, "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
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
