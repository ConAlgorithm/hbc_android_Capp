package com.hugboga.custom.data.request;

import android.content.Context;

import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.xutils.http.annotation.HttpRequest;

@HttpRequest(path = UrlLibs.ORDER_LIST_UNEVALUDATE, builder = NewParamsBuilder.class)
public class RequestOrderListUnevaludate extends RequestTravel{

    public RequestOrderListUnevaludate(Context context) {
        super(context, 6);
    }

    @Override
    public String getUrlErrorCode() {
        return "40117";
    }
}