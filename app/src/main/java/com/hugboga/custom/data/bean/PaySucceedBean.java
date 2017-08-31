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
    private String cityName;
    private String goodsNo;
    private List<String> content;


    public boolean getBargainStatus() {
        return bargainStatus == 1;
    }

    public String getGoodMsg() {
        return goodMsg;
    }

    public int getCityId() {
        return cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public List<String> getContent() {
        return content;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public String getStateStr() {
        if (content != null && content.size() > 0) {
            return content.get(0);
        } else {
            return "";
        }
    }

    public String getDescStr() {
        if (content != null && content.size() > 1) {
            return content.get(1);
        } else {
            return "";
        }
    }

}
