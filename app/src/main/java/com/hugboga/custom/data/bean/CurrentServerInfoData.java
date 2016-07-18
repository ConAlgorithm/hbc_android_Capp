package com.hugboga.custom.data.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by qingcha on 16/7/1.
 */
public class CurrentServerInfoData implements Serializable{

    @SerializedName("im_avatar")
    public String avatar;

    @SerializedName("im_name")
    public String name;

    @SerializedName("im_user_id")
    public String userId;
}
