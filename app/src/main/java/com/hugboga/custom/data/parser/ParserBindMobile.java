package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.BindMobileBean;

import org.json.JSONObject;

/**
 * BindMobileBean解析器
 * Created by admin on 2016/3/23.
 */
public class ParserBindMobile extends ImplParser {
    @Override
    public BindMobileBean parseObject(JSONObject jsonObj) throws Throwable {
        BindMobileBean cityBean = new BindMobileBean();
        cityBean.isNotRegister = jsonObj.optInt("isNotRegister");
        return cityBean;
    }
}
