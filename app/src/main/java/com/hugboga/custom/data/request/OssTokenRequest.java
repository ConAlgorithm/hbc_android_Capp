package com.hugboga.custom.data.request;

/**
 * Created by zhangqiang on 17/6/22.
 */

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.OssTokenBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

@HttpRequest(path = UrlLibs.API_OSS_TOKEN_URL, builder = NewParamsBuilder.class)
public class OssTokenRequest extends BaseRequest<OssTokenBean> {
    public OssTokenRequest(Context context) {
        super(context);

    }

//    http://api.dev.hbc.tech/trade/v1.0/c/order/acceptGuide?
//    // channelId=18&limit=10&offset=7&orderNo=Z190347971527&userId=191442516911,


    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public ImplParser getParser() {
        return new OssTokenBean();
    }

    @Override
    public String getUrlErrorCode() {
        return "400104";
    }
}
