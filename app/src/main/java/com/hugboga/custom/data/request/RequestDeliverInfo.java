package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.DeliverInfoBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by qingcha on 16/9/8.
 * http://wiki.hbc.tech/pages/viewpage.action?pageId=6619604
 */
@HttpRequest(path = UrlLibs.DELIVER_INFO, builder = NewParamsBuilder.class)
public class RequestDeliverInfo extends BaseRequest<DeliverInfoBean> {

    public RequestDeliverInfo(Context context, String orderNo) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("orderNo", orderNo);
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(UrlLibs.DELIVER_INFO, DeliverInfoBean.class);
    }

    @Override
    public String getUrlErrorCode() {
        return "40103";
    }

}