package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.HomeBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

/**
 * 首页
 * Created by qingcha on 2017/11/22.
 * http://wiki.hbc.tech/pages/viewpage.action?pageId=8556453
 */
@HttpRequest(path = UrlLibs.API_HOME, builder = NewParamsBuilder.class)
public class RequestHome extends BaseRequest<HomeBean> {

    public RequestHome(Context context ) {
        super(context);
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(UrlLibs.API_HOME, HomeBean.class);
    }

    @Override
    public String getUrlErrorCode() {
        return "40048";
    }

}
