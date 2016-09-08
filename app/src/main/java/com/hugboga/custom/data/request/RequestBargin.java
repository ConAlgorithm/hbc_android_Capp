package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.BarginBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParseBargin;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created on 16/9/7.
 * 砍价详情
 */
 @HttpRequest(path = UrlLibs.QUERYBARGAIN, builder = NewParamsBuilder.class)
public class RequestBargin extends BaseRequest<BarginBean> {

    String orderNo;
    int limit;
    int offset;
    public RequestBargin(Context context,String orderNo,int limit,int offset) {
        super(context);
        this.orderNo = orderNo;
        this.limit = limit;
        this.offset = offset;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public Map<String, Object> getDataMap() {
        TreeMap map = new TreeMap<String, Object>();
        map.put("orderNo", orderNo);
        map.put("limit", limit);
        map.put("offset", offset);
        return map;
    }

    @Override
    public ImplParser getParser() {
        return new ParseBargin();
    }
    @Override
    public String getUrlErrorCode() {
        return "40101";
    }
}