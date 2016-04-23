package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.net.HbcParamsBuilder;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.CouponBean;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParseInsureAdd;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dyt on 16/4/23.
 */
@HttpRequest(path = UrlLibs.EDIT_INSURE_LIST, builder = HbcParamsBuilder.class)
public class RequestEditInsure extends BaseRequest<ArrayList<CouponBean>> {

    public RequestEditInsure(Context context, String userId,String insuranceUserId, String name,
                            String passportNo, String sex, String birthday) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("userId", userId);
        map.put("insuranceUserId", insuranceUserId);
        map.put("name", name);
        map.put("passportNo", passportNo);
        map.put("sex", sex);
        map.put("birthday", birthday);
    }

    @Override
    public ImplParser getParser() {
        return new ParseInsureAdd();
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }
}

