package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.net.HbcParamsBuilder;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.SkuCityBean;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParserSkuCity;

import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2016/3/3.
 */
@HttpRequest(
        path = UrlLibs.SERVER_IP_CITY_SKU,
        builder = HbcParamsBuilder.class)
public class RequestSkuList extends BaseRequest<SkuCityBean> {

    private final String cityId;

    public RequestSkuList(Context context, String cityId) {
        super(context);
        this.cityId = cityId;
    }

    @Override
    public Map<String, Object> getDataMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("cityId", cityId);
        return map;
    }

    @Override
    public ImplParser getParser() {
        return new ParserSkuCity();
    }
}
