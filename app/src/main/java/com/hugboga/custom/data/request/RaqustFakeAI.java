package com.hugboga.custom.data.request;

import android.content.Context;

import com.google.gson.Gson;
import com.huangbaoche.hbcframe.data.net.HbcParamsBuilder;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.FakeAIBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;

import org.json.JSONObject;
import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;
import org.xutils.http.app.ParamsBuilder;

/**
 * Created by Administrator on 2017/12/1.
 */
@HttpRequest(path = UrlLibs.API_IP_FAKE_AI_GET, builder = NewParamsBuilder.class)
public class RaqustFakeAI extends BaseRequest<FakeAIBean> {
    public RaqustFakeAI(Context context) {
        super(context);

    }
    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }
    @Override
    public String getUrlErrorCode() {
        return "40194";
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(UrlLibs.API_IP_FAKE_AI_GET,FakeAIBean.class);
    }
}
