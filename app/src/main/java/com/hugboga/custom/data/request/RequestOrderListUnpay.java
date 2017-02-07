package com.hugboga.custom.data.request;

import android.content.Context;

import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.xutils.http.annotation.HttpRequest;

@HttpRequest(path = UrlLibs.ORDER_LIST_UNPAY, builder = NewParamsBuilder.class)
public class RequestOrderListUnpay extends RequestTravel{

    public RequestOrderListUnpay(Context context) {
        super(context, 4);
    }

    @Override
    public String getUrlErrorCode() {
        return "40115";
    }
}