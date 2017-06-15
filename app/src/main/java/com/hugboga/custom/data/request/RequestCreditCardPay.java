package com.hugboga.custom.data.request;

import android.content.Context;

import com.google.gson.Gson;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.YiLianPayBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.json.JSONObject;
import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/3/8.
 */
@HttpRequest(path = "", builder = NewParamsBuilder.class)
public class RequestCreditCardPay extends BaseRequest<YiLianPayBean> {

    public int apiType;

    @Override
    public String getUrlErrorCode() {
        if (apiType == 1) {
            return "40152";
        } else {
            return "40130";
        }
    }

    public RequestCreditCardPay(Context context, String orderNo, String actualPrice, String coupId, Integer cardId,String telNo,int apiType) {
        super(context);
        this.apiType = apiType;
        map = new HashMap();
        map.put("orderNo",orderNo);         //订单号
        map.put("actualPrice", actualPrice);//实际支付金额
        map.put("coupId", coupId);          //劵id
        map.put("cardId", cardId);          //
        map.put("telNo", telNo);
    }

    @Override
    public ImplParser getParser() {
//        return new HbcParser(UrlLibs.API_CREDIT_PAY, YiLianPayBean.class);
        return new ParserData();
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public String getUrl() {
        if (apiType == 1) {
            return UrlLibs.API_COUPON_CREDIT_PAY;
        } else {
            return UrlLibs.API_CREDIT_PAY;
        }
    }

    private class ParserData extends ImplParser {

        @Override
        public Object parseObject(JSONObject obj) throws Throwable {
            Gson gson = new Gson();
            YiLianPayBean lianPayBean = gson.fromJson(obj.toString(), YiLianPayBean.class);
            return lianPayBean;
        }
    }
}
