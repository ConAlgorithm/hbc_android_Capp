package com.hugboga.custom.data.parser;

import android.text.TextUtils;

import com.hugboga.custom.data.bean.CouponBean;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.net.UrlLibs;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ZHZEPHI on 2015/7/24.
 */
public class ParserCoupon extends ImplParser {

    public ArrayList<CouponBean> listDate;

    public ParserCoupon(String orderID, double orderPrice, int offset, int limit){
        map =new HashMap<String,Object>();
        if(!TextUtils.isEmpty(orderID))
        map.put("useOrderNo",orderID);
        if(orderPrice>0)
        map.put("useOrderPrice",orderPrice);
        map.put("status","1,2,98");
        map.put("offset",offset);
        map.put("limit",limit);
    }

    @Override
    public void getDataBase(JSONObject obj) throws Exception {
        listDate = new ArrayList<>();
        JSONArray jsonArray = obj.optJSONArray("listData");
        for(int i=0;i<jsonArray.length();i++){
            JSONObject segObj = jsonArray.optJSONObject(i);
            CouponBean bean = new CouponBean();
            bean.parser(segObj);
            listDate.add(bean);
        }
    }

    @Override
    public HttpRequest.HttpMethod getHttpMethod() {
        return HttpRequest.HttpMethod.GET;
    }

    @Override
    public String getUrl() {
        return UrlLibs.SERVER_IP_COUPONS;
    }
}
