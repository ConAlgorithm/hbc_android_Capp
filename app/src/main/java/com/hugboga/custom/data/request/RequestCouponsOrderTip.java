package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.CouponsOrderTipBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by qingcha on 17/6/29.
 */
@HttpRequest(path = UrlLibs.API_COUPONS_ORDERTIP, builder = NewParamsBuilder.class)
public class RequestCouponsOrderTip extends BaseRequest<CouponsOrderTipBean> {

    public RequestCouponsOrderTip(Context context, String orderType) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("orderType", orderType);
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(UrlLibs.API_COUPONS_ORDERTIP, CouponsOrderTipBean.class);
    }

    @Override
    public String getUrlErrorCode() {
        return "40163";
    }

}