package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.CreditCardInfoBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/3/7.
 *  易联支付绑定银行卡
 */
@HttpRequest(path = UrlLibs.API_BIND_CREDIT_CARD, builder = NewParamsBuilder.class)
public class RequestAddCreditCard extends BaseRequest<CreditCardInfoBean> {

    public RequestAddCreditCard(Context context, String creditCardNo, String  idCardNo, String  telNo, String  userName,
                                String accType, String bankId, String bankName) {
        super(context);
        map = new HashMap();
        map.put("creditCardNo", creditCardNo);
        map.put("idCardNo", "623562123456789");         //暂时是假的身份证
        map.put("telNo", telNo);
        map.put("userName",  userName);
        map.put("accType", accType);
        map.put("bankId", bankId);
        map.put("bankName", bankName);
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(UrlLibs.API_BIND_CREDIT_CARD, CreditCardInfoBean.class);
    }

    @Override
    public String getUrlErrorCode() {
        return "40127";
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }
}
