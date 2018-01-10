package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.ChatJudgeBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

@HttpRequest(path = UrlLibs.API_CHAT_JUDGE, builder = NewParamsBuilder.class)
public class RequestChatJudge extends BaseRequest<ChatJudgeBean> {

    public RequestChatJudge(Context context, String guideId) {
        super(context);
        map = new HashMap<>();
        map.put("userId", UserEntity.getUser().getUserId(context));
        map.put("guideId", guideId);
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(UrlLibs.API_CHAT_JUDGE, ChatJudgeBean.class);
    }

    @Override
    public String getUrlErrorCode() {
        return "40206";
    }

}