package com.hugboga.custom.statistic;

import android.content.Context;

import com.hugboga.custom.MyApplication;
import com.hugboga.custom.statistic.event.EventBase;
import com.umeng.analytics.MobclickAgent;

import java.util.Map;

/**
 * Created on 16/8/18.
 */

public class MobClickUtils {

    public static void onEvent(EventBase eventBase) {
        if (eventBase == null) {
            return;
        }
        Map params = eventBase.getParamsMap();
        if (params == null) {
            MobclickAgent.onEvent(MyApplication.getAppContext(), eventBase.getEventId());
        } else {
            MobclickAgent.onEvent(MyApplication.getAppContext(), eventBase.getEventId(), params);
        }
    }

    public static void onEvent(String eventId) {
        MobclickAgent.onEvent(MyApplication.getAppContext(), eventId);
    }

    /**
     * 计数
     * 统计一个数值类型的连续变量
     * @param eventId
     * @param map
     */
    public static void onEvent(String eventId, Map map) {
        MobclickAgent.onEvent(MyApplication.getAppContext(), eventId, map);
    }

    /**
     * 计算
     * 统计点击行为各属性被触发的次数
     * @param id
     * @param map
     * @param du
     */
    public static void onEventValue(String id, Map map, int du) {
        MobclickAgent.onEventValue(MyApplication.getAppContext(), id, map, du);
    }

}
