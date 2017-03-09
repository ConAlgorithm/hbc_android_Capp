package com.hugboga.custom.data.bean.combination;

import java.io.Serializable;

/**
 * Created by qingcha on 17/3/6.
 */

public class GroupPickupParam implements Serializable {
    public Double priceChannel;                // 订单价格
    public Double priceFlightBrandSign;        // 接机牌价格
    public Integer serviceCityId;              // 服务城市Id
    public String serviceCityName;             // 服务城市名
    public String serviceTime;                 // 服务开始时间  必填 yyyy-MM-dd HH:mm:ss
    public String startAddress;                // 出发地地址 //GDS-M C-APP-O
    public String startAddressPoi;             // 出发地点POI //M  depLocation
    public String destAddress;                 // 目的地address //市内包车O 跨市M
    public String destAddressDetail;           // 目的地address详情 //市内包车O 跨市M
    public String destAddressPoi;              // 目的地POI //市内包车O 跨市M
    public String priceMark;
    public String flightDestCode;              // 降落机场三字码
    public String flightDestName;              // 降落机场名称
    public String flightNo;
    public String flightArriveTime;            // 降落时间 必填 yyyy-MM-dd HH:mm:ss
    public String flightAirportCode;           // 起飞机场三字码
    public String flightFlyTime;               // yyyy-MM-dd HH:mm:ss
    public String flightAirportName;
    public String flightAirportBuiding;
    public String flightDestBuilding;          // 降落机场航站楼
    public Double distance;                    // 服务距离 必填
    public Integer expectedCompTime;           // 预计服务完成时间 接送次 必填
}
