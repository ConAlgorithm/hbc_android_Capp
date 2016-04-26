package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.net.HbcParamsBuilder;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.CouponBean;
import com.hugboga.custom.data.net.UrlLibs;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created  on 16/4/23.
 */
@HttpRequest(path = UrlLibs.DEL_INSURE_LIST, builder = HbcParamsBuilder.class)
public class RequestDelInsure extends BaseRequest<ArrayList<CouponBean>> {

    public RequestDelInsure(Context context, String insuranceUserId) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("insuranceUserId", insuranceUserId);
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