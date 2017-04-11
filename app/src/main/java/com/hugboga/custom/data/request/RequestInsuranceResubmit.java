package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by qingcha on 17/4/11.
 */

@HttpRequest(path = UrlLibs.API_INSURANCE_RESUBMIT, builder = NewParamsBuilder.class)
public class RequestInsuranceResubmit extends BaseRequest {

    public RequestInsuranceResubmit(Context context, String insuranceNo) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("insuranceNo", insuranceNo);
    }

    @Override
    public ImplParser getParser() {
        return null;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public String getUrlErrorCode() {
        return "40135";
    }
}