package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.CouponBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ZHZEPHI on 2015/7/24.
 */
public class ParserCoupon extends ImplParser {


//    public ParserCoupon(String orderID, double orderPrice, int offset, int limit){
//        map =new HashMap<String,Object>();
//        if(!TextUtils.isEmpty(orderID))
//        map.put("useOrderNo",orderID);
//        if(orderPrice>0)
//        map.put("useOrderPrice",orderPrice);
//        map.put("status","1,2,98");
//        map.put("offset",offset);
//        map.put("limit",limit);
//    }

//
//    @Override
//    public HttpRequest.HttpMethod getHttpMethod() {
//        return HttpRequest.HttpMethod.GET;
//    }
//
//    @Override
//    public String getUrl() {
//        return UrlLibs.SERVER_IP_COUPONS;
//    }

    @Override
    public ArrayList<CouponBean> parseObject(JSONObject obj) throws Throwable {
        ArrayList<CouponBean> listDate = new ArrayList<>();
        JSONArray jsonArray = obj.optJSONArray("listData");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject segObj = jsonArray.optJSONObject(i);
            ParserCouponBean parserCouponBean = new ParserCouponBean();
            CouponBean couponBean = parserCouponBean.parseObject(segObj);
            if (couponBean != null) {
                listDate.add(couponBean);
            }
        }
        return listDate;
    }
}
