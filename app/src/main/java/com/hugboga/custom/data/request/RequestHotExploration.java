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
@HttpRequest(path = UrlLibs.API_HOTEXPLORATIONS, builder = NewParamsBuilder.class)
public class RequestHotExploration extends BaseRequest<HomeBeanV2.HotExplorationAggregation> {


    public RequestHotExploration(Context context,int offset) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("explorationLimit", RequestHome.HOME_DEFALT_LIMIT);
        map.put("explorationOffset",offset);
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public ImplParser getParser() {
        return  new HbcParser(UrlLibs.API_HOTEXPLORATIONS, HomeBeanV2.HotExplorationAggregation.class);
    }

    @Override
    public String getUrlErrorCode() {
        return "40123";
    }
}
