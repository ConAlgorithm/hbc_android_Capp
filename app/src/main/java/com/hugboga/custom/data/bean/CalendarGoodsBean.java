package com.hugboga.custom.data.bean;

import java.io.Serializable;

/**
 * Created by qingcha on 17/8/4.
 */
public class CalendarGoodsBean implements Serializable {

    public String serviceDate; // 服务日期 格式 yyyy-MM-dd
    public int stockStatus;    // 库存状态  101：有库存，可服务  201： 无库存 301:  超过提前预定期

    public boolean isCanService() {
        return stockStatus == 101;
    }
}
