package com.hugboga.custom.action.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by qingcha on 16/9/27.
 */
public class ActionSkuListBean implements Serializable {

    /**
     * 1-组合、2-国家、3-城市
     * */
    @SerializedName("t")
    public String type;

    /**
     * 组合ID或国家ID或城市ID
     * */
    @SerializedName("ai")
    public String areaId;

    @SerializedName("an")
    public String areaName;
}
