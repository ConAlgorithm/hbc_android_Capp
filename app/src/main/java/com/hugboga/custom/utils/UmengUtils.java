package com.hugboga.custom.utils;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created  on 16/4/11.
 */
public class UmengUtils {

    /**计算
     * @param context
     * @param key
     * @param map_value
     */
    public static void mobClickEvent(Context context, String key, Map<String,String> map_value){
        MobclickAgent.onEventValue(context,key,map_value,1);
    }

    /**计数
     * @param context
     * @param key
     * @param map_value
     */
    public static void mobClickEventValue(Context context,String key,Map<String,String> map_value){
        MobclickAgent.onEvent(context,key,map_value);
    }



}
