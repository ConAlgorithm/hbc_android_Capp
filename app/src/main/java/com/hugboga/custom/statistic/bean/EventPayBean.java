package com.hugboga.custom.statistic.bean;

import android.text.TextUtils;

import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.OrderPriceInfo;
import com.hugboga.custom.data.bean.OrderStatus;

import java.io.Serializable;

/**
 * Created by on 16/8/19.
 * 仅用于统计
 */
public class EventPayBean implements Serializable{
    public String carType;
    public int seatCategory;
    public String guestcount;//乘客人数
    public String isFlightSign;//接机举牌等待
    public String isCheckin;//协助办理登记
    public String guideCollectId;
    public OrderStatus orderStatus;//订单状态
    public int orderType;
    public String orderId;
    public boolean forother = false;//为他人订车 是、否
    public String paystyle;//支付方式 支付宝、微信支付、无
    public String paysource;//支付来源 下单过程中、失败重新支付、未支付订单详情页
    public double shouldPay;
    public double actualPay;
    public double couponPrice;
    public double travelFundPrice;
    public boolean isSelectedGuide;
    public int days;

    public void transform(OrderBean orderBean) {
        if (orderBean == null) {
            return;
        }
        this.carType = orderBean.carDesc;
        this.seatCategory = orderBean.seatCategory;
        this.guestcount = "" + orderBean.adult + orderBean.child;
        this.isFlightSign = orderBean.isFlightSign;
        this.isCheckin = orderBean.isCheckin;
        this.guideCollectId = orderBean.guideCollectId;
        this.orderStatus = orderBean.orderStatus;
        this.orderType = orderBean.orderType;
        this.paysource = "未支付订单详情页";
        this.isSelectedGuide = !TextUtils.isEmpty(orderBean.guideCollectId);
        this.orderId = orderBean.orderNo;
        OrderPriceInfo priceInfo = orderBean.orderPriceInfo;
        this.shouldPay = priceInfo.shouldPay;
        this.actualPay = priceInfo.actualPay;
        this.couponPrice = priceInfo.couponPrice;
        this.travelFundPrice = priceInfo.travelFundPrice;
    }
}
