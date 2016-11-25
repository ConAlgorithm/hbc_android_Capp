package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.ServiceQuestionBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by qingcha on 16/11/14.
 */
@HttpRequest(path = UrlLibs.API_SERVICE_QUESTION_LIST, builder = NewParamsBuilder.class)
public class RequsetServiceQuestionList extends BaseRequest<ServiceQuestionBean> {

    public RequsetServiceQuestionList(Context context, int source) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("source", source);
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(UrlLibs.API_SERVICE_QUESTION_LIST, ServiceQuestionBean.class);
    }

    @Override
    public String getUrlErrorCode() {
        return "40107";
    }

}