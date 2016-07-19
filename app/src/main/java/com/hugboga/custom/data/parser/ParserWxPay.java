package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.WXpayBean;

import org.json.JSONObject;

/**
 * Created by admin on 2016/3/25.
 */
public class ParserWxPay extends ImplParser {
    @Override
    public WXpayBean parseObject(JSONObject jsonObj) throws Throwable {
        WXpayBean bean = new WXpayBean();
        JSONObject obj = jsonObj.optJSONObject("tenAppPayVo");
        if(obj!=null) {
            bean.appid = obj.optString("appid");
            bean.coupPay = obj.optBoolean("coupPay");
            bean.noncestr = obj.optString("noncestr");
            bean.packageinfo = obj.optString("packageinfo");
            bean.partnerid = obj.optString("partnerid");
            bean.prepayid = obj.optString("prepayid");
            bean.sign = obj.optString("sign");
            bean.timestamp = obj.optString("timestamp");
            bean.travelFundPay = obj.optBoolean("travelFundPay", false);
        }
        return bean;
    }
}
