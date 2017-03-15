package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.CarMaxCapaCityBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;
import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;
import java.util.HashMap;

/**
 * http://wiki.hbc.tech/pages/viewpage.action?pageId=7933324
 * */
@HttpRequest(path = UrlLibs.API_CAR_MAX_CAPACITY, builder = NewParamsBuilder.class)
public class RequestCarMaxCapaCity extends BaseRequest<CarMaxCapaCityBean> {

    public RequestCarMaxCapaCity(Context context, int cityId) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("cityId", cityId);
    }

    public RequestCarMaxCapaCity(Context context, int cityId, String carIds) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("cityId", cityId);
        map.put("carIds", carIds);
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(UrlLibs.API_CAR_MAX_CAPACITY, CarMaxCapaCityBean.class);
    }

    @Override
    public String getUrlErrorCode() {
        return "40118";
    }

}