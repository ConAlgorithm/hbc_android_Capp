package com.hugboga.custom.data.request;

import android.content.Context;

import com.google.gson.Gson;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.GuidesDetailData;
import com.hugboga.custom.data.bean.SkuCityBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.json.JSONObject;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;
import java.util.Objects;

/**
 * Created by admin on 2016/3/3.
 */
@HttpRequest(path = UrlLibs.API_SKU_CITY_LIST, builder = NewParamsBuilder.class)
public class RequestCitySkuList extends BaseRequest<SkuCityBean> {


    public RequestCitySkuList(Context context, String cityId, int offset) {
        super(context);
        map = new HashMap<>();
        map.put("source", Constants.REQUEST_SOURCE);
        map.put("cityId", cityId);
        map.put("offset", offset);
        map.put("limit", Constants.DEFAULT_PAGESIZE);
        map.put("picSize", "201");
    }

    @Override
    public ImplParser getParser() {
        return new DataParser();
    }

    @Override
    public String getUrlErrorCode() {
        return "40064";
    }

    private static class DataParser extends ImplParser {
        @Override
        public Object parseObject(JSONObject obj) throws Throwable {
            Gson gson = new Gson();
            SkuCityBean data = gson.fromJson(obj.toString(), SkuCityBean.class);
            return data;
        }
    }
}
