package com.hugboga.custom.data.request;

import android.content.Context;

import com.google.gson.Gson;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.data.bean.HomeBeanV2;
import com.hugboga.custom.data.bean.SearchLineBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.json.JSONObject;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by zhangqiang on 17/8/24.
 */
@HttpRequest(path = UrlLibs.API_GOODS_LINE_SEARCH, builder = NewParamsBuilder.class)
public class RequestSearchLine extends BaseRequest {
    Context context;

    public RequestSearchLine(Context context,String keywords,int offset,int limit) {
        super(context);
        this.context = context;
        map = new HashMap<String, Object>();
        try {
            map.put("keywords", keywords);
            map.put("offset", offset);
            map.put("limit", limit);
        } catch (Exception e) {
            MLog.e(e.toString());
        }
    }

//    @Override
//    public Map getDataMap() {
//        try {
//            map.put("keywords",keywords);
//            if(!isDefault){
//                map.put("offset",offset);
//                map.put("limit",limit);
//            }
//        } catch (Exception e) {
//            MLog.e(e.toString());
//        }
//        return map;
//    }
    @Override
    public ImplParser getParser() {
        return new ParseSearchLine();
    }

    @Override
    public String getUrlErrorCode() {
        return "40177";
    }

    public class ParseSearchLine extends ImplParser {
        @Override
        public SearchLineBean parseObject(JSONObject obj) throws Throwable {
            Gson gson = new Gson();
            SearchLineBean searchLineBean = gson.fromJson(obj.toString(),SearchLineBean.class);
            return searchLineBean;
        }
    }
}
