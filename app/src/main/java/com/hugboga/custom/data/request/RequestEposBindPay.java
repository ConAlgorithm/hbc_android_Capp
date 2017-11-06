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
 * 已绑定卡支付
 * Created by HONGBO on 2017/11/1 18:07.
 */
@HttpRequest(path = UrlLibs.API_EPOS_BIND_LIST_PAY, builder = NewParamsBuilder.class)
public class RequestEposBindPay extends BaseRequest<EposFirstPay> {

    public RequestEposBindPay(Context context, String orderNo, Double actualPrice, String bindId) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("orderNo", orderNo); //订单号
        map.put("actualPrice", actualPrice); //实付金额
        map.put("userId", UserEntity.getUser().getUserId(context));     //indentityId 用户标识
        map.put("bindId", bindId);     //绑定的卡ID
    }

    @Override
    public String getUrlErrorCode() {
        return "40190";
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
