package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.TestBean;

import org.json.JSONObject;

/**
 * Created by admin on 2016/2/25.
 */
public class ParserTest extends ImplParser{
    @Override
    public Object parseObject(JSONObject obj) throws Throwable {


        return new TestBean();
    }
}
