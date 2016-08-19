package com.hugboga.custom.statistic.event;

import android.text.TextUtils;

import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.statistic.StatisticConstant;
import java.util.HashMap;

/**
 * Created by qingcha on 16/8/19.
 */
public class EventCancelOrder extends EventBase{

    private OrderBean orderBean;

    public EventCancelOrder(OrderBean orderBean) {
        this.orderBean = orderBean;
    }

    @Override
    public String getEventId() {
        //goodsNo 订单类型：1-接机、2-送机、3-包车游、4-次租(单次接送)、5-固定线路、6-推荐线路；
        String result = null;
        if (orderBean == null) {
            return result;
        }
        switch (orderBean.orderType) {
            case 1:
                result = StatisticConstant.CANCELORDER_J;
                break;
            case 2:
                result = StatisticConstant.CANCELORDER_S;
                break;
            case 3:
                result = StatisticConstant.CANCELORDER_R;
                break;
            case 4:
                result = StatisticConstant.CANCELORDER_C;
                break;
            case 5:
                result = StatisticConstant.CANCELORDER_RG;
                break;
            case 6:
                result = StatisticConstant.CANCELORDER_RT;
                break;
        }
        return result;
    }

    @Override
    public HashMap getParamsMap() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (orderBean == null) {
            return map;
        }
        EventUtil eventUtil = EventUtil.getInstance();
        map.put("source", eventUtil.source);//触发来源 首页、搜索、城市页、收藏司导列表、司导个人页
        map.put("carstyle", orderBean.getCarType() + orderBean.seatCategory + "座");//车型  经济5座，舒适5座，经济7座……
        map.put("guestcount", orderBean.adult + orderBean.child);//乘客人数 1，2，3，4......11
//        map.put("forother", forother);//为他人订车 是、否
//        map.put("paystyle", paystyle);//支付方式 支付宝、微信支付、无
//        map.put("paysource", orderstatus);//支付来源 下单过程中、失败重新支付、未支付订单详情页
        map.put("orderstatus", orderBean.orderStatus.name);//订单状态 未支付、已支付、已接单……

        switch (orderBean.orderType) {
            case 1:
                map.put("pickwait", EventUtil.booleanTransform(orderBean.isFlightSign));//接机举牌等待 是、否
                break;
            case 2:
                map.put("assist", EventUtil.booleanTransform(orderBean.isCheckin));//协助办理登记 是、否
                break;
            case 3:
                if (!TextUtils.isEmpty(eventUtil.sourceDetail)) {
                    map.put("source_detail", eventUtil.sourceDetail);//触发来源-第一步 无、首页、搜索、城市页
                }
                map.put("selectG", EventUtil.booleanTransform(!TextUtils.isEmpty(orderBean.guideCollectId)));//选择已收藏司导 是、否
                break;
        }
        return map;
    }

}