package com.hugboga.custom.statistic.event;

import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.utils.CommonUtils;

/**
 * Created by qingcha on 16/8/18.
 *
 * 评价页
 */
public class EventEvaluate extends EventBase{

    private String orderType;

    public EventEvaluate(String orderType) {
        this.orderType = orderType;
    }

    @Override
    public String getEventId() {
        //goodsNo 订单类型：1-接机、2-送机、3-包车游、4-次租(单次接送)、5-固定线路、6-推荐线路；
        String result = null;
        switch (CommonUtils.getCountInteger(orderType)) {
            case 1:
                result = StatisticConstant.CLICKMARK_J;
                break;
            case 2:
                result = StatisticConstant.CLICKMARK_S;
                break;
            case 3:
                result = StatisticConstant.CLICKMARK_R;
                break;
            case 4:
                result = StatisticConstant.CLICKMARK_C;
                break;
            case 5:
                result = StatisticConstant.CLICKMARK_RG;
                break;
            case 6:
                result = StatisticConstant.CLICKMARK_RT;
                break;
        }
        return result;
    }

}
