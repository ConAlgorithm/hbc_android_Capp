package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.SkuItemBean;

import org.json.JSONObject;

/**
 * Created by admin on 2016/3/3.
 */
public class ParserSkuItem extends ImplParser {
    @Override
    public SkuItemBean parseObject(JSONObject obj) throws Throwable {
        SkuItemBean bean = new SkuItemBean();
        bean.goodsMinPrice = obj.optString("goodsMinPrice");
        bean.goodsName = obj.optString("goodsName");
        bean.goodsNo = obj.optString("goodsNo");
        bean.goodsPicture = obj.optString("goodsPicture");
        bean.saleAmount = obj.optString("saleAmount");
        bean.salePoints = obj.optString("salePoints");
        bean.guideAmount = obj.optString("guideAmount");
        return bean;
    }
}
