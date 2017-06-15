package com.hugboga.custom.action.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by qingcha on 17/6/14.
 */

public class ActionChoosePayBean implements Serializable {

    @SerializedName("o")
    public String orderId;//订单id

    @SerializedName("p")
    public String payPrice;//支付金额

    @SerializedName("at")
    public String apiType;//0：正常  1：买券

    @SerializedName("iw")
    public String isWechat;//是否显示微信支付 1：显示，0：不显示

    @SerializedName("ia")
    public String isAliPay;//是否显示支付宝 1：显示，0：不显示

    @SerializedName("iu")
    public String isUnionpay;//是否显示信用卡 1：显示，0：不显示

    @SerializedName("m")
    public String moble;

    @SerializedName("ac")
    public String areaCode;

}
