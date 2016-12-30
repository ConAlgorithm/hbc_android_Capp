package com.hugboga.custom.data.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by qingcha on 16/9/5.
 *
 * bargainStatus : 0 (是否砍价 0:否 1:是)
 * goodMsg : 发现更多东京精品线路 >
 * content : [" 已经为您通知当地司导","您可在订单详情中跟踪司导的接单情况"]
 * cityId : 123
 */
public class PaySucceedBean implements Serializable{

    private int bargainStatus;
    private String goodMsg;
    private int cityId;
    private List<String> content;

    private String highLightStr = "";

    public boolean getBargainStatus() {
        return bargainStatus == 1;
    }

    public String getGoodMsg() {
        return goodMsg;
    }

    public int getCityId() {
        return cityId;
    }

    public String getHighLightStr() {
        return highLightStr;
    }

    public String getSucceedPrompt() {
        String result = "";
        if (content == null) {
            return result;
        }
        final int size = content.size();
        for (int i = 0; i < size; i++) {
            if ((i == 1 || i == 2) && size >= 3) {
                result += content.get(i);
                if (i == 1) {
                    result += "，";
                } else if (i == 2) {
                    highLightStr = content.get(i);
                    if (i + 1 < size) {
                        result += "\n";
                    }
                }
            } else {
                result += content.get(i);
                if (i + 1 < size) {
                    result += "\n";
                }
            }
        }
        return result;
    }
}
