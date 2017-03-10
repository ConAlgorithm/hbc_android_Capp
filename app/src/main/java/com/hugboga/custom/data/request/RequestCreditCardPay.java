package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.CityRouteBean;
import com.hugboga.custom.data.bean.YiLianPayBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/3/8.
 */
@HttpRequest(path = UrlLibs.API_CREDIT_PAY, builder = NewParamsBuilder.class)
public class RequestCreditCardPay extends BaseRequest<YiLianPayBean> {
    public RequestCreditCardPay(Context context, String orderNo, String actualPrice, String coupId, Integer cardId) {
        super(context);
        map = new HashMap();
        map.put("orderNo",orderNo);         //订单号
        map.put("actualPrice", actualPrice);//实际支付金额
        map.put("coupId", coupId);          //劵id
        map.put("cardId", cardId);          //
    }

    @Override
    public String getUrlErrorCode() {
        return "40130";
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(UrlLibs.API_CREDIT_PAY, YiLianPayBean.class);
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }
}
