package com.hugboga.custom.data.request;

import android.content.Context;

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
 * Created by zhangqiang on 17/6/24.
 */
@HttpRequest(path = UrlLibs.SERVER_IP_COUPONS_UNUSED, builder = NewParamsBuilder.class)
public class RequestUnusedCoupon extends BaseRequest<ArrayList<CouponBean>> {


    public RequestUnusedCoupon(Context context, int offset, int limit) {
        super(context);
        map = new HashMap<String, Object>();
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
    @Override
    public String getUrlErrorCode() {
        return "420154";
    }
}
