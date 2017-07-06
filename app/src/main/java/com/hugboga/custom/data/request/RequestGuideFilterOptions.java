package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.activity.CityListActivity;
import com.hugboga.custom.data.bean.FilterGuideOptionsBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by qingcha on 17/6/22.
 */
@HttpRequest(path = UrlLibs.API_GUIDE_FILTER_OPTIONS, builder = NewParamsBuilder.class)
public class RequestGuideFilterOptions extends BaseRequest<FilterGuideOptionsBean> {


    public RequestGuideFilterOptions(Context context, CityListActivity.CityHomeType cityHomeType, String id) {
        super(context);
        map = new HashMap<String, Object>();
        if (cityHomeType != null) {
            switch (cityHomeType) {
                case CITY:
                    map.put("cityIds", id);
                    break;
                case ROUTE:
                    map.put("lineGroupId", id);
                    break;
                case COUNTRY:
                    map.put("countryId", id);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(UrlLibs.API_GUIDE_FILTER_OPTIONS, FilterGuideOptionsBean.class);
    }

    @Override
    public String getUrlErrorCode() {
        return "40153";
    }

}