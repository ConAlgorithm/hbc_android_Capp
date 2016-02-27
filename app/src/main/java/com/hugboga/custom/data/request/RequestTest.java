package com.hugboga.custom.data.request;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.TestBean;
import com.hugboga.custom.data.net.UrlLibs;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;
import org.xutils.http.app.DefaultParamsBuilder;

/**
 * Created by admin on 2016/2/25.
 */

@HttpRequest(
         path = "", builder = DefaultParamsBuilder.class)
public class RequestTest extends BaseRequest<TestBean> {

        @Override
        public HttpMethod getHttpMethod() {
                return HttpMethod.POST;
        }


}
