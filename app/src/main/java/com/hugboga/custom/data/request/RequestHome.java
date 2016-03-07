package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.data.request.HbcParamsBuilder;
import com.hugboga.custom.data.bean.HomeBean;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParserHome;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by admin on 2016/3/2.
 */
@HttpRequest(
        path = UrlLibs.SERVER_IP_HOME,
        builder = HbcParamsBuilder.class)
public class RequestHome extends BaseRequest<ArrayList<HomeBean>> {

    public RequestHome(Context context) {
        super(context);
    }

    @Override
    public Map<String, Object> getDataMap() {
        return null;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public ImplParser getParser() {
        return new ParserHome();
    }
}
