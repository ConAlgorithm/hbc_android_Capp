package com.hugboga.custom.data.bean;

import java.io.Serializable;

/**
 * Created by qingcha on 17/6/1.
 */
public class ActivityBuyNowBean implements Serializable{
    /**
     * 101：无效活动编号
     * 102：无效活动场次编号
     * 201：用户已参与，发券
     * 202：用户未参与，有库存
     * 203：用户未参与，无库存，发券
     * */
    public int buyNowStatus;
    public String couponName;//券包名称
}
