package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.BuildConfig;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.UserBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParserLogin;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.Map;
import java.util.TreeMap;

@HttpRequest(path = UrlLibs.GET_ACCESS_TOKEN, builder = NewParamsBuilder.class)
public class RequestLoginCheckOpenId extends BaseRequest<UserBean> {
    public String code;

    public RequestLoginCheckOpenId(Context context, String code) {
        super(context);
        this.code = code;
    }

    @Override
    public Map<String, Object> getDataMap() {
        TreeMap map = new TreeMap<String, Object>();
        map.put("code", code);
        map.put("source", 1);
        map.put("distinctid", SensorsDataAPI.sharedInstance(getContext()).getAnonymousId());
        map.put("loginChannel",1);//1:capp 2:m 3:pc
        if (Constants.CHANNEL_GOOGLE_PLAY.equals(BuildConfig.FLAVOR)) {
            map.put("weChatChannel", 1);
        }
        return map;
    }

    @Override
    public ImplParser getParser() {
        return new ParserLogin();
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public String getUrlErrorCode() {
        return "40055";
    }
}
