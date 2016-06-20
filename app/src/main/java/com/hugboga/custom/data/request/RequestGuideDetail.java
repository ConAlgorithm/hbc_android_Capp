package com.hugboga.custom.data.request;

import android.content.Context;

import com.google.gson.Gson;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.GuidesDetailData;
import com.hugboga.custom.data.bean.TravelFundData;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.json.JSONObject;
import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by qingcha on 16/5/28.
 */
@HttpRequest(path = UrlLibs.API_GUIDES_DETAIL, builder = NewParamsBuilder.class)
public class RequestGuideDetail extends BaseRequest<GuidesDetailData> {

    public RequestGuideDetail(Context context, String guideId) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("guideId", guideId);
//        map.put("guideId", "291442416917");//test
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
        return "40040";
    }

    private static class DataParser extends ImplParser {
        @Override
        public Object parseObject(JSONObject obj) throws Throwable {
            Gson gson = new Gson();
            GuidesDetailData guidesDetailData = gson.fromJson(obj.toString(), GuidesDetailData.class);
            return guidesDetailData;
        }
    }
}