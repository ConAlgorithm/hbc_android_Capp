package com.hugboga.custom.data.request;

import android.content.Context;

import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.xutils.http.annotation.HttpRequest;

@HttpRequest(path = UrlLibs.ORDER_LIST_DOING, builder = NewParamsBuilder.class)
public class RequestOrderListDoing extends RequestTravel{

    public RequestOrderListDoing(Context context) {
        super(context, 5);
    }

    @Override
    public String getUrlErrorCode() {
        return "40116";
    }
}