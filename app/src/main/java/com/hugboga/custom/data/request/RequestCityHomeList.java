package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CityListBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;
import org.xutils.http.annotation.HttpRequest;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/10/18.
 */
@HttpRequest(path = UrlLibs.API_CITY_HOME_LIST, builder = NewParamsBuilder.class)
public class RequestCityHomeList extends BaseRequest<CityListBean> {

    public RequestCityHomeList(Context context, String cityId) {
        super(context);
        map = new HashMap<>();
        map.put("source", Constants.REQUEST_SOURCE);
        map.put("channelId","18");
        map.put("cityId", cityId);
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(UrlLibs.API_CITY_HOME_LIST, CityListBean.class);
    }

    @Override
    public String getUrlErrorCode() {
        return "40047";
    }

}
