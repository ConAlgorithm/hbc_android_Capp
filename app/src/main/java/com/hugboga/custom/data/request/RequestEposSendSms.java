package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.epos.EposFirstPay;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParseEposFirstPay;

import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * 重新获取验证码
 * Created by HONGBO on 2017/11/1 18:27.
 */
@HttpRequest(path = UrlLibs.API_EPOS_SMS_SEND, builder = NewParamsBuilder.class)
public class RequestEposSendSms extends BaseRequest<EposFirstPay> {

    public RequestEposSendSms(Context context, String payNo) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("payNo", payNo);
    }

    @Override
    public String getUrlErrorCode() {
        return "40191";
    }

    @Override
    public ImplParser getParser() {
        return new ParseEposFirstPay();
    }
}
