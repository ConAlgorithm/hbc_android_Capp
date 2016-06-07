package com.hugboga.custom.data.bean;

import java.io.Serializable;

/**
 * 获取可用优惠券
 */

public class MostFitAvailableBean implements Serializable{
    public String userId;
    public String useOrderPrice;
    public String offset;
    public String limit;
    public String priceChannel;// 渠道价格   [必填]
    public String serviceTime; // 服务时间   [必填]
    public String carTypeId; // 1-经济 2-舒适 3-豪华 4-奢华    [必填]
    public String carSeatNum; // 车座数    [必填]
    public String serviceCityId; // 服务城市ID    [必填]
    public String serviceCountryId; // 服务所在国家ID   [必填]
    public String totalDays; // 日租天数，[日租必填]
    public String distance; // 预估路程公里数 [必填]
    public String serviceLocalDays;// 日租市内天数 [日租必填]
    public String serviceNonlocalDays;// 日租市外天数 [日租必填]
    public String expectedCompTime; // 接送机预计完成时间[非日租必填]
    public String orderType; // TYPE_TRANSFER(1, "接机"), TYPE_SEND(2, "送机"), TYPE_DAILY(3, "按天包车"), TYPE_SINGLE(4, "单次用车"), TYPE_ROUTE(5, "精品线路");
}
