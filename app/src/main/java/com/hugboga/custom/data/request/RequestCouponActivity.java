package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.HomeBean;
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
@HttpRequest(path = UrlLibs.API_HOME, builder = NewParamsBuilder.class)
public class RequestCouponActivity extends BaseRequest<HomeBean> {

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
        return new HbcParser(UrlLibs.API_HOME, HomeBean.class);
    }

    @Override
    public String getUrlErrorCode() {
        return "40048";
    }

}