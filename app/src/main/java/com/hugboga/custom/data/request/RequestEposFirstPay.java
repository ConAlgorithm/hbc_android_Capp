package com.hugboga.custom.data.request;

import android.content.Context;

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
 * Epos首次支付卡
 * Created by HONGBO on 2017/10/28 18:10.
 */
@HttpRequest(path = UrlLibs.API_EPOS_FIRST_PAY, builder = NewParamsBuilder.class)
public class RequestEposFirstPay extends BaseRequest<EposFirstPay> {

    public RequestEposFirstPay(Context context, String orderNo, Double actualPrice, String coupId,
                               String credCode, String buyerTel, String buyerName, String actId,
                               String expireYear, String expireMonth, String cvv, boolean isBind) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("orderNo", orderNo); //订单号
        map.put("actualPrice", actualPrice); //实付金额
        map.put("coupId", coupId);  //券ID
        map.put("credCode", credCode);  //身份证号
        map.put("buyerTel", buyerTel);  //pe_BuyerTel="15215317596";
        map.put("buyerName", buyerName);   //pf_BuyerName="张飞";
        map.put("actId", actId);   //信用卡号： pt_ActId="4062522900966872";
        map.put("expireYear", expireYear);   //信用卡有效期 年份 pa2_ExpireYear="2016";
        map.put("expireMonth", expireMonth);   //信用卡有效期月份  pa3_ExpireMonth="08";
        map.put("cvv", cvv);    //信用卡安全码  pa4_CVV="076";
        map.put("userId", UserEntity.getUser().getUserId(context));     //indentityId 用户标识
        map.put("isBind", isBind ? 1 : 0);      //支付成功 是否绑定卡信息  1 绑定  0 不绑定
    }

    @Override
    public String getUrlErrorCode() {
        return ""; //TODO Hongbo错误码待添加
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
