package com.hugboga.custom.data.request;

import android.content.Context;

import com.google.gson.Gson;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.EvaluateTagBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.json.JSONObject;
import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by qingcha on 16/6/6.
 */
@HttpRequest(path = UrlLibs.API_EVALUATE_TAG, builder = NewParamsBuilder.class)
public class RequestEvaluateTag extends BaseRequest<EvaluateTagBean> {

    public RequestEvaluateTag(Context context, int orderType) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("orderType", orderType);
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
        return "40038";
    }

    private static class DataParser extends ImplParser {
        @Override
        public Object parseObject(JSONObject obj) throws Throwable {
            Gson gson = new Gson();
            EvaluateTagBean evaluateTagBean = gson.fromJson(obj.toString(), EvaluateTagBean.class);
            return evaluateTagBean;
        }
    }
}