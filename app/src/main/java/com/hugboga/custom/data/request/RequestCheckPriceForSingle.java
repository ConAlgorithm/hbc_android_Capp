package com.hugboga.custom.data.request;

import android.content.Context;

import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.xutils.http.annotation.HttpRequest;

/**
 * Created by Administrator on 2016/3/22.
 */
@HttpRequest(path = UrlLibs.SERVER_IP_PRICE_SINGLE, builder = NewParamsBuilder.class)
public class RequestCheckPriceForSingle extends RequestCheckPrice {
    public RequestCheckPriceForSingle(Context context, int orderType, String airportCode, Integer cityId,
                                      String startLocation, String endLocation, String date,String carIds) {
        super(context, orderType, airportCode, cityId, startLocation, endLocation, date, carIds);
    }

    @Override
    public String getUrlErrorCode() {
        return "40019";
    }
}
