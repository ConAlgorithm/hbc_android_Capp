package com.hugboga.custom.data.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by qingcha on 16/6/19.
 */
public class DynamicsData implements Serializable {

    private ArrayList<DynamicsItem> clist;

    private long reqtime;

    public ArrayList<DynamicsItem> getClist() {
        return clist;
    }

    public long getReqtime() {
        return reqtime;
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
