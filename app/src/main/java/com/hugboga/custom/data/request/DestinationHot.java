package com.hugboga.custom.data.request;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.HomeBeanV2;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.http.annotation.HttpRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangqiang on 17/7/12.
 */
@HttpRequest(path = UrlLibs.API_DESTINATIONS_HOT, builder = NewParamsBuilder.class)
public class DestinationHot extends BaseRequest<HomeBeanV2.HotCity> {
    public DestinationHot(Context context) {
        super(context);
    }

    @Override
    public ImplParser getParser() {
        return new DesHotParser();
    }

    @Override
    public String getUrlErrorCode() {
        return "430169";
    }
    public  class DesHotParser extends ImplParser {
        @Override
        public Object parseObject(JSONObject obj) throws Throwable {
            return null;
        }

        @Override
        public Object parseArray(JSONArray obj) {
            Gson gson = new Gson();
            ArrayList<HomeBeanV2.HotCity> homeHotCityVo = gson.fromJson(obj.toString(), new TypeToken<List<HomeBeanV2.HotCity>>(){}.getType());
            return homeHotCityVo;
        }
    }
}
