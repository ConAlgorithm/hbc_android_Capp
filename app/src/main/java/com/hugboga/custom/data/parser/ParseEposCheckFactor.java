package com.hugboga.custom.data.parser;

import com.google.gson.Gson;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.epos.EposFirstPay;

import org.json.JSONObject;

/**
 * 加验要素请求结果解析
 * Created by HONGBO on 2017/10/30 17:43.
 */

public class ParseEposCheckFactor extends ImplParser {
    @Override
    public Object parseObject(JSONObject obj) throws Throwable {
        return new Gson().fromJson(obj.toString(), EposFirstPay.class);
    }
}
