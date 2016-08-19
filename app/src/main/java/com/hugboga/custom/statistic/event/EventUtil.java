package com.hugboga.custom.statistic.event;

import com.hugboga.custom.statistic.MobClickUtils;

import java.util.HashMap;

/**
 * Created by on 16/8/19.
 */
public class EventUtil {

    private volatile static EventUtil eventUtil;

    public String source;//该source记录工具下单的前一页
    public String sourceDetail;//记录包车详情前一页

    private EventUtil() {

    }

    public static EventUtil getInstance() {
        if (eventUtil == null) {
            synchronized(EventUtil.class) {
                if (eventUtil == null) {
                    eventUtil = new EventUtil();
                }
            }
        }
        return eventUtil;
    }

    public static String booleanTransform(int is) {
        return booleanTransform(is == 1);
    }

    public static String booleanTransform(boolean is) {
        return is ? "是" : "否";
    }

    public static String booleanTransform(String is) {
        return "1".equals(is) ? "是" : "否";
    }

    public static String getShareTypeText(String type) {
        return "1".equals(type) ? "微信好友": "朋友圈";
    }

    public static void onShareDefaultEvent(String eventId, String shareType) {
        HashMap<String, Object> map = new HashMap<String, Object>(1);
        map.put("sharetype", getShareTypeText(shareType));
        MobClickUtils.onEvent(eventId, map);
    }

    public static void onShareSkuEvent(String eventId, String shareType, String routecity) {
        HashMap<String, Object> map = new HashMap<String, Object>(2);
        map.put("sharetype", getShareTypeText(shareType));
        map.put("routecity", routecity);
        MobClickUtils.onEvent(eventId, map);
    }

    public static void onDefaultEvent(String eventId, String source) {
        HashMap<String, Object> map = new HashMap<String, Object>(1);
        map.put("source", getShareTypeText(source));
        MobClickUtils.onEvent(eventId, map);
    }
}
