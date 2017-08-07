package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.HomeAggregationVo4;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

/**
 * Created by zhangqiang on 17/8/4.
 */
@HttpRequest(path = UrlLibs.API_HOME_NEW, builder = NewParamsBuilder.class)
public class RequestHomeNew extends BaseRequest {

    public RequestHomeNew(Context context) {
        super(context);
    }


    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(UrlLibs.API_HOME, HomeAggregationVo4.class);
    }

    @Override
    public String getUrlErrorCode() {
        return "500170";
    }
}
