package com.hugboga.custom.data.request;

import android.content.Context;

import com.google.gson.Gson;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.DynamicsData;
import com.hugboga.custom.data.bean.GuidesDetailData;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.json.JSONObject;
import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by qingcha on 16/6/19.
 */
@HttpRequest(path = UrlLibs.API_HOME_DYNAMICS, builder = NewParamsBuilder.class)
public class RequestDynamics extends BaseRequest<DynamicsData> {

    private static final int DEFAULT_REQUEST_DYNAMIC_COUNT = 360;

    public RequestDynamics(Context context, long reqTime) {
        super(context);
        map = new HashMap<String, Object>();
        if (reqTime != 0) {
            map.put("reqTime", reqTime);
        }
        map.put("limit", DEFAULT_REQUEST_DYNAMIC_COUNT);
    }

    @Override
    public ImplParser getParser() {
        return new DataParser();
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getUrlErrorCode() {
        return "40034";
    }

    private static class DataParser extends ImplParser {
        @Override
        public Object parseObject(JSONObject obj) throws Throwable {
            Gson gson = new Gson();
            DynamicsData data = gson.fromJson(obj.toString(), DynamicsData.class);
            return data;
        }
    }
}