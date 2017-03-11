package com.hugboga.custom.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapsInitializer;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.Polygon;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.amap.entity.HbcLantLng;
import com.hugboga.amap.view.HbcMapView;
import com.hugboga.amap.view.HbcMapViewTools;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.CityRouteAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.AirPort;
import com.hugboga.custom.data.bean.CarMaxCapaCityBean;
import com.hugboga.custom.data.bean.ChooseDateBean;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.CityRouteBean;
import com.hugboga.custom.data.bean.DirectionBean;
import com.hugboga.custom.data.bean.FlightBean;
import com.hugboga.custom.data.bean.PoiBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestCarMaxCapaCity;
import com.hugboga.custom.data.request.RequestCityRoute;
import com.hugboga.custom.data.request.RequestDirection;
import com.hugboga.custom.models.CharterModelBehavior;
import com.hugboga.custom.utils.AlertDialogUtils;
import com.hugboga.custom.utils.CharterDataUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.DatabaseManager;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.widget.DialogUtil;
import com.hugboga.custom.widget.charter.CharterEmptyView;
import com.hugboga.custom.widget.charter.CharterSecondBottomView;
import com.hugboga.custom.widget.charter.CharterSubtitleView;
import com.hugboga.custom.widget.title.TitleBarCharterSecond;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/2/21.
 */
