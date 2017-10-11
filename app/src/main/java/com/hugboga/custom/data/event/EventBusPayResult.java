package com.hugboga.custom.data.event;

/**
 * Created by qingcha on 17/10/10.
 */

public class EventBusPayResult {

    public boolean payResult;
    public String orderNo;

    public EventBusPayResult(boolean payResult, String orderNo) {
        this.payResult = payResult;
        this.orderNo = orderNo;
    }
}
