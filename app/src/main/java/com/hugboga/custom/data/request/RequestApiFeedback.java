package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.utils.PhoneInfo;

import org.json.JSONObject;
import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

@HttpRequest(path = UrlLibs.API_ERROR_FEEDBACK, builder = NewParamsBuilder.class)
public class RequestApiFeedback extends BaseRequest<String> {
    public RequestApiFeedback(Context context, String userId, String error) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("deviceInfo", PhoneInfo.getIMEI(context));//IMEI
        map.put("userId", userId);//司导或客户ID，取不到可传空，但是必须传
        map.put("appType", "4");
        map.put("error", error);//IM|连接融云服务器|token失效|过期或者不可用
    }

    @Override
    public ImplParser getParser() {
        return new DataParser();
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public String getUrlErrorCode() {
        return "40007";
    }

    private static class DataParser extends ImplParser {
        @Override
        public Object parseObject(JSONObject obj) throws Throwable {
            return obj.toString();
        }
    }
}
