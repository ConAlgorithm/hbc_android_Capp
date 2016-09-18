package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.CouponBean;
import com.hugboga.custom.data.bean.CouponTitleContent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/9.
 */
public class ParserCouponBean extends ImplParser {
    @Override
    public CouponBean parseObject(JSONObject jsonObj) throws Throwable {
        if (jsonObj == null) return null;
        CouponBean couponBean = new CouponBean();
        couponBean.couponID = jsonObj.optString("couponId");
        couponBean.couponType = jsonObj.optInt("couponType");
        couponBean.price = jsonObj.optString("priceInfo");
        couponBean.actualPrice = jsonObj.optDouble("actualPrice");
        couponBean.startDate = jsonObj.optString("startTime");
        couponBean.endDate = jsonObj.optString("endTime");
        couponBean.couponStatus = jsonObj.optInt("status");
        couponBean.content = jsonObj.optString("content");
        couponBean.applyArea = jsonObj.optString("applyArea");
        couponBean.applyType = jsonObj.optString("applyType");
        couponBean.applyCar = jsonObj.optString("applyCar");
        couponBean.applyRule = jsonObj.optString("orderRuleRemark");
        couponBean.batchName = jsonObj.optString("couponBatchName");
        couponBean.applyCarClass = jsonObj.optString("applyCarClass");

        List<CouponTitleContent> dataList = new ArrayList<>();
        CouponTitleContent titleContent = null;
        JSONArray array = jsonObj.optJSONArray("dataList");
        if(null != array) {
            for (int i = 0; i < array.length(); i++) {
                titleContent = new CouponTitleContent();
                titleContent.title = array.getJSONObject(i).getString("title");
                titleContent.content = array.getJSONObject(i).getString("content");
                dataList.add(titleContent);
            }
        }
        couponBean.dataList = dataList;

        return couponBean;
    }
}
