package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.activity.CombinationOrderActivity;
import com.hugboga.custom.data.bean.CarListBean;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.CityRouteBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;
import com.hugboga.custom.utils.CharterDataUtils;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.utils.JsonUtils;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by qingcha on 17/3/4.
 * 组合单v1.4查报价
 * http://wiki.hbc.tech/pages/viewpage.action?pageId=7933731#id-%E7%BB%84%E5%90%88%E5%8D%95v1.4-batch-p-v1.4-daily-dayarrangement
 */

@HttpRequest(path = UrlLibs.API_BATCH_PRICE, builder = NewParamsBuilder.class)
public class RequestBatchPrice extends BaseRequest<CarListBean> {

    public RequestBatchPrice(Context context, CharterDataUtils charterDataUtils) {
        super(context);
        map = new HashMap<String, Object>();
        bodyEntity = getRequestParamsBody(charterDataUtils);
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(UrlLibs.API_BATCH_PRICE, CarListBean.class);
    }

    @Override
    public String getUrlErrorCode() {
        return "40121";
    }


    public String getRequestParamsBody(CharterDataUtils charterDataUtils) {

        int index = 1;
        int dayArrangementIndex = 0;
        ArrayList<BatchPrice> batchPriceList = new ArrayList<BatchPrice>();

        ArrayList<CityRouteBean.CityRouteScope> travelList = charterDataUtils.travelList;
        int size = travelList.size();
        DailyPriceParam dailyPriceParam = null;
        for (int i = 0; i < size; i++) {
            CityRouteBean.CityRouteScope cityRouteScope = travelList.get(i);
            if (cityRouteScope.routeType == CityRouteBean.RouteType.PICKUP) {//只接机
                AirportParam airportParam = new AirportParam();
                airportParam.airportCode = charterDataUtils.flightBean.arrAirportCode;
                airportParam.serviceDate = charterDataUtils.chooseDateBean.start_date + " "+ CombinationOrderActivity.SERVER_TIME;
                airportParam.startLocation = charterDataUtils.flightBean.arrLocation;
                airportParam.endLocation = charterDataUtils.pickUpPoiBean.location;
                BatchPrice batchPrice = new BatchPrice();
                batchPrice.param = airportParam;
                batchPrice.serviceType = SERVICE_TYPE_PICKUP;
                batchPrice.index = index;
                batchPriceList.add(batchPrice);
                index++;
            } else if (cityRouteScope.routeType == CityRouteBean.RouteType.SEND) {
                AirportParam sendParam = new AirportParam();
                sendParam.airportCode = charterDataUtils.airPortBean.airportCode;
                sendParam.serviceDate = charterDataUtils.chooseDateBean.end_date + " " + charterDataUtils.sendServerTime + ":00";
                sendParam.startLocation = charterDataUtils.sendPoiBean.location;
                sendParam.endLocation = charterDataUtils.airPortBean.location;
                BatchPrice batchPrice = new BatchPrice();
                batchPrice.param = sendParam;
                batchPrice.serviceType = SERVICE_TYPE_SEND;
                batchPrice.index = index;
                batchPriceList.add(batchPrice);
            } else if (cityRouteScope.routeType == CityRouteBean.RouteType.AT_WILL) {
                continue;
            } else {
                if (dailyPriceParam == null) {
                    dailyPriceParam = new DailyPriceParam();
                }
                if (dailyPriceParam.arrangements.size() == 0) {
                    dayArrangementIndex = i;
                }
                CityBean startCityBean = charterDataUtils.getStartCityBean(i + 1);
                DayArrangement dayArrangement = new DayArrangement();
                dayArrangement.tourType = getTourType(cityRouteScope.routeType);
                dayArrangement.startCityId = startCityBean.cityId;
                if (i == 0 && charterDataUtils.isSelectedPickUp && charterDataUtils.flightBean != null) {
                    dayArrangement.airportCode = charterDataUtils.flightBean.arrAirportCode;
                    dayArrangement.airportLocation = charterDataUtils.flightBean.arrLocation;
                    dayArrangement.serviceDate = DateUtils.getDay(charterDataUtils.chooseDateBean.start_date, dayArrangementIndex) + " " + charterDataUtils.flightBean.arrivalTime + ":00";
                    dayArrangement.airportServiceType = 1;
                } else if(i == size - 1 && charterDataUtils.isSelectedSend && charterDataUtils.airPortBean != null) {
                    dayArrangement.airportCode = charterDataUtils.airPortBean.airportCode;
                    dayArrangement.airportLocation = charterDataUtils.airPortBean.location;
                    dayArrangement.airportServiceType = 2;
                }
                if (cityRouteScope.routeType == CityRouteBean.RouteType.OUTTOWN) {
                    dayArrangement.endCityId = charterDataUtils.getEndCityBean(i + 1).cityId;
                } else {
                    dayArrangement.endCityId = startCityBean.cityId;
                }
                dailyPriceParam.arrangements.add(dayArrangement);
                 /*
                 * 拆单情况
                 * 1 如果第一天仅接机，那么第一天需要与第二天拆开
                 * 2 如果最后一天仅送机，最后一天需要与倒数第二天拆开
                 * 3 如果当前一天的开始城市不等于前一天的结束城市，那么当前一天需要与前一天拆开
                 * 4 如果当前一天或连续几天自己转转不包车，那么空白的行程前后都需要拆开
                 */
                boolean isBatch = charterDataUtils.getStartCityBean(i + 1) != charterDataUtils.getStartCityBean(i + 2);//判断当天出发城市和明天开始城市是否相同，不相同，拆单
                if ((i + 1 >= size || travelList.get(i + 1).routeType == CityRouteBean.RouteType.AT_WILL)
                        || travelList.get(i + 1).routeType == CityRouteBean.RouteType.SEND
                        || cityRouteScope.routeType == CityRouteBean.RouteType.OUTTOWN || isBatch) {
                    CityBean _startCityBean = charterDataUtils.getStartCityBean(dayArrangementIndex + 1);
                    dailyPriceParam.startCityId = _startCityBean.cityId;
                    dailyPriceParam.startLocation = _startCityBean.location;
                    dailyPriceParam.startDate = DateUtils.getDay(charterDataUtils.chooseDateBean.start_date, dayArrangementIndex) + " " + CombinationOrderActivity.SERVER_TIME;
                    CityBean endCityBean = null;
                    if (cityRouteScope.routeType == CityRouteBean.RouteType.OUTTOWN) {
                        endCityBean = charterDataUtils.getEndCityBean(i + 1);
                    } else {
                        endCityBean = charterDataUtils.getStartCityBean(i + 1);
                    }
                    dailyPriceParam.endCityId = endCityBean.cityId;
                    dailyPriceParam.endLocation = endCityBean.location;
                    dailyPriceParam.endDate = DateUtils.getDay(charterDataUtils.chooseDateBean.start_date, i) + " " + CombinationOrderActivity.SERVER_TIME_END;

                    BatchPrice batchPrice = new BatchPrice();
                    batchPrice.param = dailyPriceParam;
                    batchPrice.serviceType = SERVICE_TYPE_CHARTER;
                    batchPrice.index = index;
                    batchPriceList.add(batchPrice);
                    index++;
                    dailyPriceParam = new DailyPriceParam();
                }
            }
        }
        BatchPriceListBean batchPriceListBean = new BatchPriceListBean();
        batchPriceListBean.batchPrice = batchPriceList;
        return JsonUtils.toJson(batchPriceListBean);
    }


