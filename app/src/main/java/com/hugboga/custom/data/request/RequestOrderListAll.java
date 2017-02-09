package com.hugboga.custom.data.request;

import android.content.Context;

import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.xutils.http.annotation.HttpRequest;

@HttpRequest(path = UrlLibs.ORDER_LIST_ALL, builder = NewParamsBuilder.class)
public class RequestOrderListAll extends RequestTravel{

    public RequestOrderListAll(Context context) {
        super(context, 0);
    }

    @Override
    public String getUrlErrorCode() {
        return "40114";
    }
}
