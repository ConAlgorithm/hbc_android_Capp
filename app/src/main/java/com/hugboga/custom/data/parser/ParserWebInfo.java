package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/3/11.
 */
public class ParserWebInfo extends ImplParser {
    @Override
    public String parseObject(JSONObject obj) throws Throwable {
        return obj.toString();
    }

}
