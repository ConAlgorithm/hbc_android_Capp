package com.hugboga.custom.utils;

import android.support.v4.util.ArrayMap;

import com.hugboga.custom.activity.CharterSecondStepActivity;
import com.hugboga.custom.data.bean.AirPort;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.CityRouteBean;
import com.hugboga.custom.data.bean.FlightBean;
import com.hugboga.custom.data.bean.PoiBean;

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
    public boolean isSelectedPickUp;                            // 接机：是否选中接机

    public AirPort airPortBean;                                 // 送机：机场信息
    public PoiBean sendPoiBean;                                 // 送机：出发地点
    public String sendServerTime;                               // 送机：出发时间
    public boolean isSelectedSend;                              // 送机：是否选中送机

    public ArrayList<CityRouteBean.CityRouteScope> travelList;  // 存储每天的数据，点击"确认"后更新
    public ArrayMap<Integer, CityBean> cityBeanMap;             // 用来存储出发城市，以当前天数为KEY，实时更新


    private CharterDataUtils() {
        travelList = new ArrayList<CityRouteBean.CityRouteScope>();
        cityBeanMap = new ArrayMap<>();
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

}
