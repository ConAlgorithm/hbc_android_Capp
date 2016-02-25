package com.huangbaoche.hbcframe.data.parser;

import com.huangbaoche.hbcframe.data.net.ServerException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.ex.HttpException;

/**
 * 默认的服务器错认解析器,需要可以继承并重写
 *
 * Created by admin on 2016/2/24.
 */
public class ServerParser extends ImplParser{
    public String RESULT_KEY_STATUS    = "status";
    public String RESULT_KEY_DATA      = "data";
    public String RESULT_KEY_MESSAGE   = "message";
    public int    RESULT_KEY_CODE      = 200;

    @Override
    public Object parseObject(JSONObject object) throws Throwable {
        int code = object.optInt(RESULT_KEY_STATUS, -1);
        if (code != RESULT_KEY_CODE) {
            String errMsg = object.optString(RESULT_KEY_MESSAGE);
            throw  new ServerException(code,errMsg);
        } else {
            return object.opt(RESULT_KEY_DATA);
        }
    }
}
