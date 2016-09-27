package com.hugboga.custom.action.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by qingcha on 16/9/27.
 */

public class ActionOrderDetailBean implements Serializable{

    /**
     * 订单编号
     * */
    @SerializedName("on")
    public String orderNo;

    /**
     * 订单类型
     * */
    @SerializedName("ot")
    public String orderType;

}
