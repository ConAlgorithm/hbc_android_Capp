package com.hugboga.custom.data.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by qingcha on 16/11/18.
 */
public class CancelReasonBean implements Serializable{

    @SerializedName("cancelReason")
    public ArrayList<CancelReasonItem> cancelReasonList;

    public static class CancelReasonItem implements Serializable {
        public String content;
        public int type; //0:为其它原因
        public transient boolean isSelected = false;

        public boolean isOtherReason() {
            return type == 0;
        }
    }
}
