package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParserWxPay;
import com.hugboga.custom.utils.Config;

import org.json.JSONObject;
import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * 支付宝 支付id
 * Created by admin on 2016/3/23.
 */

@HttpRequest(path = UrlLibs.SERVER_IP_ORDER_PAY_ID,builder = NewParamsBuilder.class)
public class RequestPayNo extends BaseRequest<Object> {

    public  int payType;

    /**
     * orderID 订单ID<br/>
     * payPrice 支付金额<br/>
     * couponID 优惠券<br/>
     * payType 1 支付宝 ，2 微信
     */
    public RequestPayNo(Context context, String orderId, double payPrice, int payType, String couponID){
        super(context);
        map =new HashMap<String,Object>();
        map.put("appId", Config.getImei());
        map.put("appEnv","android");
        map.put("orderNo",orderId);
        map.put("actualPrice",payPrice);
        map.put("coupId", couponID);
        map.put("payType", payType);
        this.payType = payType;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return  HttpMethod.POST;
    }

    @Override
    public ImplParser getParser() {
        if(payType == Constants.PAY_STATE_WECHAT){
            return  new ParserWxPay();
        }else{
            return new ImplParser() {
                @Override
                public Object parseObject(JSONObject obj) throws Throwable {
                    return obj.optString("payurl");
                }
            };
        }

    }
}
