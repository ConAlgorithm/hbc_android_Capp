package com.hugboga.custom.action.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by qingcha on 17/2/9.
 */

public class ActionImChatBean implements Serializable {

    @SerializedName("g")
    public String guideId;

    @SerializedName("t")
    public String tid;

    @SerializedName("tp")
    public String type;

}
