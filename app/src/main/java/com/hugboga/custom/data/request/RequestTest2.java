package com.hugboga.custom.data.request;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.parser.ParserTest;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;
import org.xutils.http.app.DefaultParamsBuilder;

/**
 * Created by admin on 2016/2/25.
 */

@HttpRequest(
        host = "http://api.test.hbc.tech/",
        path = "reflash/v1.0/checkAppVersion?",
        builder = DefaultParamsBuilder.class/*可选参数, 控制参数构建过程, 定义参数签名, SSL证书等*/)
public class RequestTest2 extends BaseRequest {

        public RequestTest2(){
                this.addHeader("ak","9eca5a3cd3e9f37aa70488b92163f479");
                this.addHeader("ut","fc6ee45998b2be702d67112ade6f3683");
                this.addBodyParameter("appVersion","2.2");
                this.addBodyParameter("resVersions","h5:0");
        }

        @Override
        public HttpMethod getHttpMethod() {
                return HttpMethod.POST;
        }

        @Override
        public ImplParser getParser() {
                return new ParserTest();
        }
}
