package com.hugboga.custom.data.parser;

import com.google.gson.Gson;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.CanServiceGuideBean;

import org.json.JSONObject;

/**
 * Created on 16/9/9.
 */

public class ParseCanServiceGuide extends ImplParser {
    @Override
    public CanServiceGuideBean parseObject(JSONObject obj) throws Throwable {
        Gson gson = new Gson();
        CanServiceGuideBean canServiceGuideBean = gson.fromJson(obj.toString(),CanServiceGuideBean.class);
        return canServiceGuideBean;
    }
}