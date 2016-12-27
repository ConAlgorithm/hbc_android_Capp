package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.data.bean.ChooseGuideMessageBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by qingcha on 16/12/23.
 *
 * WIKI: http://wiki.hbc.tech/pages/viewpage.action?pageId=7931892
 */
@HttpRequest(path = UrlLibs.GUIDE_CHOOSE, builder = NewParamsBuilder.class)
public class RequestChooseGuide extends BaseRequest<ChooseGuideMessageBean> {

    public RequestChooseGuide(Context context, String allocatGno, String orderNo) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("userName", UserEntity.getUser().getUserName(MyApplication.getAppContext()));
        map.put("allocatGno", allocatGno);
        map.put("orderNo", orderNo);
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(UrlLibs.GUIDE_CHOOSE, ChooseGuideMessageBean.class);
    }

    @Override
    public String getUrlErrorCode() {
        return "40111";
    }

}