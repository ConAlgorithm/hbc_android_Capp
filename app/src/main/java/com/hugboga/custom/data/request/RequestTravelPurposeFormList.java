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
 * Created by Administrator on 2017/3/2.
 * 意向单列表页
 * http://wiki.hbc.tech/pages/viewpage.action?title=C4.0.0&spaceKey=hbcAdmin
 */
@HttpRequest(path = UrlLibs.API_QUERY_TRAVEL_FORM_LIST, builder = NewParamsBuilder.class)
public class RequestTravelPurposeFormList extends BaseRequest<TravelPurposeFormBean> {
    public RequestTravelPurposeFormList(Context context, String opUserId, String offset, String limit) {
        super(context);
        map = new HashMap<>();
        map.put("opUserId", opUserId);
        map.put("offset", offset);
        map.put("limit", limit);
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
        return "40126";
    }

    private static class DataParser extends ImplParser {

        @Override
        public Object parseObject(JSONObject obj) throws Throwable {
            Gson gson = new Gson();
            TravelPurposeFormBean data = gson.fromJson(obj.toString(), TravelPurposeFormBean.class);
            return data;
        }
    }
}
