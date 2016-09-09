package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.CanServiceGuideBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParseCanServiceGuide;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created on 16/9/9.
 */


@HttpRequest(path = UrlLibs.ACCEPT_GUIDE_LIST, builder = NewParamsBuilder.class)
public class RequestAcceptGuide extends BaseRequest<CanServiceGuideBean> {
    public RequestAcceptGuide(Context context, String orderNo,int limit,int offset) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("orderNo", orderNo);
        map.put("limit",limit);
        map.put("offset",offset);
    }

//    http://api.dev.hbc.tech/trade/v1.0/c/order/acceptGuide?
//    // channelId=18&limit=10&offset=7&orderNo=Z190347971527&userId=191442516911,


    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public ImplParser getParser() {
        return new ParseCanServiceGuide();
    }

    @Override
    public String getUrlErrorCode() {
        return "400104";
    }
}
