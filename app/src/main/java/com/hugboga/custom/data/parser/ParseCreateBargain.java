package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;

import org.json.JSONObject;

/**
 * Created on 16/9/8.
 */

public class ParseCreateBargain extends ImplParser {

    @Override
    public String parseObject(JSONObject obj) throws Throwable {
        return obj.optString("h5Url");
    }
}