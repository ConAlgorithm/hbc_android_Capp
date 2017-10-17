package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.PickupCouponOpenBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by qingcha on 17/6/2.
 */
@HttpRequest(path = UrlLibs.API_PICKUP_COUPON_OPEN, builder = NewParamsBuilder.class)
public class RequestPickupCouponOpen extends BaseRequest<PickupCouponOpenBean> {

    public RequestPickupCouponOpen(Context context ) {
        super(context);
        map = new HashMap<String, Object>();
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(UrlLibs.API_PICKUP_COUPON_OPEN, PickupCouponOpenBean.class);
    }

    @Override
    public String getUrlErrorCode() {
        return "40149";
    }

}