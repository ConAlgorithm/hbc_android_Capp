package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.bean.UserSession;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.ImShadowBean;
import com.hugboga.custom.data.bean.ai.FakeAIBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;
import com.hugboga.custom.data.parser.ParserNewOrder;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by zhangqi on 2017/12/28.
 */

@HttpRequest(path = UrlLibs.IM_FIRST_TALKED, builder = NewParamsBuilder.class)
public class RequestImFirstChat extends BaseRequest<ImShadowBean> {

    public RequestImFirstChat(Context context, String userId, String guideId) {
        super(context);
        map = new HashMap<String, Object>();
        try {
            map.put("self", userId);
            map.put("selfType", "2");
            map.put("target", guideId);
            map.put("targetType", "1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(UrlLibs.IM_FIRST_TALKED, ImShadowBean.class);
    }

    @Override
    public String getUrlErrorCode() {
        return "40205";
    }
}
