package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.GuideExtinfoBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by qingcha on 17/4/24.
 *
 * http://wiki.hbc.tech/display/hbcAdmin/C4.0.0#C4.0.0-8.分享司导个人页附加信息
 */
@HttpRequest(path = UrlLibs.API_GUIDE_EXTINFO, builder = NewParamsBuilder.class)
public class RequestGuideExtinfo extends BaseRequest<GuideExtinfoBean> {


    public RequestGuideExtinfo(Context context, String guideId) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("guideId", guideId);
        if (UserEntity.getUser().isLogin(context)) {
            map.put("userId", UserEntity.getUser().getUserId(context));
        }
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(UrlLibs.API_GUIDE_EXTINFO, GuideExtinfoBean.class);
    }

    @Override
    public String getUrlErrorCode() {
        return "40141";
    }

}