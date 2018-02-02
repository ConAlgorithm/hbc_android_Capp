package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.CancelReasonBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by qingcha on 31/01/2018.
 */

@HttpRequest(path = UrlLibs.AVAILABLE_CUSTOMER_SERVICE, builder = NewParamsBuilder.class)
public class RequestAvailableCustomerService extends BaseRequest<String> {

    public RequestAvailableCustomerService(Context context) {
        super(context);
        map = new HashMap<String, Object>();
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(UrlLibs.AVAILABLE_CUSTOMER_SERVICE, String.class);
    }

    @Override
    public String getUrlErrorCode() {
        return "40206";
    }

}