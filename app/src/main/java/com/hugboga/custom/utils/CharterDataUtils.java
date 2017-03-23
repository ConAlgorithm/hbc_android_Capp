package com.hugboga.custom.utils;

import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.hugboga.amap.entity.HbcLantLng;
import com.hugboga.amap.view.HbcMapViewTools;
import com.hugboga.custom.activity.CharterSecondStepActivity;
import com.hugboga.custom.activity.CombinationOrderActivity;
import com.hugboga.custom.data.bean.AirPort;
import com.hugboga.custom.data.bean.CharterlItemBean;
import com.hugboga.custom.data.bean.ChooseDateBean;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.CityRouteBean;
import com.hugboga.custom.data.bean.DirectionBean;
import com.hugboga.custom.data.bean.FlightBean;
import com.hugboga.custom.data.bean.GuideCropBean;
import com.hugboga.custom.data.bean.GuidesDetailData;
import com.hugboga.custom.data.bean.PoiBean;
import java.util.ArrayList;

/**
 * Created by qingcha on 17/2/25.
 */
public class CharterDataUtils {

    private static CharterDataUtils charterDataUtils;

    public int currentDay = 1;
    public ChooseDateBean chooseDateBean;
    public int adultCount;
    public int childCount;
    public int maxPassengers;

    public GuidesDetailData guidesDetailData;                   // 指定司导的信息
    public ArrayList<GuideCropBean> guideCropList;              // 司导可服务城市

    public FlightBean flightBean;                               // 接机：航班信息
    public PoiBean pickUpPoiBean;                               // 接机：送达地
    public DirectionBean pickUpDirectionBean;                   // 接机：机场到送达地距离及地图坐标
    public boolean isSelectedPickUp = false;                    // 接机：是否选中接机

    public AirPort airPortBean;                                 // 送机：机场信息
    public PoiBean sendPoiBean;                                 // 送机：出发地点
    public String sendServerTime;                               // 送机：出发时间
    public DirectionBean sendDirectionBean;                     // 送机：出发地到机场距离及地图坐标
    public boolean isSelectedSend = false;                      // 送机：是否选中送机

    public ArrayList<CityRouteBean.CityRouteScope> travelList;  // 存储每天的数据
    public ArrayMap<Integer, CharterlItemBean> itemInfoList;

    public boolean isShowEmpty = false;
    public boolean isGroupOrder = false;                        // 是不是组合单，（查报价时赋值，查报价后才能使用）

    /**
     * 第一段行程的类型，退改规则使用（查报价时赋值，查报价后才能使用）
     *
     * [整日包车]+[全行程为出发城市市内/周边]  市内包车  3
     * [整日包车]+[行程<=3天]+[住宿/结束城市含其他城市]  小长途  6
     * [整日包车]+[行程>3天]+[住宿/结束城市含其他城市]  大长途   7
     * */
    public int fitstOrderGoodsType;

    private CharterDataUtils() {
        travelList = new ArrayList<CityRouteBean.CityRouteScope>();
        itemInfoList = new ArrayMap<Integer, CharterlItemBean>();
    }

    public static CharterDataUtils getInstance() {
        if (charterDataUtils == null) {
            charterDataUtils = new CharterDataUtils();
        }
        return charterDataUtils;
    }

    public void init(CharterSecondStepActivity.Params params) {
        this.chooseDateBean = params.chooseDateBean;
        this.adultCount = params.adultCount;
        this.childCount = params.childCount;
        this.maxPassengers = params.maxPassengers;
        addStartCityBean(1, params.startBean);
    }

    public boolean isFirstDay() {
        return currentDay == 1;
    }

    public boolean isLastDay() {
        if (chooseDateBean != null && currentDay == chooseDateBean.dayNums) {
            return true;
        }
        return false;
    }

    public int getRouteType(int position) {
        ArrayList<CityRouteBean.CityRouteScope> travelList = charterDataUtils.travelList;
        if (position < travelList.size()) {
            int routeType = travelList.get(position).routeType;
            if (routeType == CityRouteBean.RouteType.PICKUP && !charterDataUtils.isSelectedPickUp) {
                return CityRouteBean.RouteType.URBAN;
            } else if (routeType == CityRouteBean.RouteType.SEND && !charterDataUtils.isSelectedSend) {
                return CityRouteBean.RouteType.URBAN;
            } else {
                return travelList.get(position).routeType;
            }
        } else {
            return CityRouteBean.RouteType.URBAN;
        }
    }

    public void addStartCityBean(int day, CityBean cityBean) {
        if (itemInfoList.containsKey(day)) {
            CharterlItemBean itemBean = itemInfoList.get(day);
            itemBean.startCityBean = cityBean;
        } else {
            CharterlItemBean itemBean = new CharterlItemBean();
            itemBean.startCityBean = cityBean;
            itemInfoList.put(day, itemBean);
        }
    }

