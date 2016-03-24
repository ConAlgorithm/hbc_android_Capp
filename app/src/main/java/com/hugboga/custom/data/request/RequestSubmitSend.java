package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.net.HbcParamsBuilder;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.net.UrlLibs;

import org.xutils.http.annotation.HttpRequest;

/**
 * Created by admin on 2016/3/22.
 */
@HttpRequest(path = UrlLibs.SERVER_IP_SUBMIT_TRANSFER, builder = HbcParamsBuilder.class)
public class RequestSubmitSend extends RequestSubmitBase{
    public RequestSubmitSend(Context context, OrderBean orderBean) {
        super(context, orderBean);
        map.put("flightAirportCode", orderBean.flightAirportCode);
        map.put("flightAirportName", orderBean.destAddress);
        map.put("flightAirportBuiding", orderBean.flightBean == null?null:orderBean.flightBean.depTerminal);
    }
}
