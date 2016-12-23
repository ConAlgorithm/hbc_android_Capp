package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.ChooseGuideMessageBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by qingcha on 16/12/23.
 *
 * WIKI: http://wiki.hbc.tech/display/admin/CAPP+3.2.0
 */
@HttpRequest(path = UrlLibs.GUIDE_CROP_VALID, builder = NewParamsBuilder.class)
public class RequestGuideCropValid extends BaseRequest<ChooseGuideMessageBean> {

    public RequestGuideCropValid(Context context, String guideId, int cityId, String allocatGno, String orderNo) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("guideId", guideId);
        map.put("cityId", cityId);
        map.put("allocatGno", allocatGno);
        map.put("orderNo", orderNo);
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(UrlLibs.GUIDE_CROP_VALID, ChooseGuideMessageBean.class);
    }

    @Override
    public String getUrlErrorCode() {
        return "40110";
    }

}