package com.hugboga.custom.data.bean;

import java.io.Serializable;

/**
 * Created by qingcha on 16/9/8.
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

    public boolean isCanChoose() {
        return canChoose == 1;
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
}
