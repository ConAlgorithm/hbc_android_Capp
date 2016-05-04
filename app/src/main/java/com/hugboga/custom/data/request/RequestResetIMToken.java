package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParserImToken;

import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * 重置用户的IMToken
 * Created by ZHZEPHI on 2016/3/27.
 */
@HttpRequest(path = UrlLibs.SERVER_IP_IM_TOKEN_UPDATE, builder = NewParamsBuilder.class)
public class RequestResetIMToken extends BaseRequest<String> {

    public RequestResetIMToken(Context context) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("userType", 2);
        map.put("force", 1);
    }

    @Override
    public ImplParser getParser() {
        return new ParserImToken();
    }
}
