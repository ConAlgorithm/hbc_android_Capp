package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.net.HbcParamsBuilder;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.net.UrlLibs;

import org.json.JSONObject;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by admin on 2016/3/23.
 */

@HttpRequest(path = UrlLibs.SERVER_IP_IM_TOKEN_UPDATE,builder = HbcParamsBuilder.class)
public class RequestIMTokenUpdate extends BaseRequest<String> {
    public RequestIMTokenUpdate(Context context, String orderNo) {
        super(context);
        map = new HashMap<String,Object>();
        map.put("apply_type",2);
        map.put("orderNo", orderNo);
    }

    @Override
    public ImplParser getParser() {
        return new ImplParser() {
            @Override
            public Object parseObject(JSONObject obj) throws Throwable {
               return obj.optString("token");
            }
        };
    }
}
