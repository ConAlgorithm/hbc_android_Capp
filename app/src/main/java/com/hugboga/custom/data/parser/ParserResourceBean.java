package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.ResourcesBean;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/3/12.
 */
public class ParserResourceBean extends ImplParser {
    @Override
    public ResourcesBean parseObject(JSONObject jsonObj) throws Throwable {
        if (jsonObj == null) return null;
        ResourcesBean bean = new ResourcesBean();
        bean.resVersion = jsonObj.optInt("resVersion");
        bean.resName = jsonObj.optString("resName");
        bean.resUrl = jsonObj.optString("resDownloadLink");
        return bean;
    }
}
