package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;

import org.json.JSONObject;

/**
 * Created by admin on 2016/2/25.
 */
public class ParserTest extends ImplParser{
    @Override
    public Object parseObject(JSONObject obj) throws Throwable {


        return obj.toString();
    }
}
