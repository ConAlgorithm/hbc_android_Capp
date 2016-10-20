package com.hugboga.custom.data.request;

import android.content.Context;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by on 16/10/17.
 */
@HttpRequest(path = UrlLibs.API_REPORT, builder = NewParamsBuilder.class)
public class RequestReport extends BaseRequest<String> {

    public RequestReport(Context context, String body) {
        super(context);
        map = new HashMap<String, Object>();
        bodyEntity = body;
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
        return "40106";
    }
}