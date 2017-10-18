package com.hugboga.custom.data.request;

import android.content.Context;

import com.google.gson.Gson;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.data.bean.SearchGuideBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.json.JSONObject;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by zhangqiang on 17/8/26.
 */
@HttpRequest(path = UrlLibs.API_GOODS_GUIDE_SEARCH, builder = NewParamsBuilder.class)
public class RequestSearchGuide extends BaseRequest {
    Context context;
    public RequestSearchGuide(Context context,String keywords,int offset,int limit) {
        super(context);
        this.context = context;
        map = new HashMap<String, Object>();
        try {
            map.put("key", keywords);
            map.put("offset", offset);
            map.put("limit", limit);
        } catch (Exception e) {
            MLog.e(e.toString());
        }
    }
    @Override
    public ImplParser getParser() {
        return new ParseSearchGuide();
    }

    @Override
    public String getUrlErrorCode() {
        return "40178";
    }

    public class ParseSearchGuide extends ImplParser {
        @Override
        public SearchGuideBean parseObject(JSONObject obj) throws Throwable {
            Gson gson = new Gson();
            SearchGuideBean searchGuideBean = gson.fromJson(obj.toString(),SearchGuideBean.class);
            return searchGuideBean;
        }
    }
}
