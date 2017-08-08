package com.hugboga.custom.data.request;

import android.content.Context;
import android.text.TextUtils;

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
@HttpRequest(path = "", builder = NewParamsBuilder.class)
public class RequestGuideFilterOptions extends BaseRequest<FilterGuideOptionsBean> {

    private String goodsNo;

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

    public RequestGuideFilterOptions(Context context, String goodsNo) {
        super(context);
        this.goodsNo = goodsNo;
        map = new HashMap<String, Object>();
        map.put("source", "c");
        map.put("goodsNo", goodsNo);
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(getUrl(), FilterGuideOptionsBean.class);
    }

    @Override
    public String getUrl() {
        return !TextUtils.isEmpty(goodsNo) ? UrlLibs.API_GOODS_GUIDE_FILTER_OPTIONS : UrlLibs.API_GUIDE_FILTER_OPTIONS;
    }

    @Override
    public String getUrlErrorCode() {
        return !TextUtils.isEmpty(goodsNo) ? "40175" : "40153";
    }

}