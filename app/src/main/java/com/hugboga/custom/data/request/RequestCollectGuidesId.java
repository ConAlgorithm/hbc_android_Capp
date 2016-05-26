package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by qingcha on 16/5/24.
 */
@HttpRequest(path = UrlLibs.COLLECT_GUIDES_ID, builder = NewParamsBuilder.class)
public class RequestCollectGuidesId extends BaseRequest {

    public RequestCollectGuidesId(Context context) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("source", 1);
        map.put("userId", UserEntity.getUser().getUserId(context));
//        map.put("guideId", offset);//司导ID
//        map.put("sharingUserId", limit);//分享用户ID

    }

    @Override
    public ImplParser getParser() {
        return null;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }
}
