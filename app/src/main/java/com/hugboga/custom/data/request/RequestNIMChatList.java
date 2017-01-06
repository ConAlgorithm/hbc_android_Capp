package com.hugboga.custom.data.request;

import android.content.Context;

import com.google.gson.Gson;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.ChatBean;
import com.hugboga.custom.data.bean.CityHomeBean;
import com.hugboga.custom.data.bean.ImListBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParserChatList;

import org.json.JSONObject;
import org.xutils.http.annotation.HttpRequest;

import java.util.TreeMap;

/**
 * Created by Administrator on 2016/8/26.
 */
@HttpRequest(path = UrlLibs.SERVER_IP_NIMCHAT_LIST, builder = NewParamsBuilder.class)
public class RequestNIMChatList extends BaseRequest<ImListBean> {
    public RequestNIMChatList(Context context,int offset,int limit) {
        super(context);
        map = new TreeMap<String, Object>();
        map.put("userId", UserEntity.getUser().getUserId(context));
        map.put("offset",offset);
        map.put("limit",limit);
    }

    @Override
    public ImplParser getParser() {
        return new DataParser();
    }

    @Override
    public String getUrlErrorCode() {
        return "40059";
    }


    private static class DataParser extends ImplParser {
        @Override
        public Object parseObject(JSONObject obj) throws Throwable {
            Gson gson = new Gson();
            ImListBean data = gson.fromJson(obj.toString(), ImListBean.class);
            return data;
        }
    }
}
