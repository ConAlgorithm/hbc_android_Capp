package com.hugboga.custom.data.request;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.GuideCropBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by qingcha on 17/3/12.
 */
@HttpRequest(path = UrlLibs.API_GUIDECROP, builder = NewParamsBuilder.class)
public class RequestGuideCrop extends BaseRequest<ArrayList<GuideCropBean>> {

    public RequestGuideCrop(Context context, String guideId) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("guideId", guideId);
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public ImplParser getParser() {
        return new DataParser();
    }

    @Override
    public String getUrlErrorCode() {
        return "40134";
    }

    private class DataParser extends ImplParser {

        @Override
        public Object parseArray(JSONArray array) throws Throwable {
            Gson gson = new Gson();
            ArrayList<GuideCropBean> guideCropList = gson.fromJson(array.toString(), new TypeToken<ArrayList<GuideCropBean>>(){}.getType());
            return guideCropList;
        }

        @Override
        public Object parseObject(JSONObject obj) throws Throwable {
            return null;
        }
    }

}