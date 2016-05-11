package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.util.MLog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/17.
 */
public class ParserUpLoadFile extends ImplParser {
    @Override
    public Object parseObject(JSONObject obj) throws Throwable {
        MLog.e("obj=" + obj.toString());
        return obj.toString();
    }

    @Override
    public Object parseArray(JSONArray array) throws Throwable {
        MLog.e("array=" + array);
        List<String> responseList = new ArrayList<>();
        if (array != null) {
            for (int i = 0; i < array.length(); i++) {
                responseList.add(array.getString(i));
            }
        }
        return responseList;
    }

    @Override
    public Object parseString(String string) throws Throwable {
        MLog.e("str=" + string);
        return string.toString();
    }
}
