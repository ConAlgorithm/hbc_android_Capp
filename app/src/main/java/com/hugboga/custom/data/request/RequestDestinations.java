package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.HomeBeanV2;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by SPW on 2017/3/11.
 */
@HttpRequest(path = UrlLibs.API_DESTINATIONS, builder = NewParamsBuilder.class)
public class RequestDestinations extends BaseRequest<HomeBeanV2.DestinationAggregation> {

    public RequestDestinations(Context context, int offset) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("destinationOffset",offset);
        map.put("destinationLimit",RequestHome.HOME_DEFALT_LIMIT);
    }


    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public ImplParser getParser() {
        return  new HbcParser(UrlLibs.API_DESTINATIONS, HomeBeanV2.DestinationAggregation.class);
    }

    @Override
    public String getUrlErrorCode() {
        return "40132";
    }
}
