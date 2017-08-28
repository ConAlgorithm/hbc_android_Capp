package com.hugboga.custom.data.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by qingcha on 17/8/25.
 */

public class OrderPriceInfoBean implements Serializable{

    @SerializedName("containsFeeName")
    public String title;
    @SerializedName("containsFeeDetail")
    public List<String> labelList;

}
