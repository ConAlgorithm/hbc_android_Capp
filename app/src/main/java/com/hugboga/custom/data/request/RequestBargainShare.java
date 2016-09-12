package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParseCreateBargain;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created on 16/9/8.
 */

@HttpRequest(path = UrlLibs.CREATEBARGAIN, builder = NewParamsBuilder.class)
public class RequestBargainShare extends BaseRequest<String> {

    String orderNo;
    public RequestBargainShare(Context context, String orderNo) {
        super(context);
        this.orderNo = orderNo;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public Map<String, Object> getDataMap() {
        TreeMap map = new TreeMap<String, Object>();
        map.put("orderNo", orderNo);
        return map;
    }

    @Override
    public ImplParser getParser() {
        return new ParseCreateBargain();
    }
    @Override
    public String getUrlErrorCode() {
        return "40102";
    }
}