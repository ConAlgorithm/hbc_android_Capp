package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.city.DestinationHomeVo;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by HONGBO on 2017/11/28 10:49.
 */
@HttpRequest(path = UrlLibs.API_CITY_DESTINATION_HOME, builder = NewParamsBuilder.class)
public class RequestCity extends BaseRequest<DestinationHomeVo> {

    public RequestCity(Context context, Integer destinationId, Integer destinationType) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("destinationId", destinationId);
        map.put("destinationType", destinationType);
    }

    @Override
    public String getUrlErrorCode() {
        return "4194";
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(UrlLibs.API_CITY_DESTINATION_HOME, DestinationHomeVo.class);
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }
}
