package com.huangbaoche.hbcframe.data.parser;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.http.app.ResponseParser;

public interface InterfaceParser extends ResponseParser {

    public abstract Object parseObject(JSONObject obj) throws Throwable;

    public Object parseArray(JSONArray array) throws Throwable;

    public Object parseString(String string) throws Throwable;

}