    public CityBean getStartCityBean(int day) {
        if (itemInfoList.containsKey(day)) {
            CharterlItemBean itemBean = itemInfoList.get(day);
            return itemBean.startCityBean;
        } else {
            return null;
        }
    }

    public CityBean getCurrentDayStartCityBean() {
        return getStartCityBean(currentDay);
    }

    public void addEndCityBean(int day, CityBean cityBean) {
        if (itemInfoList.containsKey(day)) {
            CharterlItemBean itemBean = itemInfoList.get(day);
            itemBean.endCityBean = cityBean;
        } else {
            CharterlItemBean itemBean = new CharterlItemBean();
            itemBean.endCityBean = cityBean;
            itemInfoList.put(day, itemBean);
        }
    }

    public CityBean getEndCityBean(int day) {
        if (itemInfoList.containsKey(day)) {
            CharterlItemBean itemBean = itemInfoList.get(day);
            return itemBean.endCityBean;
        } else {
            return null;
        }
    }

    public CityBean getEndCityBean() {
        return getEndCityBean(currentDay);
    }

    public CityBean setDefaultCityBean(int day) {
        if (day <= 1) {
            return null;
        }
        CityBean nextCityBean = getStartCityBean(day);
        if (nextCityBean == null) {
            int index = day - 2;
            CityRouteBean.CityRouteScope cityRouteScope = travelList.get(index);
            while (cityRouteScope.routeType == CityRouteBean.RouteType.AT_WILL && index - 1 >= 0 && index - 1 < travelList.size()) {
                index--;
                cityRouteScope = travelList.get(index);
            }
            CityBean startCityBean = null;
            if (cityRouteScope.routeType == CityRouteBean.RouteType.OUTTOWN) {
                startCityBean = getEndCityBean(index + 1);
            } else {
                startCityBean = getStartCityBean(index + 1);
            }
            addStartCityBean(day, startCityBean);
            return startCityBean;
        } else {
            return nextCityBean;
        }
    }

    public void addCityRouteScope(CityRouteBean.CityRouteScope cityRouteScope) {
        final int position = charterDataUtils.currentDay - 1;
        if (position < travelList.size()) {
            travelList.set(position, cityRouteScope);
        } else {
            travelList.add(cityRouteScope);
        }
    }

    public void addFences(int day, ArrayList<CityRouteBean.Fence> fences, boolean isStart) {
        if (itemInfoList.containsKey(day)) {
            CharterlItemBean itemBean = itemInfoList.get(day);
            if (isStart) {
                itemBean.startFence = fences;
            } else {
                itemBean.endFence = fences;
            }
        } else {
            CharterlItemBean itemBean = new CharterlItemBean();
            if (isStart) {
                itemBean.startFence = fences;
            } else {
                itemBean.endFence = fences;
            }
            itemInfoList.put(currentDay, itemBean);
        }
    }

    public ArrayList<CityRouteBean.Fence> getFences(int day, boolean isStart) {
        if (itemInfoList.containsKey(day)) {
            CharterlItemBean itemBean = itemInfoList.get(day);
            if (isStart) {
                return itemBean.startFence;
            } else {
                return itemBean.endFence;
            }
        } else {
            return null;
        }
    }

    public ArrayList<CityRouteBean.Fence> setDefaultFences() {
        if (currentDay <= 1) {
            return null;
        }
        ArrayList<CityRouteBean.Fence> fenceList = getFences(currentDay, true);
        if (fenceList == null) {
            CityRouteBean.CityRouteScope cityRouteScope = travelList.get(currentDay - 2);
            ArrayList<CityRouteBean.Fence> startFences = getFences(currentDay - 1, cityRouteScope.routeType != CityRouteBean.RouteType.OUTTOWN);
            addFences(currentDay, startFences, true);
            return startFences;
        } else {
            return fenceList;
        }
    }

    public ArrayList<CityRouteBean.Fence> getCurrentDayFences() {
        if (itemInfoList.containsKey(currentDay)) {
            return itemInfoList.get(currentDay).startFence;
        } else {
            return null;
        }
    }

    public ArrayList<CityRouteBean.Fence> getNextDayFences() {
        if (itemInfoList.containsKey(currentDay)) {
            return itemInfoList.get(currentDay).endFence;
        } else {
            return null;
        }
    }