public class CharterSecondStepActivity extends BaseActivity implements CharterSecondBottomView.OnBottomClickListener
        , CityRouteAdapter.OnCharterItemClickListener, CharterSubtitleView.OnPickUpOrSendSelectedListener
        , CharterEmptyView.OnRefreshDataListener{

    public static final String TAG = CharterSecondStepActivity.class.getSimpleName();

    private static final int REQUEST_CITYROUTE_TYPE_NOTIFY = 1;       // 刷新数据
    private static final int REQUEST_CITYROUTE_TYPE_OUTTOWN = 2;      // 跨城市
    private static final int REQUEST_CITYROUTE_TYPE_PICKUP = 3;       // 接机，开始城市修改

    @Bind(R.id.charter_second_titlebar)
    TitleBarCharterSecond titleBar;
    @Bind(R.id.charter_second_recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.charter_second_bottom_view)
    CharterSecondBottomView bottomView;
    @Bind(R.id.charter_second_map_layout)
    HbcMapView mapView;

    private CharterSecondStepActivity.Params params;
    private CityRouteAdapter adapter;
    private CharterDataUtils charterDataUtils;
    private CityRouteBean cityRouteBean;
    private int currentDay;
    private FlightBean flightBean;

    private String lastCityId;
    private int lastType;
    private int lastSelectedRouteType;

    LayoutInflater mLayoutInflater;

    public static class Params implements Serializable {
        public CityBean startBean;
        public ChooseDateBean chooseDateBean;
        public int adultCount;
        public int childCount;
        public int maxPassengers;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            params = (CharterSecondStepActivity.Params) savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                params = (CharterSecondStepActivity.Params) bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }
        setContentView(R.layout.activity_charter_second);
        ButterKnife.bind(this);
        initMapView();
        mapView.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

        initView();
    }

    private void initMapView(){
        MapsInitializer.loadWorldGridMap(true);
        mapView.getLayoutParams().height = (int)(ScreenUtil.screenWidth*0.62f);
        mapView.getaMap().getUiSettings().setZoomControlsEnabled(false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
        if (params != null) {
            outState.putSerializable(Constants.PARAMS_DATA, params);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        bottomView.updateConfirmView();
        updateTitleBar();
        adapter.updateSubtitleModel();
        adapter.updatePickupModelVisibility();
        adapter.updateNoCharterModelVisibility();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void initView() {
        charterDataUtils = CharterDataUtils.getInstance();
        charterDataUtils.init(params);
        currentDay = charterDataUtils.currentDay;

        titleBar.getLeftView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });
        titleBar.getRightView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showServiceDialog();
            }
        });
        updateTitleBar();

        adapter = new CityRouteAdapter();
        adapter.setOnCharterItemClickListener(this);
        adapter.setOnPickUpOrSendSelectedListener(this);
        adapter.setOnRefreshDataListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        bottomView.setOnBottomClickListener(this);

        CityBean cityBean = charterDataUtils.getCurrentDayStartCityBean();
        requestCityRoute("" + cityBean.cityId, REQUEST_CITYROUTE_TYPE_NOTIFY);
        bottomView.updateConfirmView();

        locationMapToCity();//默认定位当前城市
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            finishActivity();
        }
        return super.onKeyUp(keyCode, event);
    }

    public void showServiceDialog() {
        DialogUtil.showServiceDialog(CharterSecondStepActivity.this, null, UnicornServiceActivity.SourceType.TYPE_CHARTERED, null, null, getEventSource());
    }

    public void finishActivity() {
        EventBus.getDefault().post(new EventAction(EventType.CHARTER_FIRST_REFRESH));
        finish();
    }

    public void requestCityRoute(String cityId, int type) {
        requestCityRoute(cityId, type, 0);
    }

    public void requestCityRoute(String cityId, int type, int selectedRouteType) {
        this.lastCityId = cityId;
        this.lastType = type;
        this.lastSelectedRouteType = selectedRouteType;

        requestData(new RequestCityRoute(this, cityId, type, selectedRouteType));
    }

    public void requestDirection(String origin, String destination, String countryId) {
        requestData(new RequestDirection(this, origin, destination, countryId));
    }

    public void requestCarMaxCapaCity(int cityId) {
        requestData(new RequestCarMaxCapaCity(this, cityId));
    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        super.onDataRequestSucceed(_request);
        if (_request instanceof RequestCityRoute) {
            RequestCityRoute request= (RequestCityRoute) _request;
            CityRouteBean _cityRouteBean = request.getData();
            bottomView.setVisibility(View.VISIBLE);
            if (_cityRouteBean == null || _cityRouteBean.cityRouteList == null || _cityRouteBean.cityRouteList.size() < 0) {
//                adapter.showEmpty(CharterEmptyView.EMPTY_TYPE, true);
                return;
            } else {
                adapter.showEmpty(CharterEmptyView.EMPTY_TYPE, false);
            }
            if (request.getType() == REQUEST_CITYROUTE_TYPE_OUTTOWN) {//跨城市
                charterDataUtils.addFences(charterDataUtils.currentDay, _cityRouteBean.fences, false);
                updateDrawFences();
            } else {//更新
                int routeType = 0;
                if(request.selectedRouteType != 0) {
                    routeType = request.selectedRouteType;
                } else {
                    routeType = charterDataUtils.getRouteType(charterDataUtils.currentDay - 1);
                }
                charterDataUtils.addFences(charterDataUtils.currentDay, _cityRouteBean.fences, true);
                currentDay = charterDataUtils.currentDay;
                adapter.notifyAllModelsChanged(_cityRouteBean, routeType);
                bottomView.updateConfirmView();
                charterDataUtils.setDefaultFences();

                if (request.getType() == REQUEST_CITYROUTE_TYPE_PICKUP) {//接机，修改了开始城市
                    changeTravelDate(false);
                    EventBus.getDefault().post(new EventAction(EventType.AIR_NO, flightBean));
                }
            }
            this.cityRouteBean = _cityRouteBean;
        } else if (_request instanceof RequestDirection) {
            DirectionBean directionBean = ((RequestDirection) _request).getData();
            if (charterDataUtils.isFirstDay()) {//接机
                charterDataUtils.pickUpDirectionBean = directionBean;
                adapter.updatePickupModel();
            } else {//送机
                charterDataUtils.sendDirectionBean = directionBean;
                adapter.updateSendModel();
            }
            updateDrawFences();
        } else if (_request instanceof RequestCarMaxCapaCity) {
            final CarMaxCapaCityBean carMaxCapaCityBean = ((RequestCarMaxCapaCity) _request).getData();
            if ((charterDataUtils.adultCount + charterDataUtils.childCount) > carMaxCapaCityBean.numOfPerson) {
                String title = String.format("您选择的乘客人数，超过了当地可用车型的最大载客人数（%1$s人）如需预订多车服务，请联系客服", carMaxCapaCityBean.numOfPerson);
                AlertDialogUtils.showAlertDialogCancelable(this, title, "返回上一步", "联系客服", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        charterDataUtils.clearStartDate();
                        CityBean cityBean = DatabaseManager.getCityBean("" + flightBean.arrCityId);
                        charterDataUtils.addStartCityBean(charterDataUtils.currentDay, cityBean);
                        charterDataUtils.maxPassengers = carMaxCapaCityBean.numOfPerson;
                        finishActivity();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showServiceDialog();
                        dialog.dismiss();
                    }
                });
            } else {
                int oldRouteType = charterDataUtils.getRouteType(charterDataUtils.currentDay - 1);
                charterDataUtils.clearStartDate();
                CityBean cityBean = DatabaseManager.getCityBean("" + flightBean.arrCityId);
                charterDataUtils.addStartCityBean(charterDataUtils.currentDay, cityBean);
                charterDataUtils.maxPassengers = carMaxCapaCityBean.numOfPerson;
                adapter.updateSelectedModel();
                requestCityRoute("" + cityBean.cityId, REQUEST_CITYROUTE_TYPE_PICKUP, oldRouteType);
            }
        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest _request) {
        if (_request instanceof RequestCityRoute) {
            adapter.showEmpty(CharterEmptyView.ERROR_TYPE, true);
            bottomView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRefresh(int type) {
        requestCityRoute(lastCityId, lastType, lastSelectedRouteType);
    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case AIR_NO:
                flightBean = (FlightBean) action.getData();
                if (!checkPickUpFlightBean(flightBean)) {
                    break;
                }
                if (charterDataUtils.flightBean != null && charterDataUtils.flightBean != flightBean) {
                    charterDataUtils.pickUpPoiBean = null;
                }
                charterDataUtils.flightBean = flightBean.transformData();
                charterDataUtils.isSelectedPickUp = true;
                if (charterDataUtils.chooseDateBean.dayNums > 1) {
                    adapter.showPickupModel();
                    adapter.updatePickupModel();
                }
                adapter.updateSubtitleModel();
                updateDrawFences();
                break;
            case CHOOSE_POI_BACK:
                PoiBean poiBean = (PoiBean) action.getData();
                if (poiBean.mBusinessType == Constants.BUSINESS_TYPE_PICK) {
                    if (charterDataUtils.pickUpPoiBean == poiBean) {
                        break;
                    }
                    charterDataUtils.pickUpPoiBean = poiBean;
                    CityBean startCityBean = charterDataUtils.getCurrentDayStartCityBean();
                    requestDirection(charterDataUtils.flightBean.arrLocation, charterDataUtils.pickUpPoiBean.location, startCityBean.placeId);
                } else if(poiBean.mBusinessType == Constants.BUSINESS_TYPE_SEND) {
                    if (charterDataUtils.sendPoiBean == poiBean) {
                        break;
                    }
                    charterDataUtils.sendPoiBean = poiBean;
                    CityBean startCityBean = charterDataUtils.getCurrentDayStartCityBean();
                    requestDirection(charterDataUtils.sendPoiBean.location, charterDataUtils.airPortBean.location, startCityBean.placeId);
                }
                updateDrawFences();
                break;
            case CHOOSE_END_CITY_BACK:
                CityBean cityBean = (CityBean) action.getData();
                CityBean oldCityBean = charterDataUtils.getEndCityBean();
                if (cityBean == null || oldCityBean == cityBean) {
                    return;
                }
                charterDataUtils.addEndCityBean(charterDataUtils.currentDay, cityBean);
                adapter.updateSelectedModel();
                requestCityRoute("" + cityBean.cityId, REQUEST_CITYROUTE_TYPE_OUTTOWN);//跨城市
                break;
            case AIR_PORT_BACK:
                AirPort airPortBean = (AirPort) action.getData();
                if (charterDataUtils.airPortBean != null && charterDataUtils.airPortBean != airPortBean) {
                    charterDataUtils.sendPoiBean = null;
                    charterDataUtils.sendServerTime = null;
                }
                charterDataUtils.airPortBean = airPortBean;
                charterDataUtils.isSelectedSend = true;
                adapter.showSendModel();
                adapter.updateSendModel();
                adapter.updateSubtitleModel();
                updateDrawFences();
                break;
            case CHARTER_LIST_REFRESH:
                int selectedDay = (int) action.getData();
                if (selectedDay == currentDay) {
                    recyclerView.smoothScrollToPosition(0);
                    return;
                }
                charterDataUtils.currentDay = selectedDay;
                CityBean nextCityBean = charterDataUtils.getStartCityBean(selectedDay);
                if (nextCityBean == null) {
                    nextCityBean = charterDataUtils.setDefaultCityBean();
                }
                requestCityRoute("" + nextCityBean.cityId, REQUEST_CITYROUTE_TYPE_NOTIFY);
                adapter.updateSubtitleModel();
                if (charterDataUtils.chooseDateBean.dayNums == 1) {
                    adapter.hideSendModel();
                }
                recyclerView.smoothScrollToPosition(0);
                break;
            case CHOOSE_START_CITY_BACK://修改出行城市
                CityBean startCityBean = (CityBean) action.getData();
                if (!CharterSecondStepActivity.TAG.equals(startCityBean.fromTag)
                        || startCityBean == null
                        || startCityBean == charterDataUtils.getCurrentDayStartCityBean()) {
                    return;
                }
                charterDataUtils.clearSendInfo();
                charterDataUtils.itemInfoList.remove(charterDataUtils.currentDay);
                charterDataUtils.addStartCityBean(charterDataUtils.currentDay, startCityBean);
                requestCityRoute("" + startCityBean.cityId, REQUEST_CITYROUTE_TYPE_NOTIFY, CityRouteBean.RouteType.URBAN);
                break;
        }
    }

    @Override
    public String getEventSource() {
        return getString(R.string.custom_chartered);
    }

    @Override
    public void confirm() {
        if (cityRouteBean == null || !checkInfo()) {
            return;
        }
        if (!CommonUtils.isLogin(this)) {
            return;
        }
        if (charterDataUtils.isLastDay()) {//最后一天"查看报价"
            Intent intent = new Intent(this, CombinationOrderActivity.class);
            startActivity(intent);
        } else {
            CityBean currentCityBean = charterDataUtils.getCurrentDayStartCityBean();
            charterDataUtils.currentDay++;
            CityBean nextCityBean = charterDataUtils.setDefaultCityBean();
            bottomView.updateConfirmView();
            if (currentCityBean == nextCityBean && cityRouteBean.cityId == nextCityBean.cityId) {
                adapter.notifyAllModelsChanged(cityRouteBean, charterDataUtils.getRouteType(charterDataUtils.currentDay - 1));
                charterDataUtils.setDefaultFences();
                updateDrawFences();
            } else {
                adapter.updateSubtitleModel();
                requestCityRoute("" + nextCityBean.cityId, REQUEST_CITYROUTE_TYPE_NOTIFY);
            }
            currentDay = charterDataUtils.currentDay;
            recyclerView.smoothScrollToPosition(0);
        }
    }

    @Override
    public void intentTravelList() {
        Intent intent = new Intent(this, TravelListActivity.class);
        intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
        startActivity(intent);
        overridePendingTransition(R.anim.push_bottom_in, 0);
    }

    public boolean checkPickUpFlightBean(FlightBean flightBean) {
        final boolean checkCity = charterDataUtils.getStartCityBean(1).cityId != flightBean.arrCityId;
        final boolean checkDate = !charterDataUtils.chooseDateBean.start_date.equals(flightBean.arrDate);
        String title = null;
        if (checkCity && checkDate) {//选航班后降落日期，不等于开始日期；且降落城市，不等于开始城市
            title = String.format("根据您选择的航班，如果继续下单需要将包车开始城市和开始日期，调整为您航班的落地城市%1$s和当地日期%2$s", flightBean.arrCityName, flightBean.arrDate);
        } else if (checkCity) {//选航班后降落城市，不等于开始城市
            title = String.format("根据您选择的航班，如果继续下单需要将包车开车城市，调整为您航班的落地城市%1$s", flightBean.arrCityName);
        } else if (checkDate) {//选航班后降落日期，不等于开始日期
            title = String.format("根据您选择的航班，如果继续下单需要将包车开始日期，调整为您航班在当地的落地日期%1$s", flightBean.arrDate);
        }
        if (!TextUtils.isEmpty(title)) {
            showCheckPickUpDialog(flightBean, title, checkCity, checkDate);
            return false;
        } else {
            return true;
        }
    }

    public void showCheckPickUpDialog(final FlightBean _flightBean, final String title, final boolean checkCity, final boolean checkDate) {
        AlertDialogUtils.showAlertDialogCancelable(this, title, "取消，重选航班", "好的", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(CharterSecondStepActivity.this, ChooseAirActivity.class);
                intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                CharterSecondStepActivity.this.startActivity(intent);
                dialog.dismiss();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (checkCity && checkDate) {
                    requestCarMaxCapaCity(_flightBean.arrCityId);
                } else if (checkCity) {
                    requestCarMaxCapaCity(_flightBean.arrCityId);
                } else if (checkDate) {
                    changeTravelDate(true);
                }
                dialog.dismiss();
            }
        });
    }

    public void changeTravelDate(boolean isUpdatePickUpModel) {
        ChooseDateBean chooseDateBean = charterDataUtils.chooseDateBean;
        if (chooseDateBean.start_date.equals(flightBean.arrDate)) {
            return;
        }
        chooseDateBean.start_date = flightBean.arrDate;
        chooseDateBean.end_date = DateUtils.getDay(chooseDateBean.start_date, chooseDateBean.dayNums - 1);
        chooseDateBean.startDate = DateUtils.getDateByStr(chooseDateBean.start_date);
        chooseDateBean.endDate = DateUtils.getDateByStr(chooseDateBean.end_date);
        chooseDateBean.showStartDateStr = DateUtils.orderChooseDateTransform(chooseDateBean.start_date);
        chooseDateBean.showEndDateStr = DateUtils.orderChooseDateTransform(chooseDateBean.end_date);
        updateTitleBar();
        if (isUpdatePickUpModel) {
            EventBus.getDefault().post(new EventAction(EventType.AIR_NO, flightBean));
        }
    }

    public boolean checkInfo() {
        CharterModelBehavior selectedCharterModel = adapter.getSelectedModel();
        if (selectedCharterModel == null) {
            return false;
        }
        return charterDataUtils.checkInfo(selectedCharterModel.getRouteType(), charterDataUtils.currentDay, true);
    }

    public void updateTitleBar() {
        ChooseDateBean chooseDateBean = charterDataUtils.chooseDateBean;
        String title = "";
//        if (chooseDateBean.dayNums == 1 && charterDataUtils.travelList != null && charterDataUtils.travelList.get(0).routeType == CityRouteBean.RouteType.HALFDAY) {
//            title = String.format("%1$s(0.5天)", chooseDateBean.showStartDateStr);
//        } else {
            title = String.format("%1$s-%2$s(%3$s天)", chooseDateBean.showStartDateStr, chooseDateBean.showEndDateStr, chooseDateBean.dayNums);
//        }
        titleBar.updateSubtitle(title);
    }

    @Override
    public void onPickUpOrSendSelected(boolean isPickUp, boolean isSelected) {
        updateDrawFences();
    }

    @Override
    public void onCharterItemClick(CityRouteBean.CityRouteScope cityRouteScope) {
        charterDataUtils.addCityRouteScope(cityRouteScope);
        updateTitleBar();
        drawFences(cityRouteScope);
    }

    public void updateDrawFences() {
        CharterModelBehavior charterModelBehavior = adapter.getSelectedModel();
        if (charterModelBehavior != null) {
            drawFences(charterModelBehavior.getCityRouteScope());
        }
    }

    public void drawFences(CityRouteBean.CityRouteScope cityRouteScope) {
        mapView.getaMap().clear();
        locationMapToCity();
        final int routeType = cityRouteScope.routeType;
        final boolean isOpeanFence = cityRouteScope.isOpeanFence();
        if (routeType == CityRouteBean.RouteType.AT_WILL) {//随便转转
            //TODO 关闭围栏
            mapView.getaMap().clear();
            return;
        }


        boolean isDrawFences = isOpeanFence;;
        HbcLantLng startCoordinate = null;
        Marker startMarker = null;
        if (charterDataUtils.isFirstDay() && charterDataUtils.isSelectedPickUp && charterDataUtils.flightBean != null) {//接机点
            if (charterDataUtils.pickUpPoiBean == null && charterDataUtils.isSelectedPickUp) {//点
                startCoordinate = charterDataUtils.getHbcLantLng(charterDataUtils.flightBean.arrLocation);
                //TODO  点 startCoordinate;
                if(startCoordinate!=null){
                    startMarker = mapView.addMarker(getIconView(R.mipmap.map_icon_plane,R.drawable.map_read_point,""),startCoordinate);
                    mapView.getaMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(startCoordinate.latitude,startCoordinate.longitude),16));
                }
            } else if (charterDataUtils.isSelectedPickUp) {//点到点
                isDrawFences = false;
                //TODO 点到点 charterDataUtils.flightBean.arrLocation   charterDataUtils.pickUpPoiBean.location  酒店charterDataUtils.pickUpPoiBean.placeName
                //mapView.getaMap().clear();
                DirectionBean directionBean = charterDataUtils.pickUpDirectionBean;
                if (directionBean != null && directionBean.isHaveLines()) {//是否画点到点间的线
                    ArrayList<HbcLantLng> hbcLantLngList = charterDataUtils.getHbcLantLngList(directionBean.steps);
                    //TODO 线
                    if(hbcLantLngList!=null && hbcLantLngList.size()>0){
                        mapView.addPoyline(hbcLantLngList,6, Color.argb(150,246,50,7),false);
                    }
                }
                HbcLantLng shbcLantLng = CharterDataUtils.getHbcLantLng(charterDataUtils.flightBean.arrLocation);
                HbcLantLng ehbcLantLng = CharterDataUtils.getHbcLantLng(charterDataUtils.pickUpPoiBean.location);
                if(shbcLantLng!=null && ehbcLantLng!=null){
                    List<HbcLantLng> hbcLantLngList = new ArrayList<>();
                    hbcLantLngList.add(shbcLantLng);
                    hbcLantLngList.add(ehbcLantLng);
                    LatLngBounds latLngBounds =  HbcMapViewTools.getMapLatLngBounds(hbcLantLngList);
                    mapView.getaMap().moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds,0));
                    mapView.addMarker(getIconView(R.mipmap.map_icon_plane,R.drawable.map_read_point,""),shbcLantLng);
                    mapView.addMarker(getIconView(R.drawable.map_popbg,R.drawable.map_read_point,charterDataUtils.pickUpPoiBean.placeName),ehbcLantLng);
                }
            }
        } else if ((charterDataUtils.isLastDay() && charterDataUtils.isSelectedSend && charterDataUtils.airPortBean != null)) {//送机点
            if (charterDataUtils.sendPoiBean == null && charterDataUtils.isSelectedSend) {//点加围栏
                startCoordinate = charterDataUtils.getHbcLantLng(charterDataUtils.airPortBean.location);
                //TODO  点
                if(startCoordinate!=null){
                    mapView.addMarker(getIconView(R.mipmap.map_icon_plane,R.drawable.map_read_point,""),startCoordinate);
                    mapView.getaMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(startCoordinate.latitude,startCoordinate.longitude),16));
                }
            } else if(charterDataUtils.isSelectedSend) {//点到点
                isDrawFences = false;
                //TODO  点到点 charterDataUtils.airPortBean.location    charterDataUtils.sendPoiBean.location  charterDataUtils.sendPoiBean.placeName
                DirectionBean directionBean = charterDataUtils.sendDirectionBean;
                if (directionBean.isHaveLines()) {
                    ArrayList<HbcLantLng> hbcLantLngList = charterDataUtils.getHbcLantLngList(directionBean.steps);
                    //TODO  线
                    if(hbcLantLngList!=null && hbcLantLngList.size()>0){
                        mapView.addPoyline(hbcLantLngList,6, Color.argb(150,246,50,7),false);
                    }
                }
                HbcLantLng shbcLantLng = CharterDataUtils.getHbcLantLng(charterDataUtils.sendPoiBean.location);
                HbcLantLng ehbcLantLng = CharterDataUtils.getHbcLantLng(charterDataUtils.airPortBean.location);
                if(shbcLantLng!=null && ehbcLantLng!=null){
                    List<HbcLantLng> hbcLantLngList = new ArrayList<>();
                    hbcLantLngList.add(shbcLantLng);
                    hbcLantLngList.add(ehbcLantLng);
                    LatLngBounds latLngBounds =  HbcMapViewTools.getMapLatLngBounds(hbcLantLngList);
                    mapView.addMarker(getIconView(R.mipmap.map_icon_plane,R.drawable.map_read_point,""),shbcLantLng);
                    mapView.addMarker(getIconView(R.drawable.map_popbg,R.drawable.map_read_point,charterDataUtils.sendPoiBean.placeName),ehbcLantLng);
                    mapView.getaMap().moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds,0));
                }
            }
        } else if (!isOpeanFence) {//未开启围栏
            CityBean cityBean = charterDataUtils.getCurrentDayStartCityBean();
            //TODO cityBean.name 点加城市名 需要加经纬度
            isDrawFences = false;
            String location = cityBean.location;
            if(!TextUtils.isEmpty(location)){
                HbcLantLng hbcLantLng = CharterDataUtils.getHbcLantLng(location);
                if (hbcLantLng != null) {
                    mapView.addMarker(getIconView(R.drawable.map_pop_city,R.drawable.map_green_point,cityBean.name),hbcLantLng);
                    mapView.getaMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(hbcLantLng.latitude,hbcLantLng.longitude),12));
                }
            }
        }

        if (!isDrawFences) {
            return;
        }
        ArrayList<CityRouteBean.Fence> fences = charterDataUtils.getCurrentDayFences();
        if (fences == null || fences.size() < 1) {
            return;
        }
        ArrayList<HbcLantLng> urbanList = charterDataUtils.getHbcLantLngList(fences.get(0));//市内围栏
        ArrayList<HbcLantLng> outsideList = fences.size() > 1 ? charterDataUtils.getHbcLantLngList(fences.get(1)) : null;//周边围栏
        ArrayList<CityRouteBean.Fence> nextFences = charterDataUtils.getNextDayFences();
        if (routeType == CityRouteBean.RouteType.OUTTOWN) {//跨城市 画两个围栏
            CityBean currentCityBean = charterDataUtils.getCurrentDayStartCityBean();
            CityBean nextCityBean = charterDataUtils.getEndCityBean();
            // TODO 跨城市 画两个围栏 hbcLantLngList nextHbcLantLngList currentCityBean.name  nextCityBean.name
            if(urbanList!=null && urbanList.size()>0){
                mapView.addPolygon(urbanList,Color.argb(150,125,211,32),8,Color.argb(90,125,211,32));
                if(!TextUtils.isEmpty(currentCityBean.name)){
                    mapView.addText(currentCityBean.name,100,Color.argb(125,30,55,1),urbanList);
                }
            }
            if(nextFences != null && nextFences.get(0) != null){
                CityRouteBean.Fence nextFence = nextFences.get(0);
                ArrayList<HbcLantLng> nextHbcLantLngList = charterDataUtils.getHbcLantLngList(nextFence);
                if (nextHbcLantLngList!=null && nextHbcLantLngList.size()>0) {
                    mapView.addPolygon(nextHbcLantLngList,Color.argb(150,125,211,32),8,Color.argb(90,125,211,32));
                    if(nextCityBean != null && !TextUtils.isEmpty(nextCityBean.name)){
                        mapView.addText(nextCityBean.name,100,Color.argb(125,30,55,1),nextHbcLantLngList);
                    }
                    mapView.getaMap().moveCamera(CameraUpdateFactory.newLatLngBounds(HbcMapViewTools.getMapLatLngBounds(urbanList,nextHbcLantLngList),0));
                }
            } else {
                CityBean cityBean = charterDataUtils.getEndCityBean(charterDataUtils.currentDay);
                if (cityBean == null) {
                    return;
                }
                //TODO cityBean.name 点加城市名 需要加经纬度
                String location = cityBean.location;
                if(!TextUtils.isEmpty(location)){
                    HbcLantLng hbcLantLng = CharterDataUtils.getHbcLantLng(location);
                    if (hbcLantLng != null) {
                        mapView.addMarker(getIconView(R.drawable.map_pop_city,R.drawable.map_green_point,cityBean.name),hbcLantLng);
                        mapView.getaMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(hbcLantLng.latitude,hbcLantLng.longitude),12));
                        ArrayList<HbcLantLng> hbcLantLngList = new ArrayList<>();
                        hbcLantLngList.add(hbcLantLng);
                        mapView.getaMap().moveCamera(CameraUpdateFactory.newLatLngBounds(HbcMapViewTools.getMapLatLngBounds(urbanList, hbcLantLngList),0));
                    }
                }
            }

        } else {
            if (startCoordinate != null) {
                // TODO 围栏 startCoordinate hbcLantLngList nextHbcLantLngList 判断坐标点在 市内（一个圈）、周边（俩圈）、超出周边（俩圈）
                boolean hasOut = false;
                //urbanList  outsideList startCoordinate
                if(urbanList!=null && urbanList.size()>0){
                    Polygon polygon = mapView.addPolygon(urbanList, Color.argb(150,125,211,32), 8, Color.argb(90,125,211,32));
                    if (routeType == CityRouteBean.RouteType.SUBURBAN
                            && !polygon.contains(new LatLng(startCoordinate.latitude, startCoordinate.longitude))
                            && outsideList != null && outsideList.size() > 0) {
                        mapView.addPolygon(outsideList,Color.argb(150,125,211,32),8,Color.argb(90,125,211,32));
                        hasOut = true;
                    }
                    List<HbcLantLng> hbcLantLngs = new ArrayList<>();
                    hbcLantLngs.add(startCoordinate);
                    if(hasOut){
                        mapView.getaMap().moveCamera(CameraUpdateFactory.newLatLngBounds(HbcMapViewTools.getMapLatLngBounds(outsideList,urbanList,hbcLantLngs),0));
                    }else {
                        mapView.getaMap().moveCamera(CameraUpdateFactory.newLatLngBounds(HbcMapViewTools.getMapLatLngBounds(urbanList,hbcLantLngs),0));
                    }
                }

            } else if (routeType == CityRouteBean.RouteType.SUBURBAN) {
                if (outsideList == null || outsideList.size() <= 0 || urbanList == null || urbanList.size() <= 0) {
                    return;
                }
                // 周边围栏
                mapView.addPolygon(outsideList,Color.argb(150,125,211,32),8,Color.argb(90,125,211,32));
                mapView.addPolygon(urbanList,Color.argb(150,125,211,32),8,Color.argb(90,125,211,32));
                mapView.getaMap().moveCamera(CameraUpdateFactory.newLatLngBounds(HbcMapViewTools.getMapLatLngBounds(outsideList,urbanList),0));
            } else {
                // TODO urbanList 市内围栏
                if(urbanList!=null && urbanList.size()>0){
                    mapView.addPolygon(urbanList,Color.argb(150,125,211,32),8,Color.argb(90,125,211,32));
                    mapView.getaMap().moveCamera(CameraUpdateFactory.newLatLngBounds(HbcMapViewTools.getMapLatLngBounds(urbanList),0));
                }
            }
        }
    }

    private void convertLatLng(CityRouteBean.CityRouteScope cityRouteScope){


    }

    private View getIconView(int popSrc,int pointSrc,String text){
        if(mLayoutInflater==null){
            mLayoutInflater = LayoutInflater.from(MyApplication.getAppContext());
        }
        View view = mLayoutInflater.inflate(R.layout.mark_window_layout,null);
        view.findViewById(R.id.mark_window).setBackgroundResource(popSrc);
        view.findViewById(R.id.mark_point).setBackgroundResource(pointSrc);
        if(!TextUtils.isEmpty(text)){
            TextView textView = ((TextView)view.findViewById(R.id.mark_title));
            textView.setVisibility(View.VISIBLE);
            textView.setText(text);
        }
        return view;
    }

    private void locationMapToCity(){
        CityBean cityBean = charterDataUtils.getStartCityBean(1);
        if(cityBean==null){
            return;
        }
        HbcLantLng hbcLantLng =  CharterDataUtils.getHbcLantLng(cityBean.location);
        mapView.getaMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(hbcLantLng.latitude,hbcLantLng.longitude),16));
    }
}
