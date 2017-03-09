package com.hugboga.custom.data.bean;

import java.io.Serializable;

/**
 * Created by qingcha on 16/12/9.
 */
public class CouponActivityBean implements Serializable {

    public CouponActiviyVo couponActiviyVo;

    public static class CouponActiviyVo implements Serializable {
        public boolean activityStatus;  // 活动状态（false，已结束；true，开启）
        public long scanTime;           // 浏览秒数（秒值）
        public long cycleTime;          // 活动周期（秒值）
        public String activityTitle;    // 活动标题
        public String couponNo;         // 券编码
    }
}
