package com.hugboga.custom.data.request;

import android.content.Context;

import com.google.gson.Gson;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.CurrentServerInfoData;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.json.JSONObject;
import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by qingcha on 16/7/1.
 */
@HttpRequest(path = UrlLibs.API_IM_SERVER_INFO, builder = NewParamsBuilder.class)
public class RequestCurrentServerInfo extends BaseRequest<CurrentServerInfoData> {

    public RequestCurrentServerInfo(Context context) {
        super(context);
        map = new HashMap<String, Object>();
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
        return "40030";
    }

    private static class DataParser extends ImplParser {
        @Override
        public Object parseObject(JSONObject obj) throws Throwable {
            Gson gson = new Gson();
            CurrentServerInfoData data = gson.fromJson(obj.toString(), CurrentServerInfoData.class);
            return data;
        }
    }
}