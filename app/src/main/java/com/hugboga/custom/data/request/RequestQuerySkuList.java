package com.hugboga.custom.data.request;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.city.DestinationGoodsVo;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;
import java.util.List;

/**
 * Created by HONGBO on 2017/11/29 11:36.
 */
@HttpRequest(path = UrlLibs.API_CITY_QUERY_SKU_LIST, builder = NewParamsBuilder.class)
public class RequestQuerySkuList extends BaseRequest {

    public RequestQuerySkuList(Context context, int destinationId, int destinationType,
                               int dayCountTypeList, int destinationTagIdList, int depCityIdList) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("destinationId", destinationId); //目的地ID
        map.put("destinationType", destinationType); //目的地类型
        map.put("dayCountTypeList", dayCountTypeList); //游玩天数类型列表,全部为空
        map.put("destinationTagIdList", destinationTagIdList); //目的地标签列表,全部为空
        map.put("depCityIdList", depCityIdList); //出发城市列表,全部为空
        map.put("offset", 0); //TODO 起始项 默认值：0
        map.put("limit", 10); //TODO 每页数量 默认值：10
    }

    @Override
    public String getUrlErrorCode() {
        return "40195";
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(UrlLibs.API_CITY_QUERY_SKU_LIST, new TypeToken<List<DestinationGoodsVo>>() {
        }.getType());
    }
}
