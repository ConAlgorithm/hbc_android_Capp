package com.hugboga.custom.data.net;

import com.huangbaoche.hbcframe.data.net.HbcParamsBuilder;
import com.hugboga.custom.data.request.RequestWebInfo;

import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpRequest;

/**
 * 适用用web代理请求的 builder 不对 UrlHost 做处理
 * Created by admin on 2016/3/16.
 */
public class WebParamsBuilder extends HbcParamsBuilder {

    @Override
    public String buildUri(RequestParams params, HttpRequest httpRequest) {
        String url = httpRequest.host();
        if (params instanceof RequestWebInfo) {
            url = ((RequestWebInfo) params).getUrl();
        }
        return url;
    }


}
