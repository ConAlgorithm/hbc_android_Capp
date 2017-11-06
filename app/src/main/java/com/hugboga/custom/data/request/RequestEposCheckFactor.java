package com.hugboga.custom.data.request;

import android.content.Context;
import android.text.TextUtils;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.bean.epos.EposFirstPay;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParseEposFirstPay;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Epos加验要素
 * Created by HONGBO on 2017/10/30 17:41.
 */
@HttpRequest(path = UrlLibs.API_EPOS_CHECK_FACTOR, builder = NewParamsBuilder.class)
public class RequestEposCheckFactor extends BaseRequest<EposFirstPay> {

    public RequestEposCheckFactor(Context context, String payNo, String credCode, String buyerTel, String buyerName,
                                  String actId, String expireYear, String expireMonth, String cvv) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("payNo", payNo);
        if (!TextUtils.isEmpty(credCode)) {
            map.put("credCode", credCode);  //身份证号
        }
        if (!TextUtils.isEmpty(buyerTel)) {
            map.put("buyerTel", buyerTel);  //pe_BuyerTel="15215317596";
        }
        if (!TextUtils.isEmpty(buyerName)) {
            map.put("buyerName", buyerName);   //pf_BuyerName="张飞";
        }
        if (!TextUtils.isEmpty(actId)) {
            map.put("actId", actId);   //信用卡号： pt_ActId="4062522900966872";
        }
        if (!TextUtils.isEmpty(expireYear)) {
            map.put("expireYear", expireYear);   //信用卡有效期 年份 pa2_ExpireYear="2016";
        }
        if (!TextUtils.isEmpty(expireMonth)) {
            map.put("expireMonth", expireMonth);   //信用卡有效期月份  pa3_ExpireMonth="08";
        }
        if (!TextUtils.isEmpty(cvv)) {
            map.put("cvv", cvv);    //信用卡安全码  pa4_CVV="076";
        }
        map.put("userId", UserEntity.getUser().getUserId(context));     //indentityId 用户标识
    }

    @Override
    public String getUrlErrorCode() {
        return "40189";
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
