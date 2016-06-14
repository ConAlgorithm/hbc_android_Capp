package com.hugboga.custom.data.request;

import android.content.Context;

import com.google.gson.Gson;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CouponBean;
import com.hugboga.custom.data.bean.MostFitAvailableBean;
import com.hugboga.custom.data.bean.TravelFundData;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParserCoupon;

import org.json.JSONObject;
import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by qingcha on 16/5/30.
 */
@HttpRequest(path = UrlLibs.API_COUPONS_AVAILABLE, builder = NewParamsBuilder.class)
public class RequestAvailableCoupon extends BaseRequest<ArrayList<CouponBean>> {

    public RequestAvailableCoupon(Context context, MostFitAvailableBean params, int offset) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("source", Constants.REQUEST_SOURCE);
        map.put("carSeatNum", params.carSeatNum);
        map.put("carTypeId", params.carTypeId);
        map.put("distance", params.distance);
        map.put("expectedCompTime", params.expectedCompTime);
        map.put("limit", params.limit);
        map.put("offset", offset);
        map.put("priceChannel", params.priceChannel);
        map.put("serviceCityId", params.serviceCityId);
        map.put("serviceCountryId", params.serviceCountryId);
        map.put("serviceLocalDays", params.serviceLocalDays);
        map.put("serviceNonlocalDays", params.serviceNonlocalDays);
        map.put("serviceTime", params.serviceTime);
        map.put("userId", params.userId);
        map.put("totalDays", params.totalDays);
        map.put("orderType", params.orderType);
        map.put("useOrderPrice", params.useOrderPrice);
    }

    @Override
    public ImplParser getParser() {
        return new ParserCoupon();
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

}