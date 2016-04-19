package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.UserCouponBean;

import org.json.JSONObject;

/**
 * Created by dyt on 16/3/31.
 */
public class ParserGetCoupon extends ImplParser{

    @Override
    public UserCouponBean parseObject(JSONObject jsonObj) throws Throwable {
        UserCouponBean bean = new UserCouponBean();
        if(jsonObj !=null) {
            bean.couponSize = jsonObj.optInt("couponSize");
            bean.hasGuide = jsonObj.optBoolean("hasGuide");
            bean.newCouponSize = jsonObj.optInt("newCouponSize");
            bean.total = jsonObj.optInt("total");
        }
        return bean;
    }
}
