package com.hugboga.custom.data.request;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.data.request.HbcParamsBuilder;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;
import org.xutils.http.app.DefaultParamsBuilder;

/**
 * Created by admin on 2016/2/25.
 */

@HttpRequest(
        path = "reflash/v1.0/checkAppVersion?",
        builder = HbcParamsBuilder.class)
public class RequestTest3 extends BaseRequest {

        @Override
        public HttpMethod getHttpMethod() {
                return HttpMethod.POST;
        }


}
