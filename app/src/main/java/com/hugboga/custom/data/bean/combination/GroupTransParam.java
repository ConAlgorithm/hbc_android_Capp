package com.hugboga.custom.data.bean.combination;

import com.hugboga.custom.data.bean.OrderBean;

import java.io.Serializable;

/**
 * Created by qingcha on 17/3/6.
 */

public class GroupTransParam implements Serializable{
    public Double priceChannel;                    // 订单价格
    public Integer serviceCityId;                  // 服务城市Id
    public String serviceCityName;                 // 服务城市名
    public String serviceTime;                     // 服务开始时间 必填 yyyy-MM-dd HH:mm:ss
    public String serviceEndTime;                  // 服务结束时间 必填 yyyy-MM-dd HH:mm:ss
    public String startAddress;                    // 出发地地址 //GDS-M C-APP-O
    public String startAddressPoi;                 // 出发地点POI //M
    public String startAddressDetail;              // 出发地详情(上车地点) //O-for-CAPP | M-for-GDS

    public String destAddress;                     // 目的地address //市内包车O 跨市M
    public String destAddressDetail;               // 目的地address详情 //市内包车O 跨市M
    public String destAddressPoi;                  // 目的地POI //市内包车O 跨市M

    public String priceMark;
    public String flightDestCode;                  // 降落机场三字码
    public String flightDestName;                  // 降落机场名称
    public String flightNo;
    public String flightArriveTime;
    public String flightAirportCode;               // 起飞机场三字码
    public String flightFlyTime;
    public String flightAirportName;
    public Integer flightIsCustom;                 // 0正常，1自定义，需要MIS注册航班
    public String flightAirportBuiding;
    public String flightDestBuilding;              // 降落机场航站楼

    public Integer expectedCompTime;               // 预计服务完成时间 接送次 必填
    public Double distance;                        // 服务距离
    public Integer isCheckin;
    public Double checkInPrice;

    public OrderBean.ChildSeats childSeatInfo;
}
