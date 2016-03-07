package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.SkuCityBean;
import com.hugboga.custom.data.bean.SkuItemBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 2016/3/3.
 */
public class ParserSkuCity extends ImplParser {
    @Override
    public Object parseObject(JSONObject obj) throws Throwable {
        SkuCityBean bean = new SkuCityBean();
        bean.cityGuideAmount = obj.optString("cityGuideAmount");
        bean.cityId = obj.optString("cityId");
        bean.cityName = obj.optString("cityName");
        bean.goodsCount = obj.optString("goodsCount");
        JSONArray goodsArray = obj.optJSONArray("goodses");
        ParserSkuItem itemParser = new ParserSkuItem() ;
        if(goodsArray!=null){
            bean.goodsList = new ArrayList<SkuItemBean>();
            for(int i =0;i<goodsArray.length();i++){
                bean.goodsList.add(itemParser.parseObject(goodsArray.optJSONObject(i)));
            }
        }
        return bean;
    }
}
