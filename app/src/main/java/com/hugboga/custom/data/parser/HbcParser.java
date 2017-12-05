package com.hugboga.custom.data.parser;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.huangbaoche.hbcframe.HbcConfig;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.HomeTopBean;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.JsonUtils;
import com.hugboga.custom.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.http.request.UriRequest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HbcParser extends ImplParser {

    private Type type;
    private String url;

    public HbcParser(String _url, Type _type) {
        this.url = _url;
        this.type = _type;
    }

    @Override
    public Object parse(Type resultType, Class<?> resultClass, String result) throws Throwable {
        JSONObject jsonObject = new JSONObject(result);
        Object data = getServerParser().parseObject(jsonObject);
        if (TextUtils.isEmpty(data.toString()) || "{}".equals(data.toString())) {
            return null;
        }
        if (data instanceof JSONArray) {
            JSONArray dataArray = (JSONArray) data;
            return parseArray(dataArray);
        }
        if (type == null) {
            throw new IllegalArgumentException("param Type cannot null");
        }
        if (HbcConfig.IS_DEBUG) {
            try {
                return JsonUtils.fromJson(data.toString(), type);
            } catch (Exception e) {
                String resultStr = "解析错误 url: " + url;
                CommonUtils.showToast(resultStr);
                LogUtils.e(resultStr);
                LogUtils.json(result);
                e.printStackTrace();
                return null;
            }
        } else {
            try {
                return JsonUtils.fromJson(data.toString(), type);
            } catch (Exception e) {
                return null;
            }
        }
    }

    @Override
    public List<Object> parseArray(JSONArray array) throws Throwable {
        try {
            return JsonUtils.INSTANCE.getGson().fromJson(array.toString(), type);
        } catch (Exception e) {
            String resultStr = "解析错误 url: " + url;
            CommonUtils.showToast(resultStr);
            LogUtils.e(resultStr);
            LogUtils.json(array.toString());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Object parseObject(JSONObject obj) throws Throwable {
        return null;
    }

    @Override
    public void checkResponse(UriRequest request) throws Throwable {

    }
}
