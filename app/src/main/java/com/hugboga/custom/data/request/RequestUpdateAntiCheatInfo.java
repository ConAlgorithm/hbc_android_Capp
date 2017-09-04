package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.BuildConfig;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.ishumei.smantifraud.SmAntiFraud;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

import cn.tongdun.android.shell.FMAgent;

/**
 * Created by qingcha on 17/9/4.
 */
@HttpRequest(path = UrlLibs.API_UPDATE_ANTICHEAT_INFO, builder = NewParamsBuilder.class)
public class RequestUpdateAntiCheatInfo extends BaseRequest {

    public RequestUpdateAntiCheatInfo(Context context) {
        super(context);
        map = new HashMap<String, Object>();
        // android 大渠道（例如：官方渠道）接入数美， 其他小渠道接入同盾科技
        boolean isSM = Constants.CHANNEL_OFFICIAL.equals(BuildConfig.FLAVOR);
        map.put("antiType", isSM ? 1 : 2);// 反作弊服务商类型:1:数美、2:同盾

        String deviceId = "";
        if (isSM) {
            deviceId = SmAntiFraud.getDeviceId();
        } else {
            deviceId = FMAgent.onEvent(context);
        }
        map.put("antiId", deviceId);
}

    @Override
    public ImplParser getParser() {
        return null;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public String getUrlErrorCode() {
        return "40186";
    }
}