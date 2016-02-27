package com.hugboga.custom.data.request;

import com.huangbaoche.hbcframe.data.request.BaseRequest;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;
import org.xutils.http.app.DefaultParamsBuilder;

/**
 * Created by admin on 2016/2/25.
 */

@HttpRequest(
        host = "http://api.test.hbc.tech1/",
        path = "reflash/v1.0/checkAppVersion?",
        builder = DefaultParamsBuilder.class/*可选参数, 控制参数构建过程, 定义参数签名, SSL证书等*/)
public class RequestTest3 extends BaseRequest {

        @Override
        public HttpMethod getHttpMethod() {
                return HttpMethod.POST;
        }


}
