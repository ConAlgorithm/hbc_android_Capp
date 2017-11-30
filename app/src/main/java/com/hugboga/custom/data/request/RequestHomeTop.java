package com.hugboga.custom.data.request;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.HomeTopBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;
import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qingcha on 17/11/22.
 * http://wiki.hbc.tech/pages/viewpage.action?pageId=8556453#CAPP首页聚合信息-hide-header
 */

@HttpRequest(path = UrlLibs.API_TOP_DRAWER, builder = NewParamsBuilder.class)
public class RequestHomeTop extends BaseRequest<List<HomeTopBean>> {

    public RequestHomeTop(Context context ) {
        super(context);
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(UrlLibs.API_TOP_DRAWER, new TypeToken<ArrayList<HomeTopBean>>(){}.getType());
    }

    @Override
    public String getUrlErrorCode() {
        return "40193";
    }

}