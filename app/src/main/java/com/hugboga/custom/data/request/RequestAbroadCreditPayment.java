package com.hugboga.custom.data.request;

import android.content.Context;

import com.google.gson.Gson;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.AbroadCreditBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.json.JSONObject;
import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by zhangqiang on 17/9/4.
 */
@HttpRequest(path = UrlLibs.API_PAYMENT_ABROD_CREDIT, builder = NewParamsBuilder.class)
public class RequestAbroadCreditPayment extends BaseRequest {
    public RequestAbroadCreditPayment(Context context,double actualPrice,String orderNo,int terminalId) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("actualPrice", actualPrice);
        map.put("appEnv","m");
        map.put("orderNo",orderNo);
        map.put("payType",1);
        map.put("terminalId",terminalId);//H5(1, "H5钱海支付"), APP(2, "APP钱海支付");

    }

    @Override
    public ImplParser getParser() {
        return new ParseAbroadCredit();
    }

    @Override
    public String getUrlErrorCode() {
        return "40186";
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    public class ParseAbroadCredit extends ImplParser {
        @Override
        public AbroadCreditBean parseObject(JSONObject obj) throws Throwable {
            Gson gson = new Gson();
            AbroadCreditBean abroadCreditBean = gson.fromJson(obj.toString(),AbroadCreditBean.class);
            return abroadCreditBean;
        }
    }
}
