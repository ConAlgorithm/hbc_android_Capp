package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.CouponBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParseInsureList;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created  on 16/4/23.
 */
@HttpRequest(path = UrlLibs.GET_INSURE_LIST, builder = NewParamsBuilder.class)
public class RequestInsureList extends BaseRequest<ArrayList<CouponBean>> {

    public RequestInsureList(Context context, String userId, String insuranceUserId,
                             String offset, String limit) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("userId", userId);
        map.put("insuranceUserId", insuranceUserId);
        map.put("offset", offset);
        map.put("limit", limit);


    }

    @Override
    public ImplParser getParser() {
        return new ParseInsureList();
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getUrlErrorCode() {
        return "40044";
    }
}
