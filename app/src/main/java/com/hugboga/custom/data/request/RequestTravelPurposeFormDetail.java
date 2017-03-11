package com.hugboga.custom.data.request;

import android.content.Context;

import com.google.gson.Gson;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.TravelPurposeFormBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.json.JSONObject;
import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/3/3.
 * 意向单详情
 */
@HttpRequest(path = UrlLibs.API_QUERY_TRAVEL_FORM_DETAIL, builder = NewParamsBuilder.class)
public class RequestTravelPurposeFormDetail extends BaseRequest<TravelPurposeFormBean.ListData> {
    public RequestTravelPurposeFormDetail(Context context, String opUserId, Integer id) {
        super(context);
        map = new HashMap<>();
        map.put("opUserId", opUserId);
        map.put("id", id);
    }

    @Override
    public ImplParser getParser() {
        return new DataParser();
    }

    @Override
    public String getUrlErrorCode() {
        return "40123";
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    private static class DataParser extends ImplParser{

        @Override
        public Object parseObject(JSONObject obj) throws Throwable {
            Gson gson = new Gson();
            TravelPurposeFormBean.ListData data = gson.fromJson(obj.toString(), TravelPurposeFormBean.ListData.class);
            return data;
        }
    }
}
