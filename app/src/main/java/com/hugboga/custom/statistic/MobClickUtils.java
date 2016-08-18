package com.hugboga.custom.statistic;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

import java.util.Map;

/**
 * Created on 16/8/18.
 */

public class MobClickUtils {
    /**
     * 计算
     * 统计点击行为各属性被触发的次数
     * @param context
     * @param eventId
     * @param map
     */
    public static void OnEvent(Context context, String eventId, Map map){
        MobclickAgent.onEvent(context, eventId, map);
    }


    /**
     * 计数
     * 统计一个数值类型的连续变量
     * @param context
     * @param id
     * @param map
     * @param du
     */
    public static void OnEventValue(Context context, String id, Map map, int du){
        MobclickAgent.onEventValue(context, id, map, du);
    }

}
