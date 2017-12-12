package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.ai.FakeAIBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/12/1.
 */
@HttpRequest(path = UrlLibs.API_IP_FAKE_AI_GET, builder = NewParamsBuilder.class)
public class RaquestFakeAI extends BaseRequest<FakeAIBean> {

    public RaquestFakeAI(Context context,String distinctId) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("distinctId",distinctId);
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public String getUrlErrorCode() {
        return "40197";
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(UrlLibs.API_IP_FAKE_AI_GET, FakeAIBean.class);
    }
}
