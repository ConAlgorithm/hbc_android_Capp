package com.hugboga.custom.data.request;

import android.content.Context;

import com.google.gson.Gson;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.UserFavoriteGuideListVo3;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.json.JSONObject;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by zhangqiang on 17/6/29.
 */
@HttpRequest(path = UrlLibs.GUIDES_SAVED, builder = NewParamsBuilder.class)
public class FavoriteGuideSaved extends BaseRequest<UserFavoriteGuideListVo3> {


    public FavoriteGuideSaved(Context context,String userId,String guideIds) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("userId",userId);
        map.put("guideIds",guideIds);
    }

    @Override
    public ImplParser getParser() {
        return new FavoriteGuideSavedParser();
    }

    @Override
    public String getUrlErrorCode() {
        return "420163";
    }
    private static class FavoriteGuideSavedParser extends ImplParser {
        @Override
        public Object parseObject(JSONObject obj) throws Throwable {
            Gson gson = new Gson();
            UserFavoriteGuideListVo3 userFavoriteGuideListVo3 = gson.fromJson(obj.toString(), UserFavoriteGuideListVo3.class);
            return userFavoriteGuideListVo3;
        }
    }
}
