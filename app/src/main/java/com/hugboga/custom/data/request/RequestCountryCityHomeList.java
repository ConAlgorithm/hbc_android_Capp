package com.hugboga.custom.data.request;

import android.content.Context;

import com.google.gson.Gson;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CityHomeBean;
import com.hugboga.custom.data.bean.SkuCityBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.json.JSONObject;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by qingcha on 16/6/27.
 */
@HttpRequest(path = UrlLibs.API_CITY_HOME_COUNTRY_LIST, builder = NewParamsBuilder.class)
public class RequestCountryCityHomeList extends BaseRequest<SkuCityBean> {


    public RequestCountryCityHomeList(Context context, String countryId, int offset, int daysCountMin, int daysCountMax, int goodsClass, int themeId) {
        super(context);
        map = new HashMap<>();
        map.put("source", Constants.REQUEST_SOURCE);
        map.put("countryId", countryId);
        map.put("offset", offset);
        map.put("limit", Constants.DEFAULT_PAGESIZE);
        map.put("fixGoodsPicSize", "201");
        map.put("recommendGoodsPicSize","201");
        map.put("channelId","18");
        map.put("daysCountMin",daysCountMin);
        map.put("daysCountMax",daysCountMax);
        map.put("goodsClass",goodsClass);
        map.put("themeId",themeId);
    }

    @Override
    public ImplParser getParser() {
        return new DataParser();
    }

    @Override
    public String getUrlErrorCode() {
        return "40027";
    }

    private static class DataParser extends ImplParser {
        @Override
        public Object parseObject(JSONObject obj) throws Throwable {
            Gson gson = new Gson();
            CityHomeBean data = gson.fromJson(obj.toString(), CityHomeBean.class);
            return data;
        }
    }
}