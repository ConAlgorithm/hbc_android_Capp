package com.hugboga.custom.widget;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.QueryCityGuideBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by qingcha on 17/12/8.
 * http://wiki.hbc.tech/pages/viewpage.action?pageId=8560325#id-%E7%BA%BF%E8%B7%AF%E5%9C%88%E7%9B%AE%E7%9A%84%E5%9C%B0-url-queryCityGuide
 */

@HttpRequest(path = UrlLibs.API_QUERY_CITY_GUIDE, builder = NewParamsBuilder.class)
public class RequestQueryCityGuide extends BaseRequest<QueryCityGuideBean> {

    public RequestQueryCityGuide(Context context, String cityId) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("cityId", cityId);
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(UrlLibs.API_QUERY_CITY_GUIDE, QueryCityGuideBean.class);
    }

    @Override
    public String getUrlErrorCode() {
        return "40199";
    }

}