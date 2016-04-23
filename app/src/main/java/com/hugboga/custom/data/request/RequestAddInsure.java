package com.hugboga.custom.data.request;

import android.content.Context;
import android.text.TextUtils;

import com.huangbaoche.hbcframe.data.net.HbcParamsBuilder;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.CouponBean;
import com.hugboga.custom.data.bean.InsureResultBean;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParseInsureAdd;
import com.hugboga.custom.data.parser.ParseInsureList;
import com.hugboga.custom.data.parser.ParserCoupon;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dyt on 16/4/22.
 */
@HttpRequest(path = UrlLibs.ADD_INSURE, builder = HbcParamsBuilder.class)
public class RequestAddInsure extends BaseRequest {

    public RequestAddInsure(Context context, String userId, String name,
                            String passportNo, String sex,String birthday) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("userId", userId);
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
