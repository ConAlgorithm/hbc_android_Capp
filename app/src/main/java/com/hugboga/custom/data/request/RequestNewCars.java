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
    public RequestNewCars(Context context, int serviceType, String guideId, String guideCarId, int limit, int offset) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("guideId", guideId);
        map.put("guideCarId", guideCarId);
        map.put("serviceType", serviceType);
        map.put("limit",limit);
        map.put("offset",offset);
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
