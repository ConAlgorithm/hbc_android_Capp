package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.CollectGuideBean;
import com.hugboga.custom.data.bean.CouponBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by qingcha on 16/5/24.
 */
public class ParserCollectGuideBean extends ImplParser {
    @Override
    public CollectGuideBean parseObject(JSONObject jsonObj) throws Throwable {
        if (jsonObj == null) return null;
        CollectGuideBean collectGuideBean = new CollectGuideBean();
        collectGuideBean.guideId = jsonObj.optString("guideId");
        collectGuideBean.name = jsonObj.optString("name");
        collectGuideBean.carDesc = jsonObj.optString("carDesc");
        collectGuideBean.carModel = jsonObj.optString("carModel");
        collectGuideBean.stars = jsonObj.optString("stars");
        collectGuideBean.status = jsonObj.optInt("status");
        collectGuideBean.avatar = jsonObj.optString("avatar");
        collectGuideBean.numOfLuggage = jsonObj.optInt("numOfLuggage");
        collectGuideBean.numOfPerson = jsonObj.optInt("numOfPerson");
        collectGuideBean.carClass = jsonObj.optInt("carClass");
        collectGuideBean.carType = jsonObj.optInt("carType");

        try {
            JSONArray typesArray = jsonObj.optJSONArray("serviceTypes");
            if (typesArray != null) {
                ArrayList<Integer> arrayList= new ArrayList<Integer>(typesArray.length());
                for (int i = 0; i < typesArray.length(); i++) {
                    arrayList.add((int)typesArray.get(i));
                }
                collectGuideBean.serviceTypes = arrayList;
            }
        } catch (Exception e){
            //不处理，类型转换异常
        }
        return collectGuideBean;
    }
}
