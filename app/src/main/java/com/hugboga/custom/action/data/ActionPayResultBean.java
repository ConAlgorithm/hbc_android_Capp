package com.hugboga.custom.action.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by zhangqiang on 17/9/4.
 */

public class ActionPayResultBean implements Serializable {
    /**
     * 订单编号
     * */
    @SerializedName("on")
    public String orderNo;

    /**
     * 支付结果类型
     * */
    @SerializedName("c")
    public String resultType;
}
