package com.hugboga.custom.statistic.click;

import com.hugboga.custom.statistic.MobClickUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 16/8/19.
 */

public class StatisticClickEvent {

    //触发来源
    public static final String CLICK_SOURCE = "source";

    public static void click(String eventId,String source){
        Map map = new HashMap();
        map.put(CLICK_SOURCE,source);
        MobClickUtils.onEvent(eventId,map);
    }







}
