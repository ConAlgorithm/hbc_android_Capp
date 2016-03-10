package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.net.HbcParamsBuilder;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.net.UrlLibs;

import org.json.JSONObject;
import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2016/3/7.
 */

@HttpRequest(path = UrlLibs.SERVER_IP_UPLOAD_LOGS,builder = HbcParamsBuilder.class)
public class RequestUploadLogs extends BaseRequest<Boolean> {

    private StringBuffer log;

    public RequestUploadLogs(Context context) {
        super(context);
    }
    public RequestUploadLogs(Context context,StringBuffer log) {
        super(context);
        this.log = log;
    }

    @Override
    public Map<String, Object> getDataMap() {
        Map<String,Object> map = new HashMap<>();
//        map.put("log",log.toString());
        return map;
    }

    @Override
    public ImplParser getParser() {
        return new ImplParser() {
            @Override
            public Object parseObject(JSONObject obj) throws Throwable {
                return obj.optBoolean("debug",false);
            }
        };
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }
}
