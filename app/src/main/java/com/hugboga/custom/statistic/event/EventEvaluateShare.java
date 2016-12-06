package com.hugboga.custom.statistic.event;

import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.utils.CommonUtils;

import java.util.HashMap;

/**
 * Created by qingcha on 16/8/18.
 */
public class EventEvaluateShare extends EventBase{

    private int orderType;
    private String source;
    private String shareType;


    /**
     *  @param source     触发来源
     *  @param shareType  分享方式    微信好友、朋友圈
     * */
    public EventEvaluateShare(int orderType, String source, String shareType) {
        this.orderType = orderType;
        this.source = source;
        this.shareType = shareType;
    }

    @Override
    public String getEventId() {
        //goodsNo 订单类型：1-接机、2-送机、3-包车游、4-次租(单次接送)、5-固定线路、6-推荐线路；
        String result = null;
        switch (orderType) {
            case 1:
                result = StatisticConstant.SHAREM_J;
                break;
            case 2:
                result = StatisticConstant.SHAREM_S;
                break;
            case 3:
                result = StatisticConstant.SHAREM_R;
                break;
            case 4:
                result = StatisticConstant.SHAREM_C;
                break;
            case 5:
                result = StatisticConstant.SHAREM_RG;
                break;
            case 6:
                result = StatisticConstant.SHAREM_RT;
                break;
        }
        return result;
    }

    @Override
    public HashMap getParamsMap() {
        HashMap<String, Object> map = new HashMap<String, Object>(2);
        map.put("source", source);
        map.put("sharetype", EventUtil.getShareTypeText(shareType));
        return map;
    }

}
