package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.net.HbcParamsBuilder;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.UserBean;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParserUserInfo;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/3/17.
 */
@HttpRequest(path = UrlLibs.SERVER_IP_INFORMATION, builder = HbcParamsBuilder.class)
public class RequestUserInfo extends BaseRequest<UserBean> {
    public RequestUserInfo(Context context) {
        super(context);
        map = new HashMap<String, Object>();
    }

    @Override
    public ImplParser getParser() {
        return new ParserUserInfo();
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }
}
