package com.hugboga.custom.statistic.click;

import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CollectGuideBean;
import com.hugboga.custom.data.bean.ManLuggageBean;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.StatisticConstant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 16/8/19.
 */

public class StatisticClickEvent {

    public static void click(String eventId){
        MobClickUtils.onEvent(eventId);
    }

    //触发来源
    public static void click(String eventId,String source){
        Map map = new HashMap();
        map.put("source",source);
        MobClickUtils.onEvent(eventId,map);
    }

    //砍价分享触发来源
    public static void clickShare(String eventId,String source){
        Map map = new HashMap();
        map.put("sharetype",source);
        MobClickUtils.onEvent(eventId,map);
    }


    private static String getGuestCount(ManLuggageBean guestcount){
        if(null == guestcount){
            return "0";
        }
        return (guestcount.mans + guestcount.childs)+"";
    }

    //提交订单按钮点击
    public static void commitClick(String eventId,String source,String carstyle,int guestcount,boolean forother){
        Map map = new HashMap();
        map.put("source",source);
        map.put("carstyle",carstyle);
        map.put("guestcount",guestcount);
        map.put("forother",forother?"是":"否");
        MobClickUtils.onEvent(eventId,map);
    }

    //包车提交订单按钮点击
    public static void commitClick(String eventId, String source, String  source_detail,
                                  String selectG, String carstyle, int guestcount, boolean forother){
        Map map = new HashMap();
        map.put("source",source);
        map.put("source_detail",source_detail);
        map.put("selectG",null != selectG?"是":"否");
        map.put("carstyle",carstyle);
        map.put("guestcount",guestcount);
        map.put("forother",forother?"是":"否");
        MobClickUtils.onEvent(eventId,map);
    }

    //接机确认行程按钮点击
    public static void pickClick(String eventId, String source,String carstyle,boolean pickwait, int guestcount){
        Map map = new HashMap();
        map.put("source",source);
        map.put("carstyle",carstyle);
        map.put("pickwait",pickwait?"是":"否");
        map.put("guestcount",guestcount);
        MobClickUtils.onEvent(eventId,map);
    }

    //送机确认行程按钮点击
    public static void sendClick(String eventId, String source,String carstyle,boolean assist, int guestcount){
        Map map = new HashMap();
        map.put("source",source);
        map.put("carstyle",carstyle);
        map.put("assist",assist?"是":"否");
        map.put("guestcount",guestcount);
        MobClickUtils.onEvent(eventId,map);
    }

    //单次 sku确认行程按钮点击
    public static void singleSkuClick(String eventId, String source,String carstyle, int guestcount){
        Map map = new HashMap();
        map.put("source",source);
        map.put("carstyle",carstyle);
        map.put("guestcount",guestcount);
        MobClickUtils.onEvent(eventId,map);
    }

    public static void dailyClick(String eventId, String source, int day,
                                  boolean isCollectGuide, String guestcount){
        Map map = new HashMap();
        map.put("source",source);
        map.put("days",day);
        map.put("selectG",isCollectGuide ? "是":"否");
        map.put("guestcount",guestcount);
        MobClickUtils.onEvent(eventId,map);
    }

    //包车选车下一步按钮点击
    public static void selectCarClick(String eventId, String source, int day,
                                      boolean isCollectGuide, String carstyle, String guestcount){
        Map map = new HashMap();
        map.put("source",source);
        map.put("days",day);
        map.put("selectG",isCollectGuide ? "是":"否");
        map.put("carstyle",carstyle);
        map.put("guestcount",guestcount);
        MobClickUtils.onEvent(eventId,map);
    }



    public  static void showOrderNewPage(int type,String eventId,String source, String  source_detail,
                                         String carstyle,boolean pickwait, String guestcount,
                                         boolean selectG){
        Map map = new HashMap();
        map.put("source",source);
        map.put("carstyle",carstyle);
        map.put("guestcount",guestcount);

        switch(type){
            case 1:
                map.put("pickwait",pickwait?"是":"否");
                break;
            case 2:
                map.put("assist",pickwait?"是":"否");
                break;
            case 3:
                map.put("source_detail",source_detail);
                map.put("selectG",selectG?"是":"否");
                break;
        }

        MobClickUtils.onEvent(eventId,map);
    }

    //城市页商品筛选值
    public static void showGscreenClick(String eventId,String screentype,String Gstyle,String Gdays,String Gtheme){
        Map map=new HashMap();
        map.put("screentype",screentype);
        map.put("Gstyle",Gstyle);
        map.put("Gdays",Gdays);
        map.put("Gtheme",Gtheme);

        MobClickUtils.onEvent(eventId,map);
    }

    //司导头像列表
    public static void showGuidesClick(String evevtId,String source,int orderType){
        if (orderType==0){
            return;
        }
        Map<String,String>map=new HashMap<String, String>();
        switch(orderType){
            case 3:
                map.put("ordertype","自定义包车游");
                break;
            case 5:
                map.put("ordertype","固定线路");
                break;
            case 6:
                map.put("ordertype","推荐线路");
                break;
        }
        map.put("source",source);
        MobClickUtils.onEvent(evevtId,map);
    }
}
