package com.hugboga.custom.data.request;

import android.content.Context;

import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.xutils.http.annotation.HttpRequest;

/**
 * Created by admin on 2016/3/22.
 */
@HttpRequest(path = UrlLibs.SERVER_IP_SUBMIT_SINGLE, builder = NewParamsBuilder.class)
public class RequestSubmitRent extends RequestSubmitBase {
    public RequestSubmitRent(Context context, OrderBean orderBean) {
        super(context, orderBean);
    }
}
