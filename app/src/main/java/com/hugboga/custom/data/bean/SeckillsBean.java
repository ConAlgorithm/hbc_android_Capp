package com.hugboga.custom.data.bean;

import java.io.Serializable;

/**
 * Created by qingcha on 17/6/30.
 */
public class SeckillsBean implements Serializable{
    public String timeLimitedSaleNo;         // 秒杀活动编号
    public String timeLimitedSaleScheduleNo; // 秒杀活动场次编号
    public boolean isSeckills = false;       // 是否进入秒杀通道

    public SeckillsBean(String timeLimitedSaleNo, String timeLimitedSaleScheduleNo) {
        this.timeLimitedSaleNo = timeLimitedSaleNo;
        this.timeLimitedSaleScheduleNo = timeLimitedSaleScheduleNo;
        isSeckills = true;
    }
}
