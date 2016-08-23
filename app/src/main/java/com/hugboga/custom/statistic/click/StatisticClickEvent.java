package com.hugboga.custom.statistic.click;

import com.hugboga.custom.data.bean.CollectGuideBean;
import com.hugboga.custom.data.bean.ManLuggageBean;
import com.hugboga.custom.statistic.MobClickUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 16/8/19.
 */

public class StatisticClickEvent {

    //触发来源
    public static void click(String eventId,String source){
        Map map = new HashMap();
        map.put("source",source);
        MobClickUtils.onEvent(eventId,map);
    }


    private static String getGuestCount(ManLuggageBean guestcount){
        return (guestcount.mans + guestcount.childs)+"";
    }

    //提交订单按钮点击
    public static void commitClick(String eventId,String source,String carstyle,ManLuggageBean guestcount,boolean forother){
        Map map = new HashMap();
        map.put("source",source);
        map.put("carstyle",carstyle);
        map.put("guestcount",getGuestCount(guestcount));
        map.put("forother",forother?"是":"否");
        MobClickUtils.onEvent(eventId,map);
    }

    //包车提交订单按钮点击
    public static void commitClick(String eventId, String source, String  source_detail,
                                   CollectGuideBean selectG, String carstyle, ManLuggageBean guestcount, boolean forother){
        Map map = new HashMap();
        map.put("source",source);
        map.put("source_detail",source_detail);
        map.put("selectG",null != selectG?"是":"否");
        map.put("carstyle",carstyle);
        map.put("guestcount",getGuestCount(guestcount));
        map.put("forother",forother?"是":"否");
        MobClickUtils.onEvent(eventId,map);
    }

    //接机确认行程按钮点击
    public static void pickClick(String eventId, String source,String carstyle,boolean pickwait, ManLuggageBean guestcount){
        Map map = new HashMap();
        map.put("source",source);
        map.put("carstyle",carstyle);
        map.put("pickwait",pickwait?"是":"否");
        map.put("guestcount",getGuestCount(guestcount));
        MobClickUtils.onEvent(eventId,map);
    }

    //送机确认行程按钮点击
    public static void sendClick(String eventId, String source,String carstyle,boolean assist, ManLuggageBean guestcount){
        Map map = new HashMap();
        map.put("source",source);
        map.put("carstyle",carstyle);
        map.put("assist",assist?"是":"否");
        map.put("guestcount",getGuestCount(guestcount));
        MobClickUtils.onEvent(eventId,map);
    }

    //单次 sku确认行程按钮点击
    public static void singleSkuClick(String eventId, String source,String carstyle, ManLuggageBean guestcount){
        Map map = new HashMap();
        map.put("source",source);
        map.put("carstyle",carstyle);
        map.put("guestcount",getGuestCount(guestcount));
        MobClickUtils.onEvent(eventId,map);
    }

    //包车选行程下一步按钮点击
    public static void dailyClick(String eventId, String source, String  source_detail,
                                  CollectGuideBean selectG, String guestcount){
        Map map = new HashMap();
        map.put("source",source);
        map.put("source_detail",source_detail);
        map.put("selectG",null != selectG?"是":"否");
        map.put("guestcount",guestcount);
        MobClickUtils.onEvent(eventId,map);
    }

    //包车选车下一步按钮点击
    public static void selectCarClick(String eventId, String source, String  source_detail,
                                   String carstyle, String guestcount){
        Map map = new HashMap();
        map.put("source",source);
        map.put("source_detail",source_detail);
        map.put("selectG","否");
        map.put("carstyle",carstyle);
        map.put("guestcount",guestcount);
        MobClickUtils.onEvent(eventId,map);
    }





}