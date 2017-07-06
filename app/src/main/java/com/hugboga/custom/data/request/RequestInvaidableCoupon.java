package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CouponBean;
import com.hugboga.custom.data.bean.MostFitAvailableBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParserCoupon;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangqiang on 17/6/24.
 */
@HttpRequest(path = UrlLibs.SERVER_IP_COUPONS_UNAVAILABLE, builder = NewParamsBuilder.class)
public class RequestInvaidableCoupon extends BaseRequest<ArrayList<CouponBean>> {

    public RequestInvaidableCoupon(Context context, MostFitAvailableBean params, int offset) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("source", Constants.REQUEST_SOURCE);
        map.put("carSeatNum", params.carSeatNum);
        map.put("carTypeId", params.carTypeId);
        map.put("distance", params.distance);
        map.put("limit", params.limit);
        map.put("offset", offset);
        map.put("priceChannel", params.priceChannel);
        map.put("serviceCityId", params.serviceCityId);
        map.put("serviceCountryId", params.serviceCountryId);
        map.put("serviceTime", params.serviceTime);
        map.put("userId", params.userId);
        map.put("orderType", params.orderType);
        map.put("useOrderPrice", params.useOrderPrice);
        map.put("carModelId", params.carModelId);

        map.put("expectedCompTime", params.expectedCompTime);

        map.put("totalDays", params.totalDays);
        if (params.isPickupTransfer != null) {
            map.put("isPickupTransfer", params.isPickupTransfer);
        }
    }

    @Override
    public ImplParser getParser() {
        return new ParserCoupon();
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getUrlErrorCode() {
        return "420156";
    }
}
