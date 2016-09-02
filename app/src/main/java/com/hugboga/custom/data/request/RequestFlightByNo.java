package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.FlightBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParserFlightByNo;

import org.xutils.http.annotation.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/3/22.
 */
@HttpRequest(path = UrlLibs.SERVER_IP_FLIGHTS_BY_NO, builder = NewParamsBuilder.class)
public class RequestFlightByNo extends BaseRequest<ArrayList<FlightBean>> {
    public String no;

    public RequestFlightByNo(Context context, String no, String date, int orderType) {
        super(context);
        this.no = no;
        map = new HashMap<String, Object>();
        map.put("flightNo", no);
        map.put("date", date);
        map.put("orderType", orderType);
    }

    @Override
    public ImplParser getParser() {
        return new ParserFlightByNo();
    }

    @Override
    public String getUrlErrorCode() {
        return "40040";
    }
}
