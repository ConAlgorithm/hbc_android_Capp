package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.ActivityBuyNowBean;
import com.hugboga.custom.data.bean.HomeBeanV2;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/*
* http://wiki.hbc.tech/pages/viewpage.action?pageId=8552989
* */
@HttpRequest(path = UrlLibs.API_ACTIVITY_BUYNOW, builder = NewParamsBuilder.class)
public class RequestActivityBuyNow extends BaseRequest<ActivityBuyNowBean> {

    public RequestActivityBuyNow(Context context, String activityNo, String activityItemNo) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("userId", UserEntity.getUser().getUserId(context));
        map.put("activityNo", activityNo);
        map.put("activityItemNo", activityItemNo);
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(UrlLibs.API_ACTIVITY_BUYNOW, ActivityBuyNowBean.class);
    }

    @Override
    public String getUrlErrorCode() {
        return "40147";
    }

}