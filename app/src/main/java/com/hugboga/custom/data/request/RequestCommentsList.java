package com.hugboga.custom.data.request;

import android.content.Context;

import com.google.gson.Gson;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CommentsListData;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.json.JSONObject;
import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by qingcha on 16/6/18.
 */
@HttpRequest(path = UrlLibs.API_COMMENTS_LIST, builder = NewParamsBuilder.class)
public class RequestCommentsList extends BaseRequest<CommentsListData> {

    public RequestCommentsList(Context context, String guideId, int offset) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("guideId", guideId);
//        map.put("guideId", "291442416917");//test
        map.put("offset", offset);
        map.put("limit", Constants.DEFAULT_PAGESIZE);
    }

    @Override
    public ImplParser getParser() {
        return new DataParser();
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getUrlErrorCode() {
        return "40026";
    }

    private static class DataParser extends ImplParser {
        @Override
        public Object parseObject(JSONObject obj) {
            Gson gson = new Gson();
            CommentsListData commentsListData = gson.fromJson(obj.toString(), CommentsListData.class);
            return commentsListData;
        }
    }
}
