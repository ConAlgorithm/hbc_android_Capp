package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.DirectionBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by qingcha on 17/2/28.
 */
@HttpRequest(path = UrlLibs.API_DIRECTION, builder = NewParamsBuilder.class)
public class RequestDirection extends BaseRequest<DirectionBean> {

    public RequestDirection(Context context, String origin, String destination, String countryId) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("origin", origin); //出发地坐标 格式:纬度,经度
        map.put("destination", destination);//终点地坐标 格式:纬度,经度
        map.put("countryId", countryId);//坐标对应的国家ID
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(UrlLibs.API_DIRECTION, DirectionBean.class);
    }

    @Override
    public String getUrlErrorCode() {
        return "40120";
    }

}
