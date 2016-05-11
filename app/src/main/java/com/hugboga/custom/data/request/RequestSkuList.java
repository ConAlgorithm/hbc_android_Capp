package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParserSkuCity;

import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;
import java.util.Objects;

/**
 * Created by admin on 2016/3/3.
 */
@HttpRequest(
        path = UrlLibs.SERVER_IP_CITY_SKU,
        builder = NewParamsBuilder.class)
public class RequestSkuList extends BaseRequest<Objects[]> {

    private final String cityId;

    public RequestSkuList(Context context, String cityId) {
        super(context);
        this.cityId = cityId;
        map = new HashMap<>();
        map.put("cityId", cityId);
    }

    @Override
    public ImplParser getParser() {
        return new ParserSkuCity();
    }
}
