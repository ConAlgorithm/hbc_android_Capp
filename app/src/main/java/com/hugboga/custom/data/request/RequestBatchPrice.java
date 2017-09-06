package com.hugboga.custom.data.request;

import android.content.Context;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.activity.CombinationOrderActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CarListBean;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.CityRouteBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;
import com.hugboga.custom.utils.CharterDataUtils;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.utils.JsonUtils;
import com.hugboga.custom.utils.LogUtils;

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

    private boolean isSeckills = false;
    private Context context;

    public RequestBatchPrice(Context _context, CharterDataUtils charterDataUtils) {
        super(_context);
        this.context = _context;
        map = new HashMap<String, Object>();
        bodyEntity = getRequestParamsBody(charterDataUtils);
        LogUtils.e("组合单报价 params = " + bodyEntity);
        errorType = ERROR_TYPE_IGNORE;
        isSeckills = charterDataUtils.isSeckills();
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
        if (isSeckills) {
            return "40164";
        } else {
            return "40121";
        }
    }

    @Override
    public String getUrl() {
        if (isSeckills) {
            return UrlLibs.API_SECKILLS_BATCH_PRICE;
        } else {
            return UrlLibs.API_BATCH_PRICE;
        }
    }

    public String getRequestParamsBody(CharterDataUtils charterDataUtils) {

        int index = 1;
        int dayArrangementIndex = 0;
        ArrayList<BatchPrice> batchPriceList = new ArrayList<BatchPrice>();

        int isOuttown = -1;
        int fitstOrderGoodsType = -1;

        RequestCheckGuide.CheckGuideBeanList checkGuideBeanList = new RequestCheckGuide.CheckGuideBeanList();

        ArrayList<CityRouteBean.CityRouteScope> travelList = charterDataUtils.travelList;
        int size = travelList.size();
        DailyPriceParam dailyPriceParam = null;
        for (int i = 0; i < size; i++) {
            CityRouteBean.CityRouteScope cityRouteScope = travelList.get(i);
            if (cityRouteScope.routeType == CityRouteBean.RouteType.PICKUP) {//只接机
                AirportParam airportParam = new AirportParam();
                airportParam.airportCode = charterDataUtils.flightBean.arrAirportCode;
                airportParam.serviceDate = charterDataUtils.chooseDateBean.start_date + " "+ charterDataUtils.flightBean.arrivalTime + ":00";
                airportParam.startLocation = charterDataUtils.flightBean.arrLocation;
                airportParam.endLocation = charterDataUtils.pickUpPoiBean.location;
                BatchPrice batchPrice = new BatchPrice();
                batchPrice.param = airportParam;
                batchPrice.serviceType = SERVICE_TYPE_PICKUP;
                batchPrice.index = index;
                if (charterDataUtils.guidesDetailData != null) {
                    airportParam.carIds = charterDataUtils.guidesDetailData.getCarIds();
                    airportParam.premiumMark = charterDataUtils.guidesDetailData.isQuality;
                }
                batchPriceList.add(batchPrice);
                fitstOrderGoodsType = 1;

                if (charterDataUtils.guidesDetailData != null) {
                    RequestCheckGuide.CheckGuideBean checkGuideBean = new RequestCheckGuide.CheckGuideBean();
                    checkGuideBean.guideId = charterDataUtils.guidesDetailData.guideId;
                    checkGuideBean.cityId = charterDataUtils.getStartCityBean(i + 1).cityId;
                    checkGuideBean.orderType = 1;
                    checkGuideBean.startTime = airportParam.serviceDate;
                    checkGuideBean.endTime = charterDataUtils.chooseDateBean.start_date + " "+ CombinationOrderActivity.SERVER_TIME_END;
                    checkGuideBeanList.guideSubOrderInfos.add(checkGuideBean);
                }
                index++;
            } else if (cityRouteScope.routeType == CityRouteBean.RouteType.SEND) {//只送机
                AirportParam sendParam = new AirportParam();
                sendParam.airportCode = charterDataUtils.airPortBean.airportCode;
                sendParam.serviceDate = charterDataUtils.chooseDateBean.end_date + " " + charterDataUtils.sendServerTime + ":00";
                sendParam.startLocation = charterDataUtils.sendPoiBean.location;
                sendParam.endLocation = charterDataUtils.airPortBean.location;
                if (charterDataUtils.guidesDetailData != null) {
                    sendParam.carIds = charterDataUtils.guidesDetailData.getCarIds();
                    sendParam.premiumMark = charterDataUtils.guidesDetailData.isQuality;
                }
                BatchPrice batchPrice = new BatchPrice();
                batchPrice.param = sendParam;
                batchPrice.serviceType = SERVICE_TYPE_SEND;
                batchPrice.index = index;
                batchPriceList.add(batchPrice);

                if (charterDataUtils.guidesDetailData != null) {
                    RequestCheckGuide.CheckGuideBean checkGuideBean = new RequestCheckGuide.CheckGuideBean();
                    checkGuideBean.guideId = charterDataUtils.guidesDetailData.guideId;
                    checkGuideBean.cityId = charterDataUtils.getStartCityBean(i + 1).cityId;
                    checkGuideBean.orderType = 2;
                    checkGuideBean.startTime = sendParam.serviceDate;
                    checkGuideBean.endTime = charterDataUtils.chooseDateBean.end_date + " "+ CombinationOrderActivity.SERVER_TIME_END;
                    checkGuideBeanList.guideSubOrderInfos.add(checkGuideBean);
                }
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
                boolean isSend = false;
                if (i == 0 && charterDataUtils.isSelectedPickUp && charterDataUtils.flightBean != null) {
                    dayArrangement.airportCode = charterDataUtils.flightBean.arrAirportCode;
                    dayArrangement.airportLocation = charterDataUtils.flightBean.arrLocation;
                    dayArrangement.serviceDate = DateUtils.getDay(charterDataUtils.chooseDateBean.start_date, dayArrangementIndex) + " " + charterDataUtils.flightBean.arrivalTime + ":00";
                    dayArrangement.airportServiceType = 1;
                } else if (i == size - 1 && charterDataUtils.isSelectedSend && charterDataUtils.airPortBean != null) {
                    dayArrangement.airportCode = charterDataUtils.airPortBean.airportCode;
                    dayArrangement.airportLocation = charterDataUtils.airPortBean.location;
                    dayArrangement.airportServiceType = 2;
                    dayArrangement.endCityId = charterDataUtils.airPortBean.cityId;
                    isSend = true;
                }
                if (!isSend) {
                    if (cityRouteScope.routeType == CityRouteBean.RouteType.OUTTOWN) {
                        if (isOuttown == -1) {
                            isOuttown = 1;
                        }
                        dayArrangement.endCityId = charterDataUtils.getEndCityBean(i + 1).cityId;
                    } else {
                        dayArrangement.endCityId = startCityBean.cityId;
                    }
                }
                dailyPriceParam.arrangements.add(dayArrangement);
                 /*
                 * 拆单情况
                 * 1 如果第一天仅接机，那么第一天需要与第二天拆开
                 * 2 如果最后一天仅送机，最后一天需要与倒数第二天拆开
                 * 3 如果当前一天的开始城市不等于前一天的结束城市，那么当前一天需要与前一天拆开
                 * 4 如果当前一天或连续几天自己转转不包车，那么空白的行程前后都需要拆开
                 */
                if (i + 1 >= size
                        || travelList.get(i + 1).routeType == CityRouteBean.RouteType.AT_WILL
                        || travelList.get(i + 1).routeType == CityRouteBean.RouteType.SEND
                        || (cityRouteScope.routeType == CityRouteBean.RouteType.OUTTOWN && charterDataUtils.getEndCityBean(i + 1).cityId != charterDataUtils.getStartCityBean(i + 2).cityId)
                        || (cityRouteScope.routeType != CityRouteBean.RouteType.OUTTOWN && charterDataUtils.getStartCityBean(i + 1).cityId != charterDataUtils.getStartCityBean(i + 2).cityId)) {
                    CityBean _startCityBean = charterDataUtils.getStartCityBean(dayArrangementIndex + 1);
                    dailyPriceParam.startCityId = _startCityBean.cityId;
                    dailyPriceParam.startLocation = _startCityBean.location;
                    dailyPriceParam.startDate = DateUtils.getDay(charterDataUtils.chooseDateBean.start_date, dayArrangementIndex) + " " + CombinationOrderActivity.SERVER_TIME;

                    if (i == size - 1 && charterDataUtils.isSelectedSend && charterDataUtils.airPortBean != null) {//选了送机，结束城市为机场所在城市
                        dailyPriceParam.endCityId = charterDataUtils.airPortBean.cityId;
                        dailyPriceParam.endLocation = charterDataUtils.airPortBean.location;
                    } else if (cityRouteScope.routeType == CityRouteBean.RouteType.OUTTOWN) {
                        CityBean endCityBean = charterDataUtils.getEndCityBean(i + 1);
                        dailyPriceParam.endCityId = endCityBean.cityId;
                        dailyPriceParam.endLocation = endCityBean.location;
                    } else {
                        CityBean endCityBean = charterDataUtils.getStartCityBean(i + 1);
                        dailyPriceParam.endCityId = endCityBean.cityId;
                        dailyPriceParam.endLocation = endCityBean.location;
                    }

                    dailyPriceParam.endDate = DateUtils.getDay(charterDataUtils.chooseDateBean.start_date, i) + " " + CombinationOrderActivity.SERVER_TIME_END;
                    if (charterDataUtils.guidesDetailData != null) {
                        dailyPriceParam.carIds = charterDataUtils.guidesDetailData.getCarIds();
                        dailyPriceParam.premiumMark = charterDataUtils.guidesDetailData.isQuality;
                    }

                    BatchPrice batchPrice = new BatchPrice();
                    batchPrice.param = dailyPriceParam;
                    batchPrice.serviceType = SERVICE_TYPE_CHARTER;
                    batchPrice.index = index;
                    batchPriceList.add(batchPrice);

                    //第一个订单的订单类型，退改规则使用
                    if (batchPriceList != null && ((fitstOrderGoodsType == 1 && batchPriceList.size() == 2) || batchPriceList.size() == 1)) {
                        fitstOrderGoodsType = getOrderType(isOuttown == 1, dailyPriceParam.arrangements.size());
                    }

                    if (charterDataUtils.guidesDetailData != null) {
                        RequestCheckGuide.CheckGuideBean checkGuideBean = new RequestCheckGuide.CheckGuideBean();
                        checkGuideBean.guideId = charterDataUtils.guidesDetailData.guideId;
                        checkGuideBean.cityId = dailyPriceParam.startCityId;
                        checkGuideBean.orderType = getOrderType(isOuttown == 1, dailyPriceParam.arrangements.size());
                        checkGuideBean.startTime = dailyPriceParam.startDate;
                        checkGuideBean.endTime = dailyPriceParam.endDate;
                        checkGuideBeanList.guideSubOrderInfos.add(checkGuideBean);
                    }

                    index++;
                    dailyPriceParam = new DailyPriceParam();
                }
            }
        }
        BatchPriceListBean batchPriceListBean = new BatchPriceListBean();
        batchPriceListBean.batchPrice = batchPriceList;
        batchPriceListBean.adultNum = charterDataUtils.adultCount;
        batchPriceListBean.childNum = charterDataUtils.childCount;
        batchPriceListBean.childSeatNum = charterDataUtils.childSeatCount;

        batchPriceListBean.userId = UserEntity.getUser().getUserId(context);
        if (charterDataUtils.isSeckills()) {
            batchPriceListBean.timeLimitedSaleNo = charterDataUtils.seckillsBean.timeLimitedSaleNo;
            batchPriceListBean.timeLimitedSaleScheduleNo = charterDataUtils.seckillsBean.timeLimitedSaleScheduleNo;
        }
        if (batchPriceList != null && batchPriceList.size() > 1) {//标记是否是组合单
            charterDataUtils.isGroupOrder = true;
        } else {
            charterDataUtils.isGroupOrder = false;
        }

        charterDataUtils.fitstOrderGoodsType = fitstOrderGoodsType;

        if (charterDataUtils.guidesDetailData != null) {
            charterDataUtils.checkGuideBeanList = checkGuideBeanList;
        }
        return JsonUtils.toJson(batchPriceListBean);
    }


    public int getOrderType(boolean isOuttown, int size) {
        int result = 3;
        if (isOuttown) {
            if (size > 3) {//大长途
                result = 7;
            } else if (size <= 3) {//小长途
                result = 6;
            }
        } else {
            result = 3;
        }
        return result;
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
        public int adultNum;
        public int childNum;
        public int childSeatNum;
        public int channelId = Constants.REQUEST_CHANNEL_ID;
        public String userId;
        public String timeLimitedSaleNo;
        public String timeLimitedSaleScheduleNo;
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
        public int specialCarsIncluded = 1; // 是否包含特殊车型,默认不包含1.包含 0.不包含 写死为1
        public int premiumMark = 0;
    }

    public static class DailyPriceParam implements Serializable {
        public Long channelId = 18l;                 // 渠道ID
        public String carIds;                        // 否 指定报价车型 格式：车型ID,车型ID
        public int premiumMark = 0;
        public int specialCarsIncluded = 1;          // 是否包含特殊车型 0.不包含 1.包含 默认不包含 写死为1
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