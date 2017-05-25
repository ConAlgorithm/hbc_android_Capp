package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.GuideCarBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParseGuideCar;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;

@HttpRequest(path = UrlLibs.API_CARS, builder = NewParamsBuilder.class)
public class RequestNewCars extends BaseRequest<ArrayList<GuideCarBean>> {
    public RequestNewCars(Context context, int serviceType, String guideId, String guideCarId) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("guideId", guideId);
        map.put("guideCarId", guideCarId);  // 司导的车id，如果有，会对这个司导的车优先排序
        map.put("serviceType", serviceType);// (1-接机;2-送机;3-按天包车;4-单次接送;6-小长途;7-大长途)
        errorType = ERROR_TYPE_IGNORE;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public ImplParser getParser() {
        return new ParseGuideCar();
    }

    @Override
    public String getUrlErrorCode() {
        return "400142";
    }
}
