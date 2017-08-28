package com.hugboga.custom.data.request;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.AirPort;
import com.hugboga.custom.data.bean.GuideCarBean;
import com.hugboga.custom.data.bean.SearchHotBean;
import com.hugboga.custom.data.bean.SearchLineBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

/**
 * Created by zhangqiang on 17/8/26.
 */
import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.http.annotation.HttpRequest;

import java.util.ArrayList;
import java.util.List;

@HttpRequest(path = UrlLibs.API_HOT_SEARCH, builder = NewParamsBuilder.class)
public class RequestHotSearch extends BaseRequest {
    public RequestHotSearch(Context context) {
        super(context);
    }

    @Override
    public ImplParser getParser() {
        return new SearchHot();
    }

    @Override
    public String getUrlErrorCode() {
        return "40179";
    }
    public  class SearchHot extends ImplParser{
        @Override
        public Object parseArray(JSONArray array) throws Exception {
            Gson gson = new Gson();
            ArrayList<String> data = gson.fromJson(array.toString(),new TypeToken<List<String>>(){}.getType());
            return data;
        }
        @Override
        public Object parseObject(JSONObject obj) throws Throwable {
            return null;
        }
    }
}
