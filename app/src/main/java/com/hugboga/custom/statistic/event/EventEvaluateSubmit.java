package com.hugboga.custom.statistic.event;

import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.utils.CommonUtils;

import java.util.HashMap;

/**
 * Created by qingcha on 16/8/18.
 *
 * 提交评价
 */
public class EventEvaluateSubmit extends EventBase{

    private String orderType;
    private String score;
    private boolean content;
    private boolean picture;

    /**
     *  @param score    1.分数         1星、2星、3星、4星、5星
     *  @param content  2.是否有评论    是、否
     *  @param picture  3.是否有图片    是、否
     * */
    public EventEvaluateSubmit(String orderType, String score, boolean content, boolean picture) {
        this.orderType = orderType;
        this.score = score;
        this.content = content;
        this.picture = picture;
    }

    @Override
    public String getEventId() {
        //goodsNo 订单类型：1-接机、2-送机、3-包车游、4-次租(单次接送)、5-固定线路、6-推荐线路；
        String result = null;
        switch (CommonUtils.getCountInteger(orderType)) {
            case 1:
                result = StatisticConstant.MARK_J;
                break;
            case 2:
                result = StatisticConstant.MARK_S;
                break;
            case 3:
                result = StatisticConstant.MARK_R;
                break;
            case 4:
                result = StatisticConstant.MARK_C;
                break;
            case 5:
                result = StatisticConstant.MARK_RG;
                break;
            case 6:
                result = StatisticConstant.MARK_RT;
                break;
        }
        return result;
    }

    @Override
    public HashMap getParamsMap() {
        HashMap<String, Object> map = new HashMap<String, Object>(3);
        map.put("score", score + "星");
        map.put("content", booleanTransform(content));
        map.put("picture", booleanTransform(picture));
        return map;
    }
}
