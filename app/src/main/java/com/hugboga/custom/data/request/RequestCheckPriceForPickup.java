package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.net.HbcParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.xutils.http.annotation.HttpRequest;

/**
 * Created by Administrator on 2016/3/22.
 */

@HttpRequest(path = UrlLibs.SERVER_IP_PRICE_PICKUP, builder = HbcParamsBuilder.class)
public class RequestCheckPriceForPickup extends RequestCheckPrice {
    public RequestCheckPriceForPickup(Context context, int orderType, String airportCode, Integer cityId, String startLocation, String endLocation, String date) {
        super(context, orderType, airportCode, cityId, startLocation, endLocation, date);
    }
}
