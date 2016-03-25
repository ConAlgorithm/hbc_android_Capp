package com.hugboga.custom.data.request;

import android.content.Context;
import android.text.TextUtils;

import com.huangbaoche.hbcframe.data.net.HbcParamsBuilder;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.net.UrlLibs;

import org.xutils.http.annotation.HttpRequest;

/**
 * Created by admin on 2016/3/22.
 */
@HttpRequest(path = UrlLibs.SERVER_IP_SUBMIT_TRANSFER, builder = HbcParamsBuilder.class)
public class RequestSubmitSend extends RequestSubmitBase {
    public RequestSubmitSend(Context context, OrderBean orderBean) {
        super(context, orderBean);
        if (orderBean.flightBean != null) {
            map.put("flightFlyTimeL", orderBean.flightBean.depDate + " " + orderBean.flightBean.depTime + ":00");
            map.put("flightArriveTimeL", orderBean.flightBean.arrDate + " " + orderBean.flightBean.arrivalTime + ":00");
            map.put("flightAirportBuiding", orderBean.flightBean == null ? null : orderBean.flightBean.depTerminal);
            if (!TextUtils.isEmpty(orderBean.flightBean.flightNo)) {
                map.put("flightNo", orderBean.flightBean.flightNo);
            }
            if (!TextUtils.isEmpty(orderBean.flightBean.depAirportCode)) {
                map.put("flightAirportCode", orderBean.flightBean.depAirportCode);
            }
            if (!TextUtils.isEmpty(orderBean.flightBean.depAirportName)) {
                map.put("flightAirportName", orderBean.flightBean.depAirportName);
            }
            if (!TextUtils.isEmpty(orderBean.flightBean.arrivalAirportCode)) {
                map.put("flightDestCode", orderBean.flightBean.arrivalAirportCode);
            }
            if (!TextUtils.isEmpty(orderBean.flightBean.arrAirportName)) {
                map.put("flightDestName", orderBean.flightBean.arrAirportName);
            }

        }
    }
}
