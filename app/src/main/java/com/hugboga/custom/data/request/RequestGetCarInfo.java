package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.data.bean.CarInfoBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParseGetCarInfo;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created  on 16/4/16.
 */
@HttpRequest(path = UrlLibs.GET_CAR_INFOS,builder = NewParamsBuilder.class)
public class RequestGetCarInfo extends BaseRequest<CarInfoBean> {

/**
    http://api.dev.hbc.tech/price/v1.2/e/dailyPrice?
    // endCityId=204&startCityId=204&channelId=1925283890
    // &startDate=2016-05-12%2019:30:22&endDate=2016-05-13%2000:00:00
    // &halfDay=0&adultNum=4&childrenNum=1&childseatNum=0
    // &luggageNum=0&passCities=1_1_204,1_1_2042
 **/
    public RequestGetCarInfo(Context context,String startCityId,String endCityId,String startDate,String endDate,String halfDay,String adultNum,String childrenNum,String childseatNum,String luggageNum,String passCities,String channelId) {
        super(context);
        map = new HashMap<String,Object>();
        try {
            map.put("startCityId", startCityId);
            map.put("endCityId", endCityId);
            map.put("startDate", startDate);
            map.put("endDate", endDate);
            map.put("halfDay", halfDay);
            map.put("adultNum", adultNum);
            map.put("childrenNum", childrenNum);
            map.put("childseatNum", childseatNum);
            map.put("luggageNum", luggageNum);
            map.put("passCities", passCities);
            map.put("channelId",channelId);
        }catch (Exception e){
            MLog.e(e.toString());
        }
    }



    @Override
    public ImplParser getParser() {
        return new ParseGetCarInfo();
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

}
