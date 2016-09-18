package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/8/26.
 */
public class ParseNIMToken extends ImplParser {
    @Override
    public Object parseObject(JSONObject obj) throws Throwable {
        if(obj==null){
            return obj;
        }
        Object[] objs = new Object[2];
        String nimUserId = obj.optString("neUserId");
        String nimToken =  obj.optString("neToken");
        //String rimUserId = obj.optString("rcUserId");
        //String rimToken =  obj.optString("rcToken");
        objs[0] = nimUserId;
        objs[1] = nimToken;
        //objs[2] = rimUserId;
       //objs[3] = rimToken;
        return objs;
    }
}
