package com.hugboga.custom.statistic.event;

import android.text.TextUtils;

import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.bean.EventPayBean;

import java.util.HashMap;

/**
 * Created by qingcha on 16/8/19.
 */
public class EventPayShow extends EventBase{

    private EventPayBean eventPayBean;

    public EventPayShow(EventPayBean eventPayBean) {
        this.eventPayBean = eventPayBean;
    }

    @Override
    public String getEventId() {
        //goodsNo 订单类型：1-接机、2-送机、3-包车游、4-次租(单次接送)、5-固定线路、6-推荐线路；
        String result = null;
        if (eventPayBean == null) {
            return result;
        }
        switch (eventPayBean.orderType) {
            case 1:
                result = StatisticConstant.LAUNCH_PAYJ;
                break;
            case 2:
                result = StatisticConstant.LAUNCH_PAYS;
                break;
            case 3:
            case 888:
                result = StatisticConstant.LAUNCH_PAYR;
                break;
            case 4:
                result = StatisticConstant.LAUNCH_PAYC;
                break;
            case 5:
                result = StatisticConstant.LAUNCH_PAYRG;
                break;
            case 6:
                result = StatisticConstant.LAUNCH_PAYRT;
                break;
        }
        return result;
    }

    @Override
    public HashMap getParamsMap() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (eventPayBean == null) {
            return map;
        }
        EventUtil eventUtil = EventUtil.getInstance();
        map.put("source", eventUtil.source);//触发来源 首页、搜索、城市页、收藏司导列表、司导个人页
        map.put("carstyle", eventPayBean.carType);//车型  经济5座，舒适5座，经济7座……
        map.put("guestcount", eventPayBean.guestcount);//乘客人数 1，2，3，4......11
        map.put("forother", eventPayBean.forother);//为他人订车 是、否
        map.put("paystyle", eventPayBean.paystyle);//支付方式 支付宝、微信支付、无
        map.put("paysource", eventUtil.isRePay ? "失败重新支付" : eventPayBean.paysource);//支付来源 下单过程中、失败重新支付、未支付订单详情页

        switch (eventPayBean.orderType) {
            case 1:
                map.put("pickwait", EventUtil.booleanTransform(eventPayBean.isFlightSign));//接机举牌等待 是、否
                break;
            case 2:
                map.put("assist", EventUtil.booleanTransform(eventPayBean.isCheckin));//协助办理登记 是、否
                break;
            case 3:
            case 888:
                map.put("selectG", EventUtil.booleanTransform(eventPayBean.isSelectedGuide));//选择已收藏司导 是、否
                map.put("days", eventPayBean.days);
                break;
        }
        return map;
    }
}

