package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

@HttpRequest(path = UrlLibs.API_GUIDE_AVAILABLE_CHECK, builder = NewParamsBuilder.class)
public class RequestCheckGuide extends BaseRequest {
    public RequestCheckGuide(Context context, String guideId, int cityId, int orderType, String startTime, String endTime) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("guideId", guideId);
        map.put("cityId", cityId);
        map.put("orderType", orderType);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        errorType = ERROR_TYPE_IGNORE;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public ImplParser getParser() {
        return null;
    }

    @Override
    public String getUrlErrorCode() {
        return "40143";
    }
}