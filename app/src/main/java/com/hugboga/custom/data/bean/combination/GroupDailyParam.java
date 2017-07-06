package com.hugboga.custom.data.bean.combination;

import com.hugboga.custom.data.bean.OrderBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qingcha on 17/3/6.
 */

public class GroupDailyParam implements Serializable{

    public Integer complexType;                            // 0 默认只 1 包含接机 2 包含送机 3 接机和送机 必填
    public Double priceChannel;                            // 订单价格
    public Integer serviceCityId;                          // 服务城市Id
    public String serviceCityName;                         // 服务城市名
    public Integer serviceEndCityid;                       // 服务结束城市ID
    public String serviceEndCityname;                      // 服务结束城市Id
    public String serviceTime;                             // 服务开始时间  必填 yyyy-MM-dd HH:mm:ss
    public String serviceEndTime;                          // 服务结束时间  必填 yyyy-MM-dd HH:mm:ss
    public String startAddress;                            // 出发地地址 //GDS-M C-APP-O
    public String startAddressPoi;                         // 出发地点POI //M
    public String startAddressDetail;                      // 出发地详情(上车地点) //O-for-CAPP | M-for-GDS

    public String destAddress;                             // 目的地address //市内包车O 跨市M
    public String destAddressDetail;                       // 目的地address详情 //市内包车O 跨市M
    public String destAddressPoi;                          // 目的地POI //市内包车O 跨市M
    public String priceMark;
    public String servicePassCitys;                        // 途经城市[1,3,4,5] 城市ID列表 //O
    public List<ServicePassDetail> servicePassDetailList;  // 行程信息
    public Integer totalDays;                              // 订单天数
    public String userRemark;                              // 客人备注
    public String userWechat;                              // 客人微信号
    public int halfDaily;                                  // 是否半日包车 0:不是半日包车 1:是半日包车
//    public Double priceHotel;                              // 酒店总费用
//    public Integer hotelRoom;                              // 房间数
//    public Integer hotelDays;                              // 几晚

    public Double priceTicket;
    public Double priceActual;
    public String limitedSaleNo;                           // 秒杀活动编号
    public String limitedSaleScheduleNo;                   // 秒杀活动场次编号

    public TravelRaiders travelRaidersInfo;
    public OrderBean.ChildSeats childSeatInfo;

    public GroupDailyParam() {
        this.servicePassDetailList = new ArrayList<ServicePassDetail>();
    }

    public void setTravelFlightParam(TravelFlightParam param, boolean isPickUp) {
        if (travelRaidersInfo == null) {
            travelRaidersInfo = new TravelRaiders();
        }
        if (isPickUp) {
            travelRaidersInfo.pickup = param;
        } else {
            travelRaidersInfo.transfer = param;
        }
    }

    public static class ServicePassDetail implements Serializable{
        public String goodsNo;         // 商品编号
        public String goodsVersion;    // 商品版本号
        public Integer startCityId;    // 开始城市ID
        public Integer cityId;         // 结束城市Id
        public String cityName;        // 结束城市名
        public Integer days;           // 天数
        public Integer cityType;       // 1 市内 2 周边 3 跨城市
        public String description;
        public String distance;
        public String airportCode;     // 包含接送机的时候机场三字节码
        public Integer complexType;    // 1 接机 2送机
        public String scenicDesc;      // 景点
        public String scopeDesc;       // 游玩范围
    }

    public static class TravelRaiders implements Serializable{
        public TravelFlightParam pickup;
        public TravelFlightParam transfer;
    }

    public static class TravelFlightParam {
        public String serviceTime;         // 服务开始时间
        public String flightDestCode;      // 降落机场三字码
        public String flightDestName;      // 降落机场名称
        public String flightDestPoi;
        public String flightNo;
        public String flightArriveTime;
        public String flightAirportCode;   // 起飞机场三字码
        public String flightAriportPoi;
        public String flightFlyTime;
        public String flightAirportName;
        public String flightAirportBuiding;
        public String flightDestBuilding;  // 降落机场航站楼
    }
}

