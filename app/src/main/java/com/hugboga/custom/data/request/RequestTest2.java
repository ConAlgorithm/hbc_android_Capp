package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.data.request.HbcParamsBuilder;
import com.hugboga.custom.data.bean.TestBean;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParserTest;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.Map;

/**
 * Created by admin on 2016/2/25.
 */

@HttpRequest(
        path = UrlLibs.SERVER_IP_CHECK_APP_VERSION,
        builder = HbcParamsBuilder.class/*可选参数, 控制参数构建过程, 定义参数签名, SSL证书等*/)
public class RequestTest2 extends BaseRequest<TestBean> {

        public RequestTest2(Context context){
                super(context);
//                this.addHeader("ak","9eca5a3cd3e9f37aa70488b92163f479");
//                this.addHeader("ut","fc6ee45998b2be702d67112ade6f3683");
                this.addBodyParameter("appVersion","2.2");
                this.addBodyParameter("resVersions","h5:0");
        }

        @Override
        public Map getDataMap() {
                return null;
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
