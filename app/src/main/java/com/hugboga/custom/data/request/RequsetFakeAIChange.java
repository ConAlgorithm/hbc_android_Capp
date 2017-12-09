package com.hugboga.custom.data.request;

import android.content.Context;

import com.google.gson.Gson;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.ai.FakeAIQuestionsBean;
import com.hugboga.custom.data.bean.ai.AiRequestInfo;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

/**
 * Created by Administrator on 2017/12/4.
 */
@HttpRequest(path = UrlLibs.API_IP_FAKE_AI_POST, builder = NewParamsBuilder.class)
public class RequsetFakeAIChange extends BaseRequest<FakeAIQuestionsBean> {

    public RequsetFakeAIChange(Context context, AiRequestInfo info) {
        super(context);
        bodyEntity = new Gson().toJson(info);
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public String getUrlErrorCode() {
        return "40196";
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(UrlLibs.API_IP_FAKE_AI_POST, FakeAIQuestionsBean.class);
    }
}

