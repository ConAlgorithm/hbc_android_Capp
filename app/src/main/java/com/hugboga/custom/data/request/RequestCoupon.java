package com.hugboga.custom.data.request;

import android.content.Context;
import android.text.TextUtils;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.CouponBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParserCoupon;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/3/9.
 */
@HttpRequest(path = UrlLibs.SERVER_IP_COUPONS, builder = NewParamsBuilder.class)
public class RequestCoupon extends BaseRequest<ArrayList<CouponBean>> {

    public RequestCoupon(Context context, String orderID, double orderPrice, int offset, int limit) {
        super(context);
        map = new HashMap<String, Object>();
        if (!TextUtils.isEmpty(orderID))
            map.put("useOrderNo", orderID);
        if (orderPrice > 0)
            map.put("useOrderPrice", orderPrice);
        map.put("status", "1,2,98");
        map.put("offset", offset);
        map.put("limit", limit);
        map.put("userId", UserEntity.getUser().getUserId(getContext()));
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
