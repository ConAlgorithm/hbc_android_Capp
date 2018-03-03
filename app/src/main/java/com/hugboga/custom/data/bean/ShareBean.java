package com.hugboga.custom.data.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by qingcha on 17/1/3.
 */
public class ShareBean {
    public int type;//0弹框，1好友，2朋友圈
    public String title;
    @SerializedName(value = "content", alternate = {"desc"})
    public String content;
    @SerializedName(value = "goUrl", alternate = {"link"})
    public String goUrl;
    @SerializedName(value = "picUrl", alternate = {"imgUrl"})
    public String picUrl;
}
