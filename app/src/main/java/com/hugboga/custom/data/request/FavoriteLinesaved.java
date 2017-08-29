package com.hugboga.custom.data.request;

import android.content.Context;

import com.google.gson.Gson;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.UserFavoriteLineList;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.json.JSONObject;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by zhangqiang on 17/8/28.
 */
@HttpRequest(path = UrlLibs.LINES_SAVED, builder = NewParamsBuilder.class)
public class FavoriteLinesaved extends BaseRequest {
    public FavoriteLinesaved(Context context,String userId) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("userId",userId);
    }

    @Override
    public ImplParser getParser() {
        return new FavoriteLineSavedParser();
    }

    @Override
    public String getUrlErrorCode() {
        return "40181";
    }

    private static class FavoriteLineSavedParser extends ImplParser {
        @Override
        public Object parseObject(JSONObject obj) throws Throwable {
            Gson gson = new Gson();
            UserFavoriteLineList userFavoriteLineList = gson.fromJson(obj.toString(), UserFavoriteLineList.class);
            return userFavoriteLineList;
        }
    }
}
