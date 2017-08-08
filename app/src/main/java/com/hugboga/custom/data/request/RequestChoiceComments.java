package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.ChoiceCommentsBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by qingcha on 17/8/7.
 */

@HttpRequest(path = UrlLibs.API_CHOICE_COMMENTS, builder = NewParamsBuilder.class)
public class RequestChoiceComments extends BaseRequest<ChoiceCommentsBean> {

    public RequestChoiceComments(Context context, int offset) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("offset", offset);
        map.put("limit", Constants.DEFAULT_PAGESIZE);
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(UrlLibs.API_CHOICE_COMMENTS, ChoiceCommentsBean.class);
    }

    @Override
    public String getUrlErrorCode() {
        return "40174";
    }

}