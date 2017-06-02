package com.hugboga.custom.data.request;

import android.content.Context;
import android.text.TextUtils;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParserCheckPrice;
import com.hugboga.custom.utils.Config;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by qingcha on 17/6/1.
 */
@HttpRequest(path = UrlLibs.API_AIRPORT_PICKUP_PRICE, builder = NewParamsBuilder.class)
public class RequestSeckillsPickupPrice extends BaseRequest {

    public RequestSeckillsPickupPrice(Context context, Builder builder) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("channelId", Config.channelId);
        map.put("userId", UserEntity.getUser().getUserId(context));
        if (!TextUtils.isEmpty(builder.carIds)) {
            map.put("carIds", builder.carIds);
        }
        map.put("timeLimitedSaleNo", builder.timeLimitedSaleNo);//秒杀活动编号
        map.put("timeLimitedSaleScheduleNo", builder.timeLimitedSaleScheduleNo);//秒杀活动场次编号
        map.put("airportCode", builder.airportCode);//机场三字码
        map.put("serviceDate", builder.serviceDate);//服务时间 格式：yyyy-MM-dd HH:mm:ss
        map.put("startLocation", builder.startLocation);//起点坐标(纬度,经度) 注：即机场坐标
        map.put("startAddress", builder.startAddress);//起点地址
        map.put("startDetailAddress", builder.startDetailAddress);//startDetailAddress
        map.put("endLocation", builder.endLocation);//终点坐标(纬度,经度)
        map.put("endAddress", builder.endAddress);//终点地址
        map.put("endDetailAddress", builder.endDetailAddress);//终点地址详情
        map.put("specialCarsIncluded", 1);
        map.put("priceLevel", 1);
        errorType = ERROR_TYPE_IGNORE;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public ImplParser getParser() {
        return new ParserCheckPrice();
    }

    @Override
    public String getUrlErrorCode() {
        return "40148";
    }

    public static class Builder {
        public String carIds;
        public String timeLimitedSaleNo;
        public String timeLimitedSaleScheduleNo;
        public String airportCode;
        public String serviceDate;
        public String startLocation;
        public String startAddress;
        public String startDetailAddress;
        public String endLocation;
        public String endAddress;
        public String endDetailAddress;
    }
}
