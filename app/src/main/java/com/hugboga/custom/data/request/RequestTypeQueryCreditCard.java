package com.hugboga.custom.data.request;

import android.content.Context;

import com.google.gson.Gson;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.CreditCardInfoBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.json.JSONObject;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/3/8
 * 查询银行卡所属银行.
 */
@HttpRequest(path = UrlLibs.API_QUERY_BANK_BELONG, builder = NewParamsBuilder.class)
public class RequestTypeQueryCreditCard extends BaseRequest<CreditCardInfoBean> {
    public RequestTypeQueryCreditCard(Context context, String bankNo, String amount) {
        super(context);
        map = new HashMap<>();
        map.put("bankNo", bankNo);
        map.put("amount", amount);
    }

    @Override
    public String getUrlErrorCode() {
        return "40129";
    }

    @Override
    public ImplParser getParser() {
        return new DataParser();
    }

    public class DataParser extends ImplParser {

        @Override
        public Object parseObject(JSONObject obj) throws Throwable {
            Gson gson = new Gson();
            CreditCardInfoBean bean = gson.fromJson(obj.toString(), CreditCardInfoBean.class);
            return bean;
        }
    }
}
