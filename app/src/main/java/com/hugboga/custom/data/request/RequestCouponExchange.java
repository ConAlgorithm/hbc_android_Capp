package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/3/10.
 */
@HttpRequest(path = UrlLibs.SERVER_IP_COUPONS_BIND, builder = NewParamsBuilder.class)
public class RequestCouponExchange extends BaseRequest<String> {
    public RequestCouponExchange(Context context, String couponCode) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("couponCode", couponCode);
        map.put("userId", UserEntity.getUser().getUserId(getContext()));
    }

    @Override
    public ImplParser getParser() {
        return null;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public String getUrlErrorCode() {
        return "40026";
    }
}
