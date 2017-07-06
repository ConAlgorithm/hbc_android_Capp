package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParseGuideConflict;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;
import java.util.List;

// http://wiki.hbc.tech/pages/viewpage.action?pageId=8553972
@HttpRequest(path = UrlLibs.GUIDE_CONFLIC, builder = NewParamsBuilder.class)
public class RequestGuideConflict extends BaseRequest<List<String>> {

    public RequestGuideConflict(Context context, int orderType, int cityId, String guideIds,
                                String startTime, String origin, String destination, String countryId) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("orderType", orderType);
        map.put("cityId", cityId);
        map.put("guideIds", guideIds);
        map.put("startTime", startTime);
        map.put("origin", origin);
        map.put("destination", destination);
        map.put("countryId", countryId);
        errorType = ERROR_TYPE_IGNORE;
    }

    @Override
    public ImplParser getParser() {
        return new ParseGuideConflict();
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getUrlErrorCode() {
        return "40046";
    }
}
