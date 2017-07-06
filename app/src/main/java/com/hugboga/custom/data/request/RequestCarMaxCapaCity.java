package com.hugboga.custom.data.request;

import android.content.Context;
import android.text.TextUtils;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.CarMaxCapaCityBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;
import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;
import java.util.HashMap;

/**
 * http://wiki.hbc.tech/pages/viewpage.action?pageId=7933324
 * */
@HttpRequest(path = UrlLibs.API_CAR_MAX_CAPACITY, builder = NewParamsBuilder.class)
public class RequestCarMaxCapaCity extends BaseRequest<CarMaxCapaCityBean> {

    public String activityNo;

    public RequestCarMaxCapaCity(Context context, int cityId, String carIds, String activityNo) {
        super(context);
        this.activityNo = activityNo;
        map = new HashMap<String, Object>();
        map.put("cityId", cityId);
        if (!TextUtils.isEmpty(carIds)) {
            map.put("carIds", carIds);
        }
        if (!TextUtils.isEmpty(activityNo)) {
            map.put("activityNo", activityNo);
        }
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getUrl() {
        return TextUtils.isEmpty(activityNo) ? UrlLibs.API_CAR_MAX_CAPACITY : UrlLibs.API_CAR_MAX_CAPACITY_SECKILLS;
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(getUrl(), CarMaxCapaCityBean.class);
    }

    @Override
    public String getUrlErrorCode() {
        return TextUtils.isEmpty(activityNo) ? "40118" : "40166";
    }

}