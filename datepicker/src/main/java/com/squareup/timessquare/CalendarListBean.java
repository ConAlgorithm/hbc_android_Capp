package com.squareup.timessquare;


import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

public class CalendarListBean implements Serializable {

    public boolean hasOrder;    // 是否包含订单
    public boolean past;        // 跟订单时间比，是否是过去
    public boolean serviceAble; // 是否可服务
    public boolean daily;       // 当天有包车or线路订单
    public boolean pickup;      // 仅有接机 or 送机 or 单次订单
    public String day;

    public int orderType;       // 本地字段，当前的订单类型

    // 包车是否可定
    public boolean isCanDailyService() {
        return !past && serviceAble && !hasOrder;
    }

    // 全天是否可定
    public boolean isCanService() {
        return !past && serviceAble && !daily;
    }

    // 部分时间可定
    public boolean isCanHalfService() {
        return !past && serviceAble && pickup && !daily;
    }

    public void parse(JSONObject jsonObject) {
        hasOrder = jsonObject.optBoolean("hasOrder");
        past = jsonObject.optBoolean("past");
        serviceAble = jsonObject.optBoolean("serviceAble");
        daily = jsonObject.optBoolean("daily");
        pickup = jsonObject.optBoolean("pickup");
        day = jsonObject.optString("day");
    }

    public static HashMap<String, CalendarListBean> getMonthMap(String jsonStr, int orderType) {
        HashMap<String, CalendarListBean> map = new HashMap<>();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonStr);
            Iterator<String> keys = jsonObject.keys();
            for (; keys.hasNext(); ) {
                String day = keys.next();
                CalendarListBean calendarOrderEntity = new CalendarListBean();
                calendarOrderEntity.parse(jsonObject.optJSONObject(day));
                calendarOrderEntity.orderType = orderType;
                map.put(day, calendarOrderEntity);
            }
        } catch (Exception e) {
        }
        return map;
    }
}