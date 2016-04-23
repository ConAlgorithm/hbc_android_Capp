package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.AccessTokenBean;
import com.hugboga.custom.data.bean.UserBean;

import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * Created by admin on 2016/3/8.
 */
public class ParserAccessToken extends ImplParser {
    @Override
    public AccessTokenBean parseObject(JSONObject jsonObj) throws Throwable {
        AccessTokenBean bean = new AccessTokenBean();
        bean.access_token = jsonObj.optString("access_token");
        bean.expires_in = jsonObj.optInt("expires_in");
        bean.refresh_token = jsonObj.optString("refresh_token");
        bean.openid = jsonObj.optString("openid");
        bean.scope = jsonObj.optString("scope");
        return bean;
    }

    @Override
    public Object parse(Type resultType, Class<?> resultClass, String result) throws Throwable {
        JSONObject jsonObject = new JSONObject(result);
        return parseObject(jsonObject);
    }
}
