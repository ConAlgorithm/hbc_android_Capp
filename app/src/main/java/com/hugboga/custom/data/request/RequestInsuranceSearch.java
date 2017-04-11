package com.hugboga.custom.data.request;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.DirectionBean;
import com.hugboga.custom.data.bean.InsureListBean;
import com.hugboga.custom.data.bean.InsureSearchBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by qingcha on 17/4/11.
 */
@HttpRequest(path = UrlLibs.API_INSURANCE_SEARCH, builder = NewParamsBuilder.class)
public class RequestInsuranceSearch extends BaseRequest<InsureSearchBean> {

    public RequestInsuranceSearch(Context context, String orderNo) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("orderNo", orderNo);
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(UrlLibs.API_INSURANCE_SEARCH, InsureSearchBean.class);
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getUrlErrorCode() {
        return "40136";
    }

}