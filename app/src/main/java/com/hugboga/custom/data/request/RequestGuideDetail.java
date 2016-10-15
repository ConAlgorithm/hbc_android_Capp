package com.hugboga.custom.data.request;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.GuidesDetailData;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.json.JSONObject;
import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * 司导详情页接口
 * Created by qingcha on 16/5/28.
 */
@HttpRequest(path = UrlLibs.API_GUIDES_DETAIL, builder = NewParamsBuilder.class)
public class RequestGuideDetail extends BaseRequest<GuidesDetailData> {

    /**
     * @param guideId               司导id  test:291442416917
     * @param guideCarId            司导的车id，如果有，会对这个司导的车优先排序(订单详情页需要传)
     * @param guideAgencyDriverId   司机的id（如果是地接社）(订单详情页需要传)
     * */
    public RequestGuideDetail(Context context, String guideId, String guideCarId, String guideAgencyDriverId) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("guideId", guideId);
        if (!TextUtils.isEmpty(guideCarId)) {
            map.put("guideCarId", guideCarId);
        }
        if (!TextUtils.isEmpty(guideAgencyDriverId)) {
            map.put("guideAgencyDriverId", guideAgencyDriverId);
        }
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
        return "40047";
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