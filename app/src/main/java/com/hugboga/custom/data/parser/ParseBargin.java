package com.hugboga.custom.data.parser;

import com.google.gson.Gson;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.BarginBean;

import org.json.JSONObject;

/**
 * Created on 16/9/7.
 */

public class ParseBargin extends ImplParser {

        @Override
        public BarginBean parseObject(JSONObject obj) throws Throwable {
            Gson gson = new Gson();
            BarginBean bean = gson.fromJson(obj.toString(),BarginBean.class);
            return bean;
        }
}
