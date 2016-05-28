package com.hugboga.custom.data.parser;

import com.google.gson.Gson;
import com.huangbaoche.hbcframe.data.parser.ImplParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by dyt on 16/5/28.
 */

public class ParseGuideConflict extends ImplParser {

    @Override
    public List<String> parseObject(JSONObject jsonObj) throws Throwable {
        return null;
    }

    @Override
    public List<String> parseArray(JSONArray array) throws Exception {
        Gson gson = new Gson();
        List<String>  list = gson.fromJson(array.toString(),List.class);
        return list;
    }
}
