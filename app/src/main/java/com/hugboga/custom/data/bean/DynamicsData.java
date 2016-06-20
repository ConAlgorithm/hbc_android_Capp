package com.hugboga.custom.data.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by qingcha on 16/6/19.
 */
public class DynamicsData implements Serializable {

    @SerializedName("clist")
    private ArrayList<DynamicsItem> dynamicsList;

    private ArrayList<DynamicsItem> dynamicsAllList;

    private long reqtime;

    private int position = 0;

    public ArrayList<DynamicsItem> getDynamicsAllList() {
        return dynamicsAllList;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void updateDynamicsAllList() {
        if (dynamicsList == null) {
            return;
        }
        if (dynamicsAllList == null) {
            this.dynamicsAllList = dynamicsList;
        } else {
            this.dynamicsAllList.addAll(dynamicsList);
        }
    }

    public ArrayList<DynamicsItem> getDynamicsList() {
        return dynamicsList;
    }

    public long getReqtime() {
        return reqtime;
    }

    public int getPosition() {
        return position;
    }

    public static class DynamicsItem implements Serializable {
        private String ctrackNo;
        private String trackInfo;

        public String getCtrackNo() {
            return ctrackNo;
        }

        public String getTrackInfo() {
            return trackInfo;
        }
    }
}
