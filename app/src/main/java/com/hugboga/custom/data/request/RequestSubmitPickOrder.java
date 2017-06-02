package com.hugboga.custom.data.request;

import android.content.Context;

import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.xutils.http.annotation.HttpRequest;

@HttpRequest(path = UrlLibs.SERVER_IP_SUBMIT_PICKUP, builder = NewParamsBuilder.class)
public class RequestSubmitPickOrder extends RequestSubmitPick{
    public RequestSubmitPickOrder(Context context, OrderBean orderBean) {
        super(context, orderBean);
    }

    @Override
    public String getUrlErrorCode() {
        return "40083";
    }
}