    public static int getTourType(int routeType) {
        switch (routeType) {
            case CityRouteBean.RouteType.HALFDAY://半日
                return 0;
            case CityRouteBean.RouteType.URBAN://市内
                return 1;
            case CityRouteBean.RouteType.SUBURBAN://周边
                return 2;
            case CityRouteBean.RouteType.OUTTOWN://跨城
                return 3;
            default:
                return -1;
        }
    }

    // serviceType 服务类型服务端定义
    public int SERVICE_TYPE_PICKUP = 1;     // 1：接机
    public int SERVICE_TYPE_SEND = 2;       // 2：送机
    public int SERVICE_TYPE_CHARTER = 3;    // 3：包车

    public static class BatchPriceListBean implements Serializable {
        ArrayList<BatchPrice> batchPrice;
    }

    public static class BatchPrice implements Serializable {
        public int serviceType;                 // 服务类型
        public Serializable param;              // 服务所对应的参数
        public int index;                       // 当前服务索引序号
    }

    public static class AirportParam implements Serializable {
        public String airportCode;          // 机场三字码
        public String serviceDate;          // 服务时间  格式：yyyy-MM-dd HH:mm:ss
        public String startLocation;        // 起始点坐标  格式：纬度,经度
        public String endLocation;          // 结束点坐标  格式：纬度,经度
        public Long channelId = 18l;        // 渠道ID
        public String carIds;               // 否 车型列表 格式 车型ID,车型ID  e.g: 100005,200005
        public int source = 1;              // 来源
        public int specialCarsIncluded = 1; // 是否包含特殊车型,默认不包含1.包含 0.不包含
    }

    public static class DailyPriceParam implements Serializable {
        public Long channelId = 18l;                 // 渠道ID
        public String carIds;                        // 否 指定报价车型 格式：车型ID,车型ID
        public int specialCarsIncluded = 1;          // 是否包含特殊车型 0.不包含 1.包含 默认不包含
        public int startCityId;                      // 出发城市ID
        public int endCityId;                        // 结束城市ID
        public String startLocation;                 // 否 起点坐标(纬度,经度) 默认开始城市坐标
        public String endLocation;                   // 否 终点坐标(纬度,经度) 默认结束城市坐标
        public String startDate;                     // 服务开始时间 格式:yyyy-MM-dd HH:mm:ss
        public String endDate;                       // 服务结束时间 格式:yyyy-MM-dd HH:mm:ss 半日包可不传
        public List<DayArrangement> arrangements;    // 每日行程安排

        public DailyPriceParam() {
            this.arrangements = new ArrayList<DayArrangement>();
        }
    }

    public static class DayArrangement implements Serializable {
        public String airportCode;          // 否 当天日程包含接/送机时必选
        public String airportLocation;      // 否 如果前端传入location，则验证时候必选
        public String goodsNo;              // 否 当天日程是线路时必选
        public String serviceDate;          // 否 服务时间
        public int startCityId;             // 开始城市ID
        public int endCityId;               // 当天结束城市ID
        public int tourType;                // 否 游玩类型 0.半日 1.市内 2.周边 3.出城 4.推荐线路
        public Integer airportServiceType;  // 机场服务类型 1.接机 2.送机
    }

}