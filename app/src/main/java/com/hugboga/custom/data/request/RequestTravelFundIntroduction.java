package com.hugboga.custom.data.request;

import android.content.Context;

import com.google.gson.Gson;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by qingcha on 16/5/26.
 */
@HttpRequest(path = UrlLibs.TRAVELFUND_INTRODUCTION, builder = NewParamsBuilder.class)
public class RequestTravelFundIntroduction extends BaseRequest<String[]> {

    public RequestTravelFundIntroduction(Context context) {
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
        return "40075";
    }

    private static class DataParser extends ImplParser {
        @Override
        public Object parseObject(JSONObject obj) throws Throwable {
            return null;
        }

        @Override
        public Object parseArray(JSONArray array) throws Throwable {
            String[] result = null;
            if (array != null) {
                final int length = array.length();
                result = new String[length];
                for (int i = 0; i < length; i++) {
                    result[i] = array.get(i).toString();
                }
            }
            return result;
        }
    }


}
