package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.CouponActivityBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * CApp3.2领券礼物活动
 * Created by qingcha on 16/12/9.
 */
@HttpRequest(path = UrlLibs.COUPON_ACTIVITY, builder = NewParamsBuilder.class)
public class RequestCouponActivity extends BaseRequest<CouponActivityBean> {

    public RequestCouponActivity(Context context) {
        super(context);
        map = new HashMap<String, Object>();
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(UrlLibs.COUPON_ACTIVITY, CouponActivityBean.class);
    }

    @Override
    public String getUrlErrorCode() {
        return "40109";
    }

}