package com.hugboga.custom.data.bean;

/**
 * Created by admin on 2015/10/16.
 */
public enum OrderStatus {


    /**
     * 1: 未付款
     */
    INITSTATE(1, "等待付款"),

    /**
     * 2: 已付款
     */
    PAYSUCCESS(2, "预订成功"),

    /**
     * 3: 已接单
     */
    AGREE(3, "导游接单"),

    /**
     * 4: 已到达
     */
    ARRIVED(4, "导游到达"),

    /**
     * 5: 服务中
     */
    SERVICING(5, "服务中"),

    /**
     * 6: 未评价
     */
    NOT_EVALUATED(6, "未评价"),

    /**
     * 7: 已完成
     */
    COMPLETE(7, "服务完成"),

    /**
     * 8: 已取消
     */
    CANCELLED(8, "已取消"),

    /**
     * 9: 已退款
     */
    REFUNDED(9, "已退款"),

    /**
     * 10: 客诉处理中
     */
    COMPLAINT(10, "客诉处理中");


    public int code;
    public String name;

    private OrderStatus(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static OrderStatus getStateByCode(int code) {
        for (OrderStatus state : OrderStatus.values()) {
            if (state.code == code) return state;
        }
        return null;
    }
}
