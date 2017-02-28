package com.hugboga.custom.data.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by qingcha on 17/2/24.
 */
public class CityRouteBean {

    public int cityId;
    public String cityLocation;
    public String cityName;
    @SerializedName("cityRouteScopes")
    public List<CityRouteScope> cityRouteList; // 城市范围列表
    public int fenceSwitch; // 围栏开关

    public static class CityRouteScope implements Serializable {
        public int routeKms;            // 行程公里数
        public int routeLength;         // 行程时长
        public String routePlaces;      // 行程点
        public String routeScope;       // 范围
        public String routeTitle;       // 行程名称
        public int routeType;           // 行程类型: 101：市内包车、102：市内半日包、201：周边包车、301：跨城
    }

    public static class RouteType {
        public static final int URBAN = 101;       // 市内包车
        public static final int HALFDAY = 102;     // 市内半日包
        public static final int SUBURBAN = 201;    // 周边包车
        public static final int OUTTOWN = 301;     // 跨城

        //-----前端定义-----
        public static final int AT_WILL = 11;     // 随便转转
        public static final int PICKUP = 12;      // 只接机
        public static final int SEND = 13;        // 只送机
    }
}
