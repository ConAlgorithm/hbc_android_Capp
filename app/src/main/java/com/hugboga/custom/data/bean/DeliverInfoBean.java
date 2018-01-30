package com.hugboga.custom.data.bean;

import java.io.Serializable;

/**
 * Created by qingcha on 16/9/8.
 * http://wiki.hbc.tech/pages/viewpage.action?pageId=6619604
 */
public class DeliverInfoBean implements Serializable{

    public String deliverMessage;
    public String deliverDetail;

    /**
     * 1:未发单；2:已发单；3-已通知司导；4-有司导表态；5-正在安排司导；6-已通知该司导；7-为您协调司导；8-已确定导游；9-发单中
     * */
    public int deliverStatus;

    /**
     * 时间戳 毫秒
     * */
    public int span;

    /**
     * 发单间隔
     * */
    public int deliverTimeSpan;

    /**
     * 倒计时总时间 毫秒
     * */
    public int stayTime;

    /**
     * 是否可挑选司导 0-不可挑选；1-可挑选
     * */
    private int canChoose;

    /**
     * 支付成功2001 其他状态都不显示发单详情
     **/
    private int orderStatus;

    /**
     * 是否二次确认状态，0否，1是
     **/
    private Integer twiceConfirm = 0;

    /**
     * 二次确认状态距离自动取消的时间距离，单位为毫秒
     **/
    public Long twiceCancelSpan;

    /**
     * 二次确认的总时间，单位为毫秒
     **/
    public Long twiceCancelTotalSpan;

    public boolean isTwiceConfirm() {
        return twiceConfirm != null ? twiceConfirm == 1 : false;
    }

    public boolean isCanChoose() {
        return canChoose == 1;
    }

    public boolean isOrderStatusChanged() {
        return orderStatus != 2001;
    }

    public static class DeliverStatus {
        public static final int UNBILLED = 1;           // 未发单
        public static final int BILLED = 2;             // 已发单
        public static final int INFORMED = 3;           // 已通知司导
        public static final int COMMITTED = 4;          // 有司导表态
        public static final int BEING_ARRANGED = 5;     // 正在安排司导
        public static final int INFORMED_GUIDE = 6;     // 已通知该司导
        public static final int COORDINATION = 7;       // 为您协调司导
        public static final int IDENTIFIED = 8;         // 已确定导游
        public static final int DELIVERING = 9;         // 发单中
    }

    /**
     * 本地字段:记录刷新次数
     * 进入倒计时状态，访问这个页面时，改为先每5秒请求一次并更新数据，连续请求6次后，恢复到1分钟请求一次
     **/
    public int refreshCount;
    public static final int MAX_REFRESH_COUNT = 6;
}
