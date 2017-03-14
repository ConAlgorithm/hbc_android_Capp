package com.hugboga.custom.data.bean;

/**
 * Created by admin on 2015/10/16.
 */
public enum OrderStatus {


    /**
     * 1: 未付款
     */
    INITSTATE(1, "等待支付"),

    /**
     * 2: 已付款
     */
    PAYSUCCESS(2, "预订成功"),

    /**
     * 3: 已接单
     */
    AGREE(3, "司导接单"),

    /**
     * 4: 已到达
     */
    ARRIVED(4, "司导到达"),

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
    COMPLAINT(10, "客诉处理中"),;

//    /** 11: 等待支付 */
//    Z_INIT(11, "等待支付"),
//
//    /** 12: 预订成功 */
//    Z_PAYSUCCESS(12, "预订成功"),
//
//    /** 13: 司导接单 */
//    Z_AGREE(13, "司导接单"),
//
//    /** 14: 服务中 */
//    Z_SERVICING(14, "服务中"),
//
//    /** 15: 服务完成 */
//    Z_COMPLETE(15, "服务完成"),
//
//    /** 16: 已取消 */
//    Z_CANCEL(16, "已取消"),
//
//    /** 17: 已退款 */
//    Z_REFUND(17, "已退款");


    public int code;
    public String name;

    private OrderStatus(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static OrderStatus getStateByCode(int code) {
        switch (code) {
            case 11:
                return INITSTATE;
            case 12:
                return PAYSUCCESS;
            case 13:
                return AGREE;
            case 14:
                return SERVICING;
            case 15:
                return COMPLETE;
            case 16:
                return REFUNDED;
        }

        for (OrderStatus state : OrderStatus.values()) {
            if (state.code == code) return state;
        }
        return null;
    }
}
