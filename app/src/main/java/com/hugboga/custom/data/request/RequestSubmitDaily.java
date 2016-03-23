package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.net.HbcParamsBuilder;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.net.UrlLibs;

import org.json.JSONObject;
import org.xutils.http.annotation.HttpRequest;

/**
 * Created by admin on 2016/3/22.
 */
@HttpRequest(path = UrlLibs.SERVER_IP_SUBMIT_DAILY, builder = HbcParamsBuilder.class)
public class RequestSubmitDaily extends RequestSubmitBase{
    public RequestSubmitDaily(Context context, OrderBean orderBean) {
        super(context, orderBean);
        map.put("startCityId", orderBean.serviceCityId);
        map.put("startCityName", orderBean.serviceCityName);
        map.put("destCityId", orderBean.serviceEndCityid);
        map.put("destCityName", orderBean.serviceEndCityName);
        map.put("serviceDate", orderBean.serviceTime);
        map.put("serviceEndDate", orderBean.serviceEndTime);
        map.put("serviceTimeL", null);
        map.put("serviceEndTimeL", null);
        map.put("oneCityTravel",orderBean.oneCityTravel);
        map.put("isHalfDaily",orderBean.isHalfDaily);
        map.put("serviceLocalDays",orderBean.inTownDays);
        map.put("serviceNonlocalDays",orderBean.outTownDays);
        map.put("journeyComment",orderBean.journeyComment);
        map.put("serviceDate", orderBean.serviceTime);
        map.put("serviceEndDate", orderBean.serviceEndTime);
        map.put("serviceDepartTime", orderBean.serviceTime+" "+orderBean.serviceStartTime+":00");
        map.put("servicePassCitys", orderBean.stayCityListStr);
        map.put("passbyPois", orderBean.skuPoi);

    }
}
