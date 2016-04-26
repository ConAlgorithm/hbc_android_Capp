package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.net.HbcParamsBuilder;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.UserBean;
import com.hugboga.custom.data.bean.UserCouponBean;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParserGetCoupon;
import com.hugboga.custom.data.parser.ParserLogin;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created  on 2016/3/30.
 */
@HttpRequest(path = UrlLibs.GET_USER_COUPON, builder = HbcParamsBuilder.class)
public class RequestGetCoupon extends BaseRequest<UserCouponBean> {

    public RequestGetCoupon(Context context) {
        super(context);
    }

    @Override
    public ImplParser getParser() {
        return new ParserGetCoupon();
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }
}
