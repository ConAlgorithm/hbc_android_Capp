package com.hugboga.custom.statistic.event;

import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.utils.CommonUtils;

import java.util.HashMap;

/**
 * Created by qingcha on 16/8/18.
 */
public class EventEvaluateShareFloat extends EventBase{

    private String orderType;
    private String source;

    public EventEvaluateShareFloat(String orderType, String source) {
        this.orderType = orderType;
        this.source = source;
    }

    @Override
    public String getEventId() {
        //goodsNo 订单类型：1-接机、2-送机、3-包车游、4-次租(单次接送)、5-固定线路、6-推荐线路；
        String result = null;
        switch (CommonUtils.getCountInteger(orderType)) {
            case 1:
                result = StatisticConstant.SHAREMARK_J;
                break;
            case 2:
                result = StatisticConstant.SHAREMARK_S;
                break;
            case 3:
                result = StatisticConstant.SHAREMARK_R;
                break;
            case 4:
                result = StatisticConstant.SHAREMARK_C;
                break;
            case 5:
                result = StatisticConstant.SHAREMARK_RG;
                break;
            case 6:
                result = StatisticConstant.SHAREMARK_RT;
                break;
        }
        return result;
    }

    @Override
    public HashMap getParamsMap() {
        HashMap<String, Object> map = new HashMap<String, Object>(1);
        map.put("source", source);
        return map;
    }

}
