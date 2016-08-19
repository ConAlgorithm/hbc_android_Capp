package com.hugboga.custom.statistic.bean;

import com.hugboga.custom.data.bean.OrderBean;

import java.io.Serializable;

/**
 * Created by qingcha on 16/8/19.
 */
public class EventPayBean implements Serializable{
    public String carType;
    public String seatCategory;
    public String guestcount;//乘客人数
    public String isFlightSign;//接机举牌等待
    public String isCheckin;//协助办理登记
    public String guideCollectId;
    public String orderStatus;//订单状态
    public int orderType;

    public boolean forother;//为他人订车 是、否
    public String paystyle;//支付方式 支付宝、微信支付、无
    public String paysource;//支付来源 下单过程中、失败重新支付、未支付订单详情页

    public void transform(OrderBean orderBean) {
        if (orderBean == null) {
            return;
        }
    }
}
