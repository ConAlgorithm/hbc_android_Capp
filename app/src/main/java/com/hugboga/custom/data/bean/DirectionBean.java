package com.hugboga.custom.data.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by qingcha on 17/2/28.
 * http://wiki.hbc.tech/pages/viewpage.action?pageId=7934121#id-行程查询-StepVo
 */
public class DirectionBean implements Serializable{
    public String distance;       // 距离
    public String distanceDesc;   // 距离描述 xxx公里
    public String duration;       // 时长
    public String durationDesc;   // 时间描述 xxx分钟"
    public ArrayList<Step> steps; // 途径点
    public int status;            // 起始坐标查不到距离返回0

    public static class Step implements Serializable{
        public String distance;
        public String duration;
        public CoordinateBean endCoordinate;
        public CoordinateBean startCoordinate;
    }
}
