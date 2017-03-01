package com.hugboga.custom.utils;

import android.support.v4.util.ArrayMap;

import com.hugboga.amap.entity.HbcLantLng;
import com.hugboga.custom.activity.CharterSecondStepActivity;
import com.hugboga.custom.data.bean.AirPort;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.CityRouteBean;
import com.hugboga.custom.data.bean.DirectionBean;
import com.hugboga.custom.data.bean.FlightBean;
import com.hugboga.custom.data.bean.PoiBean;
import com.hugboga.im.entity.HbcLogicImBean;

import java.util.ArrayList;

/**
 * Created by qingcha on 17/2/25.
 */
public class CharterDataUtils {

    private static CharterDataUtils charterDataUtils;

    public int currentDay = 1;
    public CharterSecondStepActivity.Params params;             // 下单第一步带过来的信息，包含第一天出发城市、日期、出行人数

    public FlightBean flightBean;                               // 接机：航班信息
    public PoiBean pickUpPoiBean;                               // 接机：送达地
    public DirectionBean pickUpDirectionBean;                   // 接机：机场到送达地距离及地图坐标
    public boolean isSelectedPickUp = true;                     // 接机：是否选中接机

    public AirPort airPortBean;                                 // 送机：机场信息
    public PoiBean sendPoiBean;                                 // 送机：出发地点
    public String sendServerTime;                               // 送机：出发时间
    public DirectionBean sendDirectionBean;                     // 送机：出发地到机场距离及地图坐标
    public boolean isSelectedSend = true;                       // 送机：是否选中送机

    public ArrayList<CityRouteBean.CityRouteScope> travelList;             // 存储每天的数据，点击"确认"后更新
    public ArrayMap<Integer, CityBean> cityBeanMap;                        // 用来存储出发城市，以当前天数为KEY，实时更新
    public ArrayMap<Integer, ArrayList<CityRouteBean.Fence>> fencesMap;    // 用来存储出发城市，以当前天数为KEY，实时更新

    private CharterDataUtils() {
        travelList = new ArrayList<CityRouteBean.CityRouteScope>();
        cityBeanMap = new ArrayMap<>();
        fencesMap = new ArrayMap<Integer, ArrayList<CityRouteBean.Fence>>();
    }

    public static CharterDataUtils getInstance() {
        if (charterDataUtils == null) {
            charterDataUtils = new CharterDataUtils();
        }
        return charterDataUtils;
    }

    public void init(CharterSecondStepActivity.Params params) {
        this.params = params;
        cityBeanMap.put(1, charterDataUtils.params.startBean);
    }

    public boolean isFirstDay() {
        return currentDay == 1;
    }

    public boolean isLastDay() {
        if (params != null && params.chooseDateBean != null && currentDay == params.chooseDateBean.dayNums) {
            return true;
        }
        return false;
    }

    public void addCityRouteScope(CityRouteBean.CityRouteScope cityRouteScope) {
        final int position = charterDataUtils.currentDay - 1;
        if (position < travelList.size()) {
            travelList.set(position, cityRouteScope);
        } else {
            travelList.add(cityRouteScope);
        }
        charterDataUtils.currentDay++;
    }

    public ArrayList<CityRouteBean.Fence> getCurrentDayFences() {
        int position = 1;
        for (int i = currentDay; i >= 1; i--) {
            if (fencesMap.containsKey(i)) {
                position = i;
                break;
            }
        }
        return fencesMap.get(position);
    }

    public ArrayList<CityRouteBean.Fence> getNextDayFences() {
        if (fencesMap.containsKey(currentDay + 1)) {
            return fencesMap.get(currentDay + 1);
        } else {
            return null;
        }
    }

    public CityBean getCurrentDayCityBean() {
        int position = 1;
        for (int i = currentDay; i >= 1; i--) {
            if (cityBeanMap.containsKey(i)) {
                position = i;
                break;
            }
        }
        return cityBeanMap.get(position);
    }

    public CityBean getNextDayCityBean() {
        if (cityBeanMap.containsKey(currentDay + 1)) {
            return cityBeanMap.get(currentDay + 1);
        } else {
            return null;
        }
    }

    public void onDestroy() {
        flightBean = null;
        pickUpPoiBean = null;
        params = null;
        currentDay = 1;
    }

    public static ArrayList<HbcLantLng> getHbcLantLngList(CityRouteBean.Fence _fence) {
        if (_fence == null || _fence.fencePoints == null) {
            return null;
        }
        ArrayList<CityRouteBean.Fencepoint> fencePoints = _fence.fencePoints;
        final int fencePointsSize = fencePoints.size();
        ArrayList<HbcLantLng> resultList = new ArrayList<>(fencePointsSize);
        try {
            for (int i = 0; i < fencePointsSize; i++) {
                CityRouteBean.Fencepoint fencePoint = fencePoints.get(i);
                String[] points = fencePoint.startPoint.split(",");
                HbcLantLng hbcLantLng = new HbcLantLng();
                hbcLantLng.latitude = CommonUtils.getCountDouble(points[0]);
                hbcLantLng.longitude = CommonUtils.getCountDouble(points[1]);
                resultList.add(hbcLantLng);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }

    public static ArrayList<HbcLantLng> getHbcLantLngList(ArrayList<DirectionBean.Step> _steps) {
        if (_steps == null || _steps.size() <= 0) {
            return null;
        }
        ArrayList<DirectionBean.Step> steps = _steps;
        final int stepsSize = steps.size();
        ArrayList<HbcLantLng> resultList = new ArrayList<>(stepsSize);
        for (int i = 0; i < stepsSize; i++) {
            DirectionBean.Step step = steps.get(i);
            HbcLantLng hbcLantLng = new HbcLantLng();
            hbcLantLng.latitude = CommonUtils.getCountDouble(step.startCoordinate.lat);
            hbcLantLng.longitude = CommonUtils.getCountDouble(step.startCoordinate.lng);
            resultList.add(hbcLantLng);
        }
        return resultList;
    }

    public static HbcLantLng getHbcLantLng(String location) {
        try {
            String[] points = location.split(",");
            HbcLantLng hbcLantLng = new HbcLantLng();
            hbcLantLng.latitude = CommonUtils.getCountDouble(points[0]);
            hbcLantLng.longitude = CommonUtils.getCountDouble(points[1]);
            return hbcLantLng;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
