package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by admin on 2016/3/24.
 */
@HttpRequest(path = UrlLibs.SERVER_IP_GUIDES_COMMENTS, builder = NewParamsBuilder.class)
public class RequestEvaluate extends BaseRequest {
    public RequestEvaluate(Context context, String userId, String userName, String guideID, String guideName, String orderID, int orderType, Integer star1, Integer star2, Integer star3, String comment) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("orderNo", orderID);
        map.put("content", comment);
        map.put("sceneryNarrate", star1);
        map.put("serviceAttitude", star2);
        map.put("routeFamiliar", star3);
        map.put("fromUid", userId);
        map.put("guideId", guideID);
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public ImplParser getParser() {
        return null;
    }
}
