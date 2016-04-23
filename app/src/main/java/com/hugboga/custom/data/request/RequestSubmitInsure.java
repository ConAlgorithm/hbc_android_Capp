package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.net.HbcParamsBuilder;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParseInsureAdd;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by dyt on 16/4/23.
 */
@HttpRequest(path = UrlLibs.SUBMIT_INSURE_LIST, builder = HbcParamsBuilder.class)
public class RequestSubmitInsure extends BaseRequest {

    public RequestSubmitInsure(Context context, String userId, String insuranceUserId, String orderNo) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("userId", userId);
        map.put("insuranceUserId", insuranceUserId);
        map.put("orderNo", orderNo);
    }

    @Override
    public ImplParser getParser() {
        return null;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }
}