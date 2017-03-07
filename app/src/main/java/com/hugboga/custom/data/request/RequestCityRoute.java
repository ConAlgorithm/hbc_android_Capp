package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.CityRouteBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by qingcha on 17/2/24.
 * http://wiki.hbc.tech/pages/viewpage.action?pageId=7933695
 */
@HttpRequest(path = UrlLibs.API_CITY_ROUTE, builder = NewParamsBuilder.class)
public class RequestCityRoute extends BaseRequest<CityRouteBean> {

    public int type;
    public int selectedRouteType;

    public RequestCityRoute(Context context, String cityId, int type, int selectedRouteType) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("cityId", cityId);
        this.type = type;
        this.selectedRouteType = selectedRouteType;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(UrlLibs.API_CITY_ROUTE, CityRouteBean.class);
    }

    @Override
    public String getUrlErrorCode() {
        return "40119";
    }

    public int getType() {
        return type;
    }


}
