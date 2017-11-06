package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.epos.EposFirstPay;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParseEposFirstPay;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * 验证易宝短信验证码
 * Created by HONGBO on 2017/11/1 18:42.
 */
@HttpRequest(path = UrlLibs.API_EPOS_SMS_VERIFY, builder = NewParamsBuilder.class)
public class RequestEposSmsVerify extends BaseRequest<EposFirstPay> {

    public RequestEposSmsVerify(Context context, String payNo, String verifyCode) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("payNo", payNo);
        map.put("verifyCode", verifyCode);
    }

    @Override
    public String getUrlErrorCode() {
        return "40192";
    }

    @Override
    public ImplParser getParser() {
        return new ParseEposFirstPay();
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }
}
