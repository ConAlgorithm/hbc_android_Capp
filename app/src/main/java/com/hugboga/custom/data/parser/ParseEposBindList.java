package com.hugboga.custom.data.parser;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.epos.EposBindCard;
import com.hugboga.custom.data.bean.epos.EposBindList;

import org.json.JSONObject;

import java.util.List;

/**
 * Epos绑定卡解析
 * Created by HONGBO on 2017/10/28 17:18.
 */

public class ParseEposBindList extends ImplParser {
    @Override
    public Object parseObject(JSONObject obj) throws Throwable {
        EposBindList bean = new Gson().fromJson(obj.toString(), EposBindList.class);
        if (!TextUtils.isEmpty(bean.re_BindList)) {
            bean.bindList = new Gson().fromJson(bean.re_BindList, new TypeToken<List<EposBindCard>>() {
            }.getType());
        }
        return bean;
    }
}
