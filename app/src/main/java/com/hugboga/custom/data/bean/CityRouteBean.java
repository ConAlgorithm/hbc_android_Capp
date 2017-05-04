package com.hugboga.custom.data.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qingcha on 17/2/24.
 */
public class CityRouteBean {

    public int cityId;
    public String cityLocation;
    public String cityName;
    public String countryId;
    @SerializedName("cityRouteScopes")
    public List<CityRouteScope> cityRouteList;  // 城市范围列表
    public int fenceSwitch;                     // 围栏开关
    public ArrayList<Fence> fences;             // 围栏

    public static class CityRouteScope implements Serializable {
        public int routeKms;            // 行程公里数
        public int routeLength;         // 行程时长
        public String routePlaces;      // 行程点
        public String routeScope;       // 范围
        public String routeTitle;       // 行程名称
        public int routeType;           // 行程类型: 101：市内包车、102：市内半日包、201：周边包车、301：跨城

        //-----前端定义-----
        public int fenceSwitch;         // 围栏开关 0：关闭、1：开启

        public boolean isOpeanFence() {
            return fenceSwitch == 1;
        }

        public CityRouteScope(int routeType) {
            this.routeType = routeType;
        }
    }

    public static class RouteType {
        public static final int URBAN = 101;       // 市内包车
        public static final int HALFDAY = 102;     // 市内半日包
        public static final int SUBURBAN = 201;    // 周边包车
        public static final int OUTTOWN = 301;     // 跨城

        //-----前端定义-----
        public static final int AT_WILL = -11;     // 随便转转
        public static final int PICKUP = -12;      // 只接机
        public static final int SEND = -13;        // 只送机
    }

    public static class Fence implements Serializable {
        public String fenceId;                              // 围栏ID
        public String fenceVersion;                         // 围栏版本
        public int fenceType;                               // 围栏类型 1：市内、2：周边
        public ArrayList<Fencepoint> fencePoints;           // 围栏点 格式：纬度，经度
    }

    public static class Fencepoint implements Serializable {
        public String endPoint;
        public String startPoint;
    }
}
