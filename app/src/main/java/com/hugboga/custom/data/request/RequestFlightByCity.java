package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.net.HbcParamsBuilder;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.FlightBean;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParserFlightByCity;

import org.xutils.http.annotation.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/3/22.
 */
@HttpRequest(path = UrlLibs.SERVER_IP_FLIGHTS_BY_CITY, builder = HbcParamsBuilder.class)
public class RequestFlightByCity extends BaseRequest<ArrayList<FlightBean>> {
    public RequestFlightByCity(Context context, int depCity, int arrCity, String date) {
        super(context);
        map = new HashMap<String,Object>();
        map.put("depCityId",depCity);
        map.put("arrCityId",arrCity);
        map.put("flightDate", date);
    }

    @Override
    public ImplParser getParser() {
        return new ParserFlightByCity();
    }
}
