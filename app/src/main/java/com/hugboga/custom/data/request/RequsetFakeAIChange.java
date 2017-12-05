package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.FakeAIBean;
import com.hugboga.custom.data.bean.FakeAIQuestionsBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;
import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/12/4.
 */
@HttpRequest(path = UrlLibs.API_IP_FAKE_AI_POST, builder = NewParamsBuilder.class)
public class RequsetFakeAIChange extends BaseRequest<FakeAIQuestionsBean> {
    public RequsetFakeAIChange(Context context) {
        super(context);
        map = new HashMap<String, Object>();
        //map.put("orderNo", orderId);

    }
    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }
    @Override
    public String getUrlErrorCode() {
        return "40195";
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(UrlLibs.API_IP_FAKE_AI_POST,FakeAIQuestionsBean.class);
    }
}

