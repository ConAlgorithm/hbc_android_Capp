package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.utils.Config;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/3/21.
 */
public class RequestCheckPrice extends BaseRequest {
    private int orderType;

    public RequestCheckPrice(Context context, int orderType,String airportCode, Integer cityId,  String startLocation, String endLocation, String date) {
        super(context);
        map = new HashMap<String,Object>();
        this.orderType = orderType;
        try {
            map.put("airportCode", airportCode);
            map.put("cityId", cityId);
            if(date!=null)
                map.put("serviceDate", date+":00");
            map.put("startLocation", startLocation);
            map.put("endLocation", endLocation);
            map.put("channelId", Config.channelId);
            map.put("assitCheckIn", 1);
        } catch (Exception e) {
            MLog.e(e.toString());
        }
    }

    @Override
    public ImplParser getParser() {
        return null;
    }
}
