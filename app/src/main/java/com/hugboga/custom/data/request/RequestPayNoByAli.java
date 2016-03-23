package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.net.HbcParamsBuilder;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.utils.Config;

import org.json.JSONObject;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * 支付宝 支付id
 * Created by admin on 2016/3/23.
 */

@HttpRequest(path = UrlLibs.SERVER_IP_ORDER_PAY_ID,builder = HbcParamsBuilder.class)
public class RequestPayNoByAli extends BaseRequest<String> {

    /**
     * orderID 订单ID<br/>
     * payPrice 支付金额<br/>
     * couponID 优惠券
     */
    public RequestPayNoByAli(Context context,String orderId,double payPrice,int getway,String couponID){
        super(context);
        map =new HashMap<String,Object>();
        map.put("appId", Config.getImei());
        map.put("appEnv","android");
        map.put("orderNo",orderId);
        map.put("actualPrice",payPrice);
        map.put("coupId", couponID);
    }

    @Override
    public ImplParser getParser() {
        return new ImplParser() {
            @Override
            public Object parseObject(JSONObject obj) throws Throwable {
                return obj.optString("payurl");
            }
        };
    }
}
