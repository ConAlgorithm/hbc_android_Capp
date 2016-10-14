package com.hugboga.custom.action.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by qingcha on 16/9/27.
 */
public class ActionSkuDetailBean implements Serializable {
    /**
     * 城市ID
     * */
    @SerializedName("ci")
    public String cityId;

    /**
     * 商品编号
     * */
    @SerializedName("gn")
    public String goodsNo;

    /**
     * 商品H5地址
     * */
    @SerializedName("u")
    public String url;
}
