package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.BuildConfig;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.json.JSONObject;
import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by qingcha on 17/9/4.
 */
@HttpRequest(path = UrlLibs.API_REPORT, builder = NewParamsBuilder.class)
public class RequestUpdateAntiCheatInfo extends BaseRequest<String> {

    public RequestUpdateAntiCheatInfo(Context context, String body) {
        super(context);
        map = new HashMap<String, Object>();
        bodyEntity = body;
        // android 大渠道（例如：官方渠道）接入数美， 其他小渠道接入同盾科技
        // 反作弊服务商类型:1:数美、2:同盾
        map.put("antiType", Constants.CHANNEL_OFFICIAL.equals(BuildConfig.FLAVOR) ? 1 : 2);
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
        return "40106";
    }

    private static class DataParser extends ImplParser {
        @Override
        public Object parseObject(JSONObject obj) throws Throwable {
            return obj.toString();
        }
    }
}