    public boolean checkInfo(int routeType, int currentDay, boolean isShowToast) {

        // 判断接机"送达地"是否填写
        boolean checkPickup = routeType == CityRouteBean.RouteType.PICKUP
                && isFirstDay()
                && isSelectedPickUp
                && pickUpPoiBean == null;
        if (checkPickup) {
            if (isShowToast) {
                CommonUtils.showToast("请添加接机的送达地");
            }
            return false;
        }

        // 是否是送机
        boolean isSend = routeType == CityRouteBean.RouteType.SEND
                && airPortBean != null
                && isSelectedSend;

        // 判断送机"时间"是否填写
        boolean checkSendTime = isSend && TextUtils.isEmpty(charterDataUtils.sendServerTime);
        if (checkSendTime) {
            if (isShowToast) {
                CommonUtils.showToast("请添加送机的出发时间");
            }
            return false;
        }

        // 判断送机"出发地点"是否填写
        boolean checkSendAddress = isSend && charterDataUtils.sendPoiBean == null;
        if (checkSendAddress) {
            if (isShowToast) {
                CommonUtils.showToast("请添加送机的出发地点");
            }
            return false;
        }

        // 判断跨城市"结束城市"是否填写
        boolean checkOuttown = routeType == CityRouteBean.RouteType.OUTTOWN
                && charterDataUtils.getEndCityBean(currentDay) == null;
        if (checkOuttown) {
            if (isShowToast) {
                CommonUtils.showToast("请添加送达城市");
            }
            return false;
        }
        return true;
    }

    public String getStartServiceTime() {
        String result = "";
        if (isSelectedPickUp && flightBean != null) {
            result = chooseDateBean.start_date + " " + flightBean.arrivalTime + ":00";
        } else {
            result = chooseDateBean.start_date + " " + CombinationOrderActivity.SERVER_TIME;
        }
        return result;
    }

    public String getEndServiceTime() {
        return chooseDateBean.end_date + " " + CombinationOrderActivity.SERVER_TIME_END;
    }

    public String getPassCitiesId() {
        String citiesId = "";
        for (int i = 0; i < chooseDateBean.dayNums; i++) {
            if (itemInfoList.get(i) == null) {
                continue;
            }
            CityBean cityBean = itemInfoList.get(i).startCityBean;
            if (cityBean != null && cityBean.cityId != 0) {
                citiesId += cityBean.cityId;
                if (i < chooseDateBean.dayNums -1) {
                    citiesId += ",";
                }
            } else {
              continue;
            }
        }
        return citiesId;
    }

    public void cleanSendInfo() {
        if (isLastDay() && isSelectedSend && airPortBean != null) {
            resetSendInfo();
        }
    }

    public void resetSendInfo() {
        charterDataUtils.airPortBean = null;
        charterDataUtils.sendPoiBean = null;
        charterDataUtils.sendServerTime = null;
        charterDataUtils.sendDirectionBean = null;
        isSelectedSend = false;
    }

    public void cleanStartDate() {
        travelList.clear();
        resetSendInfo();
        itemInfoList.clear();
    }

    public void cleanGuidesDate() {
        guidesDetailData = null;
        guideCropList = null;
    }

    public void cleanDayDate(int day) {
        if (day > 1 && day - 1 < travelList.size()) {
            travelList.remove(day - 1);
            itemInfoList.remove(day);
        }
        if (day == chooseDateBean.dayNums) {
            resetSendInfo();
        }
    }

    public void onDestroy() {
        currentDay = 1;
        chooseDateBean = null;

        flightBean = null;
        pickUpPoiBean = null;
        pickUpDirectionBean = null;
        isSelectedPickUp = false;

        airPortBean = null;
        sendPoiBean = null;
        sendServerTime = null;
        sendDirectionBean = null;
        isSelectedSend = false;

        travelList.clear();
        itemInfoList.clear();
    }

    public static ArrayList<HbcLantLng> getHbcLantLngList(int cityId,CityRouteBean.Fence _fence) {
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
                if(cityId==1269 || cityId==1270){
                    HbcMapViewTools.convertToAmappCoordition(hbcLantLng);
                }
                resultList.add(hbcLantLng);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }

    public static ArrayList<HbcLantLng> getHbcLantLngList(int cityId,ArrayList<DirectionBean.Step> _steps) {
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
            if(cityId==1269 || cityId==1270){
                HbcMapViewTools.convertToAmappCoordition(hbcLantLng);
            }
            resultList.add(hbcLantLng);
        }
        return resultList;
    }

    public static HbcLantLng getHbcLantLng(int cityId,String location) {
        try {
            String[] points = location.split(",");
            HbcLantLng hbcLantLng = new HbcLantLng();
            hbcLantLng.latitude = CommonUtils.getCountDouble(points[0]);
            hbcLantLng.longitude = CommonUtils.getCountDouble(points[1]);
            if(cityId==1269 || cityId==1270){
                HbcMapViewTools.convertToAmappCoordition(hbcLantLng);
            }
            return hbcLantLng;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
