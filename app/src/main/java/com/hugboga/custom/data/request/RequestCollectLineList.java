package com.hugboga.custom.data.request;

import android.content.Context;

import com.google.gson.Gson;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CollectLineBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.json.JSONObject;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by zhangqiang on 17/8/28.
 */
@HttpRequest(path = UrlLibs.COLLECT_LINES_LIST, builder = NewParamsBuilder.class)
public class RequestCollectLineList extends BaseRequest {
    public RequestCollectLineList(Context context,int offset) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("source", Constants.REQUEST_SOURCE);
        map.put("userId", UserEntity.getUser().getUserId(context));
        map.put("offset", offset);
        map.put("limit", Constants.DEFAULT_PAGESIZE);
    }

    @Override
    public ImplParser getParser() {
        return new CollectLineParser();
    }

    @Override
    public String getUrlErrorCode() {
        return "40182";
    }

    private static class CollectLineParser extends ImplParser {
        @Override
        public Object parseObject(JSONObject obj) throws Throwable {
            Gson gson = new Gson();
            CollectLineBean collectLineBean = gson.fromJson(obj.toString(), CollectLineBean.class);
            return collectLineBean;
        }
    }
}
