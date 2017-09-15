package com.hugboga.custom.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyModel;
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
import com.hugboga.custom.data.bean.GuideCropBean;
import com.hugboga.custom.data.bean.PoiBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestCarMaxCapaCity;
import com.hugboga.custom.data.request.RequestCityRoute;
import com.hugboga.custom.data.request.RequestDirection;
import com.hugboga.custom.models.CharterModelBehavior;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.AlertDialogUtils;
import com.hugboga.custom.utils.CharterDataUtils;
import com.hugboga.custom.utils.CharterFragmentAgent;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.DatabaseManager;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.utils.SharedPre;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.CsDialog;
import com.hugboga.custom.widget.charter.CharterEmptyView;
import com.hugboga.custom.widget.charter.CharterItemView;
import com.hugboga.custom.widget.charter.CharterSecondBottomView;
import com.hugboga.custom.widget.charter.CharterSubtitleView;
import com.hugboga.custom.widget.title.TitleBarCharterSecond;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by qingcha on 17/2/21.
 */
public class CharterSecondStepActivity extends BaseActivity implements CharterSecondBottomView.OnBottomClickListener
        , CityRouteAdapter.OnCharterItemClickListener, CharterSubtitleView.OnPickUpOrSendSelectedListener
        , CharterEmptyView.OnRefreshDataListener{

    private static final int REQUEST_CITYROUTE_TYPE_NOTIFY = 1;       // 刷新数据
    private static final int REQUEST_CITYROUTE_TYPE_OUTTOWN = 2;      // 跨城市
    private static final int REQUEST_CITYROUTE_TYPE_PICKUP = 3;       // 接机，开始城市修改

    @Bind(R.id.charter_second_top_layout)
    LinearLayout topLayout;
    @Bind(R.id.charter_second_titlebar)
    TitleBarCharterSecond titleBar;
    @Bind(R.id.charter_second_bottom_view)
    CharterSecondBottomView bottomView;
    @Bind(R.id.charter_second_map_layout)
    HbcMapView mapView;
    @Bind(R.id.charter_second_list_container)
    FrameLayout listContainer;
    @Bind(R.id.charter_second_seckills_layout)
    RelativeLayout seckillsLayout;

    @Bind(R.id.charter_second_unfold_map_layout)
    LinearLayout unfoldMapLayout;
    @Bind(R.id.charter_second_packup_map_layout)
    FrameLayout packupMapLayout;

    private CharterFragmentAgent fragmentAgent;

    private CharterDataUtils charterDataUtils;
    private CityRouteBean cityRouteBean;
    private int currentDay;
    private FlightBean flightBean;

    private String lastCityId;
    private int lastType;
    private int lastSelectedRouteType;

    private LayoutInflater mLayoutInflater;

    private boolean isUnfoldMap = false;
    CsDialog csDialog;
    @Override
    public int getContentViewId() {
        return R.layout.activity_charter_second;
    }

    //此页有map，不能解绑
    protected boolean isUnbinded(){
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initView(savedInstanceState);
    }

    private void initMapView(Bundle savedInstanceState) {
        MapsInitializer.loadWorldGridMap(true);
        int topMargin = getMapTopMargin();
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mapView.getLayoutParams();
        params.height = getMapHight(false);
        params.topMargin = topMargin;
        mapView.getaMap().getUiSettings().setZoomControlsEnabled(false);
        mapView.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        bottomView.updateConfirmView();
        updateTitleBar();
        updateSeckillsLayout();
        final int topMargin = getMapTopMargin();
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mapView.getLayoutParams();
        params.topMargin = topMargin;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null) {
            mapView.onDestroy();
        }
        EventBus.getDefault().unregister(this);
    }

    public void initView(Bundle savedInstanceState) {
        charterDataUtils = CharterDataUtils.getInstance();
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

        initMapView(savedInstanceState);

        fragmentAgent = new CharterFragmentAgent(this, R.id.charter_second_list_container);
        fragmentAgent.setOnCharterItemClickListener(this);
        fragmentAgent.setOnPickUpOrSendSelectedListener(this);
        fragmentAgent.setOnRefreshDataListener(this);

        bottomView.setOnBottomClickListener(this);

        CityBean cityBean = charterDataUtils.getCurrentDayStartCityBean();
        requestCityRoute("" + cityBean.cityId, REQUEST_CITYROUTE_TYPE_NOTIFY);
        bottomView.updateConfirmView();

        locationMapToCity(charterDataUtils.getStartCityBean(1));//默认定位当前城市

        StatisticClickEvent.dailyClick(StatisticConstant.LAUNCH_RWEILAN, getIntentSource(), charterDataUtils.chooseDateBean.dayNums,
                charterDataUtils.guidesDetailData != null, (charterDataUtils.adultCount + charterDataUtils.childCount) + "");
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            if (isUnfoldMap) {
                onUnfoldMap(false);
               return true;
            }
            finishActivity();
        }
        return super.onKeyUp(keyCode, event);
    }

    public void showServiceDialog() {
        SensorsUtils.onAppClick(getEventSource(), "客服", getIntentSource());
        //DialogUtil.showServiceDialog(CharterSecondStepActivity.this, null, UnicornServiceActivity.SourceType.TYPE_CHARTERED, null, null, getEventSource());
        csDialog = CommonUtils.csDialog(CharterSecondStepActivity.this, null, null, null, UnicornServiceActivity.SourceType.TYPE_CHARTERED, getEventSource(), new CsDialog.OnCsListener() {
            @Override
            public void onCs() {
                if (csDialog != null && csDialog.isShowing()) {
                    csDialog.dismiss();
                }
            }
        });

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
        String carIds = "";
        if (charterDataUtils != null && charterDataUtils.guidesDetailData != null) {
            carIds = charterDataUtils.guidesDetailData.getCarIds();
        }
        String activityNo = "";
        if (charterDataUtils != null && charterDataUtils.seckillsBean != null) {
            activityNo = charterDataUtils.seckillsBean.timeLimitedSaleNo;
        }
        requestData(new RequestCarMaxCapaCity(this, cityId, carIds, activityNo));
    }
    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        super.onDataRequestSucceed(_request);
        if (_request instanceof RequestCityRoute) {
            RequestCityRoute request= (RequestCityRoute) _request;
            CityRouteBean _cityRouteBean = request.getData();
            if (_cityRouteBean == null || _cityRouteBean.cityRouteList == null || _cityRouteBean.cityRouteList.size() < 0) {
                if (request.getType() == REQUEST_CITYROUTE_TYPE_OUTTOWN) {
                    charterDataUtils.addEndCityBean(charterDataUtils.currentDay, null);
                    fragmentAgent.updateSelectedModel();
                    CommonUtils.showToast(R.string.daily_second_city_cannot_hint);
                } else {
                    if (charterDataUtils.currentDay > 1) {
                        charterDataUtils.cleanSendInfo();
                        charterDataUtils.itemInfoList.remove(charterDataUtils.currentDay);
                        charterDataUtils.addStartCityBean(charterDataUtils.currentDay, charterDataUtils.setDefaultCityBean(charterDataUtils.currentDay));
                        charterDataUtils.travelList.remove(charterDataUtils.currentDay - 1);
                    }
                    fragmentAgent.showEmpty(CharterEmptyView.EMPTY_TYPE, true);
                    bottomView.setConfirmViewEnabled(false);
                }
                return;
            } else {
                fragmentAgent.showEmpty(CharterEmptyView.EMPTY_TYPE, false);
                bottomView.setConfirmViewEnabled(true);
                bottomView.setVisibility(View.VISIBLE);
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
                fragmentAgent.notifyAllModelsChanged(_cityRouteBean, routeType);
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
                fragmentAgent.updatePickupModel();
            } else {//送机
                charterDataUtils.sendDirectionBean = directionBean;
                fragmentAgent.updateSendModel();
            }
            updateDrawFences();
        } else if (_request instanceof RequestCarMaxCapaCity) {
            final CarMaxCapaCityBean carMaxCapaCityBean = ((RequestCarMaxCapaCity) _request).getData();
            if (charterDataUtils.getTotalPeopleCount() > carMaxCapaCityBean.numOfPerson) {
                String title = getString(R.string.daily_second_exceed_max_person, "" + carMaxCapaCityBean.numOfPerson);
                AlertDialogUtils.showAlertDialogCancelable(this, title, getString(R.string.last_step), getString(R.string.contact_service), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        charterDataUtils.cleanStartDate();
                        CityBean cityBean = DatabaseManager.getCityBean("" + flightBean.arrCityId);
                        charterDataUtils.addStartCityBean(charterDataUtils.currentDay, cityBean);
                        charterDataUtils.maxPassengers = carMaxCapaCityBean.numOfPerson;
                        charterDataUtils.isSupportChildSeat = carMaxCapaCityBean.isSupportChildSeat();
                        charterDataUtils.flightBean = flightBean;
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
                charterDataUtils.cleanStartDate();
                CityBean cityBean = DatabaseManager.getCityBean("" + flightBean.arrCityId);
                charterDataUtils.addStartCityBean(charterDataUtils.currentDay, cityBean);
                charterDataUtils.maxPassengers = carMaxCapaCityBean.numOfPerson;
                charterDataUtils.isSupportChildSeat = carMaxCapaCityBean.isSupportChildSeat();
                fragmentAgent.updateSelectedModel();
                requestCityRoute("" + cityBean.cityId, REQUEST_CITYROUTE_TYPE_PICKUP, oldRouteType);
            }
        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest _request) {
        super.onDataRequestError(errorInfo, _request);
        if (isFinishing()) {
            return;
        }
        if (_request instanceof RequestCityRoute && fragmentAgent != null && bottomView != null) {
            fragmentAgent.showEmpty(CharterEmptyView.ERROR_TYPE, true);
            bottomView.setConfirmViewEnabled(false);
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
                charterDataUtils.flightBean = flightBean;
                charterDataUtils.isSelectedPickUp = true;
                if (charterDataUtils.chooseDateBean.dayNums > 1) {
                    fragmentAgent.showPickupModel();
//                    fragmentAgent.updatePickupModel();
                }
//                fragmentAgent.updateSubtitleModel();
                fragmentAgent.notifyDataSetChanged();
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
                if (CharterItemView.TAG.equals(cityBean.fromTag)) {//跨城市
                    CityBean oldCityBean = charterDataUtils.getEndCityBean();
                    if (cityBean == null || oldCityBean == cityBean) {
                        return;
                    }
                    charterDataUtils.addEndCityBean(charterDataUtils.currentDay, cityBean);
                    fragmentAgent.updateSelectedModel();
                    requestCityRoute("" + cityBean.cityId, REQUEST_CITYROUTE_TYPE_OUTTOWN);//跨城市
                }
                break;
            case CHOOSE_START_CITY_BACK:
                CityBean cityBeanData = (CityBean) action.getData();
                if (CharterSubtitleView.TAG.equals(cityBeanData.fromTag)) {//修改开始城市，第二天后可修改
                    CityBean startCityBean = (CityBean) action.getData();
                    if (startCityBean == null || startCityBean == charterDataUtils.getCurrentDayStartCityBean()) {
                        return;
                    }
                    charterDataUtils.cleanSendInfo();
                    charterDataUtils.itemInfoList.remove(charterDataUtils.currentDay);
                    charterDataUtils.addStartCityBean(charterDataUtils.currentDay, startCityBean);
                    requestCityRoute("" + startCityBean.cityId, REQUEST_CITYROUTE_TYPE_NOTIFY, CityRouteBean.RouteType.URBAN);
                }
                break;
            case AIR_PORT_BACK:
                AirPort airPortBean = (AirPort) action.getData();
                if (charterDataUtils.airPortBean != null && charterDataUtils.airPortBean != airPortBean) {
                    charterDataUtils.sendPoiBean = null;
                    charterDataUtils.sendServerTime = null;
                }
                charterDataUtils.airPortBean = airPortBean;
                charterDataUtils.isSelectedSend = true;
                fragmentAgent.showSendModel();
//                fragmentAgent.updateSendModel();
//                fragmentAgent.updateSubtitleModel();
                fragmentAgent.notifyDataSetChanged();
                updateDrawFences();
                break;
            case CHARTER_LIST_REFRESH:
                RefreshBean refreshBean = (RefreshBean) action.getData();
                int selectedDay = refreshBean.day;
                if (selectedDay == currentDay && !refreshBean.isRefresh) {
                    fragmentAgent.smoothScrollToPosition(0);
                    return;
                }
                charterDataUtils.currentDay = selectedDay;
                CityBean nextCityBean = charterDataUtils.getStartCityBean(selectedDay);
                if (nextCityBean == null) {
                    nextCityBean = charterDataUtils.setDefaultCityBean(charterDataUtils.currentDay);
                }
                requestCityRoute("" + nextCityBean.cityId, REQUEST_CITYROUTE_TYPE_NOTIFY);
                fragmentAgent.updateSubtitleModel();
                if (charterDataUtils.chooseDateBean.dayNums == 1) {
                    fragmentAgent.hideSendModel();
                }
                fragmentAgent.smoothScrollToPosition(0);
                break;
        }
    }

    @Override
    public String getEventSource() {
        return "按天包车游";
    }

    @Override
    public void previous() {
        final CityBean currentCityBean = charterDataUtils.getCurrentDayStartCityBean();
        charterDataUtils.currentDay--;
        final CityBean preCityBean = charterDataUtils.getStartCityBean(charterDataUtils.currentDay);
        changeData(false, currentCityBean, preCityBean);
    }

    @Override
    public void confirm() {
        if (cityRouteBean == null || !checkInfo()) {
            return;
        }
        if (charterDataUtils.isLastDay()) {//最后一天"查看报价"
            if (!CommonUtils.isLogin(this,getEventSource())) {
                return;
            }
            Intent intent = new Intent(this, CombinationOrderActivity.class);
            startActivity(intent);

            StatisticClickEvent.dailyClick(StatisticConstant.CONFIRM2_R, getIntentSource(), charterDataUtils.chooseDateBean.dayNums,
                    charterDataUtils.guidesDetailData != null, (charterDataUtils.adultCount + charterDataUtils.childCount) + "");
            charterDataUtils.setSensorsConfirmEvent(this);
            SensorsUtils.onAppClick(getEventSource(), "查看报价", getIntentSource());
        } else {
            final CityBean currentCityBean = charterDataUtils.getCurrentDayStartCityBean();
            charterDataUtils.currentDay++;
            final CityBean nextCityBean = charterDataUtils.setDefaultCityBean(charterDataUtils.currentDay);
            changeData(true, currentCityBean, nextCityBean);
        }
    }

    public void changeData(boolean isNext, final CityBean currentCityBean, final CityBean nextCityBean) {
        bottomView.updateConfirmView();
        fragmentAgent.setCurrentFg(isNext, new CharterFragmentAgent.OnInitializedListener() {
            @Override
            public void onInitialized() {
                if (currentCityBean == nextCityBean && cityRouteBean.cityId == nextCityBean.cityId) {
                    fragmentAgent.notifyAllModelsChanged(cityRouteBean, charterDataUtils.getRouteType(charterDataUtils.currentDay - 1));
                    charterDataUtils.setDefaultFences();
                    updateDrawFences();
                } else {
                    fragmentAgent.updateSubtitleModel();
                    requestCityRoute("" + nextCityBean.cityId, REQUEST_CITYROUTE_TYPE_NOTIFY);
                }
                currentDay = charterDataUtils.currentDay;
                fragmentAgent.smoothScrollToPosition(0);
            }
        });
    }

    @Override
    public void intentTravelList() {
        Intent intent = new Intent(this, TravelListActivity.class);
        intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
        startActivity(intent);
        overridePendingTransition(R.anim.push_bottom_in, 0);
        StatisticClickEvent.click(StatisticConstant.R_XINGCHENG, "包车下单");
    }

    public boolean checkPickUpFlightBean(FlightBean flightBean) {
        final boolean checkCity = charterDataUtils.getStartCityBean(1).cityId != flightBean.arrCityId;
        final boolean checkDate = !charterDataUtils.chooseDateBean.start_date.equals(flightBean.arrDate);
        if (charterDataUtils.guidesDetailData != null) {//指定司导
            boolean isContain = false;
            ArrayList<GuideCropBean> guideCropList = charterDataUtils.guideCropList;
            final int size = guideCropList.size();
            for (int i = 0; i < size; i++) {
                if (guideCropList.get(i) != null && flightBean.arrCityName.equals(guideCropList.get(i).cityName)) {
                    isContain = true;
                    break;
                }
            }
            if (!isContain) {
                showGuideCheckPickUpDialog(flightBean);
                return false;
            }
        }

        String title = null;
        if (checkCity && checkDate) {//选航班后降落日期，不等于开始日期；且降落城市，不等于开始城市
            title = getString(R.string.daily_second_pickup_error_1, flightBean.arrCityName, flightBean.arrDate);
        } else if (checkCity) {//选航班后降落城市，不等于开始城市
            title = getString(R.string.daily_second_pickup_error_2, flightBean.arrCityName);
        } else if (checkDate) {//选航班后降落日期，不等于开始日期
            title = getString(R.string.daily_second_pickup_error_3, flightBean.arrDate);
        }
        if (!TextUtils.isEmpty(title)) {
            showCheckPickUpDialog(flightBean, title, checkCity, checkDate);
            return false;
        } else {
            return true;
        }
    }

    public void showCheckPickUpDialog(final FlightBean _flightBean, final String title, final boolean checkCity, final boolean checkDate) {
        AlertDialogUtils.showAlertDialogCancelable(this, title, getString(R.string.daily_second_pickup_cancel), getString(R.string.ok2), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(CharterSecondStepActivity.this, ChooseAirActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("flightBean",charterDataUtils.flightBean);
                intent.putExtra("flightBean",bundle);
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

    public void showGuideCheckPickUpDialog(final FlightBean _flightBean) {
        AlertDialogUtils.showAlertDialogCancelable(this, getString(R.string.daily_second_guide_cannot_service, flightBean.arrCityName), getString(R.string.last_step), getString(R.string.daily_second_no_assign), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CharterSecondStepActivity.this.finish();
                dialog.dismiss();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                charterDataUtils.cleanGuidesDate();
                requestCarMaxCapaCity(_flightBean.arrCityId);
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
        CharterModelBehavior selectedCharterModel = fragmentAgent.getSelectedModel();
        if (selectedCharterModel == null) {
            return false;
        }
        boolean result = charterDataUtils.checkInfo(selectedCharterModel.getRouteType(), charterDataUtils.currentDay, true);
        if (!result && selectedCharterModel instanceof EpoxyModel) {
            fragmentAgent.smoothScrollToModel((EpoxyModel) selectedCharterModel);
        }
        return result;
    }

    public void updateTitleBar() {
        ChooseDateBean chooseDateBean = charterDataUtils.chooseDateBean;
        String title = String.format("%1$s-%2$s(%3$s天)", chooseDateBean.showStartDateStr, chooseDateBean.showEndDateStr, chooseDateBean.dayNums);
        titleBar.updateSubtitle(title);
    }

    public static class RefreshBean implements Serializable {
        public int day;
        public boolean isRefresh;//是否强制刷新

        public RefreshBean(int day) {
            this.day = day;
            this.isRefresh = false;
        }

        public RefreshBean(int day, boolean isRefresh) {
            this.day = day;
            this.isRefresh = isRefresh;
        }
    }

    public void updateSeckillsLayout() {
        if (charterDataUtils.isSeckills()) {
            seckillsLayout.setVisibility(View.VISIBLE);
        } else {
            seckillsLayout.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.charter_second_unfold_map_layout})
    public void onUnfoldMapListener() {//展开地图
        onUnfoldMap(true);
    }

    @OnClick({R.id.charter_second_packup_map_layout})
    public void onPackupMapListener() {//收起地图
        onUnfoldMap(false);
    }

    private void onUnfoldMap(boolean isUnfold) {
        SensorsUtils.onAppClick(getEventSource(), "展开地图", getIntentSource());
        this.isUnfoldMap = isUnfold;
        final int mapTopMargin = getMapTopMargin();
        if (isUnfold) {
            unfoldMapLayout.setVisibility(View.GONE);
            bottomView.setVisibility(View.GONE);
            ObjectAnimator anim = ObjectAnimator.ofFloat(topLayout, "translationY", -mapTopMargin);
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    topLayout.setY(0);
                    topLayout.setVisibility(View.GONE);
                    packupMapLayout.setVisibility(View.VISIBLE);

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mapView.getLayoutParams();
                    params.topMargin = 0;
                    mapView.getaMap().moveCamera(CameraUpdateFactory.zoomIn());
                    updateDrawFences();
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float h = (float)valueAnimator.getAnimatedValue();
                    float topMargin = mapTopMargin + h;
                    if (topMargin >= 0) {
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mapView.getLayoutParams();
                        params.topMargin = (int)topMargin;
                        mapView.requestLayout();
                    }
                }
            });
            anim.setDuration(300);
            anim.start();

            ValueAnimator va = ValueAnimator.ofInt(getMapHight(false), getMapHight(true));
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int h = (Integer)valueAnimator.getAnimatedValue();
                    mapView.getLayoutParams().height = h;
                    mapView.requestLayout();
                }
            });
            va.setDuration(500);
            va.start();
        } else {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mapView.getLayoutParams();
            params.height = getMapHight(isUnfold);
            params.topMargin = mapTopMargin;
            bottomView.setVisibility(View.VISIBLE);
            packupMapLayout.setVisibility(View.GONE);
            unfoldMapLayout.setVisibility(View.VISIBLE);
            topLayout.setVisibility(View.VISIBLE);
            updateDrawFences();
            mapView.getaMap().moveCamera(CameraUpdateFactory.zoomOut());
        }
    }

    public int getMapHight(boolean isUnfold) {
        return isUnfold ? UIUtils.getScreenHeight() : (int)((1 / 2.6f) * UIUtils.getScreenWidth());
    }

    public int getMapTopMargin() {
        int actionBarSize = UIUtils.getActionBarSize();
        return charterDataUtils.isSeckills() ? (actionBarSize + UIUtils.dip2px(50)) : actionBarSize;
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
        CharterModelBehavior charterModelBehavior = fragmentAgent.getSelectedModel();
        if (charterModelBehavior != null) {
            drawFences(charterModelBehavior.getCityRouteScope());
        }
    }

    //XXX 待优化
    public void drawFences(CityRouteBean.CityRouteScope cityRouteScope) {
        mapView.getaMap().clear();
        locationMapToCity(charterDataUtils.getCurrentDayStartCityBean());
        final int routeType = cityRouteScope.routeType;
        final boolean isOpeanFence = cityRouteScope.isOpeanFence();
        if (routeType == CityRouteBean.RouteType.AT_WILL) {//随便转转
            //关闭围栏
            mapView.getaMap().clear();
            return;
        }

        HbcLantLng startCoordinate = null;
        if (charterDataUtils.isFirstDay() && charterDataUtils.isSelectedPickUp && charterDataUtils.flightBean != null) {//接机点
            if (routeType == CityRouteBean.RouteType.PICKUP && charterDataUtils.pickUpPoiBean != null) {//点到点
                //TODO 点到点 charterDataUtils.flightBean.arrLocation   charterDataUtils.pickUpPoiBean.location  酒店charterDataUtils.pickUpPoiBean.placeName
                //mapView.getaMap().clear();
                DirectionBean directionBean = charterDataUtils.pickUpDirectionBean;
                if (directionBean != null && directionBean.isHaveLines()) {//是否画点到点间的线
                    ArrayList<HbcLantLng> hbcLantLngList = charterDataUtils.getHbcLantLngList(charterDataUtils.flightBean.arrCityId, directionBean.steps);
                    //TODO 线
                    if(hbcLantLngList!=null && hbcLantLngList.size()>0){
                        mapView.addPoyline(hbcLantLngList,6, Color.argb(150,246,50,7),false);
                    }
                }
                HbcLantLng shbcLantLng = CharterDataUtils.getHbcLantLng(charterDataUtils.flightBean.arrCityId, charterDataUtils.flightBean.arrLocation);
                HbcLantLng ehbcLantLng = CharterDataUtils.getHbcLantLng(charterDataUtils.flightBean.arrCityId, charterDataUtils.pickUpPoiBean.location);
                if(shbcLantLng!=null && ehbcLantLng!=null){
                    List<HbcLantLng> hbcLantLngList = new ArrayList<>();
                    hbcLantLngList.add(shbcLantLng);
                    hbcLantLngList.add(ehbcLantLng);
                    LatLngBounds latLngBounds =  HbcMapViewTools.getMapLatLngBounds(hbcLantLngList);
                    mapView.getaMap().moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds,0));
                    mapView.addMarker(getIconView(R.mipmap.map_icon_plane,R.drawable.map_read_point,""),shbcLantLng);
                    mapView.addMarker(getIconView(R.drawable.map_popbg,R.drawable.map_read_point,charterDataUtils.pickUpPoiBean.placeName),ehbcLantLng);
                }
                return;
            } else {//点
                startCoordinate = charterDataUtils.getHbcLantLng(charterDataUtils.flightBean.arrCityId,charterDataUtils.flightBean.arrLocation);
                //TODO  点 startCoordinate;
                if(startCoordinate!=null){
                    Marker startMarker = mapView.addMarker(getIconView(R.mipmap.map_icon_plane,R.drawable.map_read_point,""),startCoordinate);
                    mapView.getaMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(startCoordinate.latitude,startCoordinate.longitude),16));
                }
            }
        } else if ((charterDataUtils.isLastDay() && charterDataUtils.isSelectedSend && charterDataUtils.airPortBean != null)) {//送机点
            if(routeType == CityRouteBean.RouteType.SEND && charterDataUtils.sendPoiBean != null) {//点到点
                //TODO  点到点 charterDataUtils.airPortBean.location    charterDataUtils.sendPoiBean.location  charterDataUtils.sendPoiBean.placeName
                DirectionBean directionBean = charterDataUtils.sendDirectionBean;
                if (directionBean.isHaveLines()) {
                    ArrayList<HbcLantLng> hbcLantLngList = charterDataUtils.getHbcLantLngList(charterDataUtils.getStartCityBean(charterDataUtils.currentDay).cityId,directionBean.steps);
                    //TODO  线
                    if(hbcLantLngList!=null && hbcLantLngList.size()>0){
                        mapView.addPoyline(hbcLantLngList,6, Color.argb(150,246,50,7),false);
                    }
                }
                HbcLantLng shbcLantLng = CharterDataUtils.getHbcLantLng(charterDataUtils.getStartCityBean(charterDataUtils.currentDay).cityId, charterDataUtils.sendPoiBean.location);
                HbcLantLng ehbcLantLng = CharterDataUtils.getHbcLantLng(charterDataUtils.airPortBean.cityId, charterDataUtils.airPortBean.location);
                if(shbcLantLng!=null && ehbcLantLng!=null){
                    List<HbcLantLng> hbcLantLngList = new ArrayList<>();
                    hbcLantLngList.add(shbcLantLng);
                    hbcLantLngList.add(ehbcLantLng);
                    LatLngBounds latLngBounds =  HbcMapViewTools.getMapLatLngBounds(hbcLantLngList);
                    mapView.addMarker(getIconView(R.drawable.map_popbg,R.drawable.map_read_point,charterDataUtils.sendPoiBean.placeName),shbcLantLng);
                    mapView.addMarker(getIconView(R.mipmap.map_icon_plane,R.drawable.map_read_point,""),ehbcLantLng);
                    mapView.getaMap().moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds,0));
                }
                return;
            }else {//点加围栏
                startCoordinate = charterDataUtils.getHbcLantLng(charterDataUtils.airPortBean.airportId, charterDataUtils.airPortBean.location);
                //TODO  点
                if(startCoordinate!=null){
                    mapView.addMarker(getIconView(R.mipmap.map_icon_plane,R.drawable.map_read_point,""),startCoordinate);
                    mapView.getaMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(startCoordinate.latitude,startCoordinate.longitude),16));
                }
            }
        }

        if (!isOpeanFence) {//未开启围栏
            CityBean cityBean = charterDataUtils.getCurrentDayStartCityBean();
            //TODO cityBean.name 点加城市名 需要加经纬度
            String location = cityBean.location;
            if(!TextUtils.isEmpty(location)){
                HbcLantLng hbcLantLng = CharterDataUtils.getHbcLantLng(cityBean.cityId,location);
                if (hbcLantLng != null) {
                    mapView.addMarker(getIconView(R.drawable.map_pop_city,R.drawable.map_green_point,cityBean.name),hbcLantLng);
                    mapView.getaMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(hbcLantLng.latitude,hbcLantLng.longitude),12));

                    if (startCoordinate != null) {
                        List<HbcLantLng> hbcLantLngs = new ArrayList<>();
                        hbcLantLngs.add(startCoordinate);
                        hbcLantLngs.add(hbcLantLng);
                        mapView.getaMap().moveCamera(CameraUpdateFactory.newLatLngBounds(HbcMapViewTools.getMapLatLngBounds(hbcLantLngs),0));
                    }
                }
            }
        }

        if (routeType == CityRouteBean.RouteType.OUTTOWN) {//跨城市 画两个围栏
            ArrayList<CityRouteBean.Fence> fences = charterDataUtils.getCurrentDayFences();
            ArrayList<CityRouteBean.Fence> nextFences = charterDataUtils.getNextDayFences();

            if (fences != null && fences.size() >= 1 && nextFences != null && nextFences.size() >= 1) {
                CityBean currentCityBean = charterDataUtils.getCurrentDayStartCityBean();
                CityBean nextCityBean = charterDataUtils.getEndCityBean();
                ArrayList<HbcLantLng> urbanList = charterDataUtils.getHbcLantLngList(currentCityBean.cityId, fences.get(0));//市内围栏
                if(urbanList!=null && urbanList.size()>0){
                    mapView.addPolygon(urbanList,Color.argb(150,125,211,32),8,Color.argb(90,125,211,32));
                    if(!TextUtils.isEmpty(currentCityBean.name)){
                        mapView.addText(currentCityBean.name,UIUtils.dip2px(30),Color.argb(125,30,55,1),urbanList);
                        mapView.getaMap().moveCamera(CameraUpdateFactory.newLatLngBounds(HbcMapViewTools.getMapLatLngBounds(urbanList),0));
                    }
                }
                if(nextFences != null && nextFences.get(0) != null){
                    CityRouteBean.Fence nextFence = nextFences.get(0);
                    ArrayList<HbcLantLng> nextHbcLantLngList = charterDataUtils.getHbcLantLngList(nextCityBean.cityId, nextFence);
                    if (nextHbcLantLngList!=null && nextHbcLantLngList.size()>0) {
                        mapView.addPolygon(nextHbcLantLngList,Color.argb(150,125,211,32),8,Color.argb(90,125,211,32));
                        if(nextCityBean != null && !TextUtils.isEmpty(nextCityBean.name)){
                            mapView.addText(nextCityBean.name,UIUtils.dip2px(30),Color.argb(125,30,55,1),nextHbcLantLngList);
                        }
                        mapView.getaMap().moveCamera(CameraUpdateFactory.newLatLngBounds(HbcMapViewTools.getMapLatLngBounds(urbanList,nextHbcLantLngList),0));
                    }
                }

            } else if (fences != null && fences.size() >= 1) {
                CityBean currentCityBean = charterDataUtils.getCurrentDayStartCityBean();
                ArrayList<HbcLantLng> urbanList = charterDataUtils.getHbcLantLngList(currentCityBean.cityId,fences.get(0));//市内围栏
                if(urbanList!=null && urbanList.size()>0){
                    mapView.addPolygon(urbanList,Color.argb(150,125,211,32),8,Color.argb(90,125,211,32));
                    if(!TextUtils.isEmpty(currentCityBean.name)){
                        mapView.addText(currentCityBean.name,UIUtils.dip2px(30),Color.argb(125,30,55,1),urbanList);
                        mapView.getaMap().moveCamera(CameraUpdateFactory.newLatLngBounds(HbcMapViewTools.getMapLatLngBounds(urbanList),0));
                    }
                }
                CityBean cityBean = charterDataUtils.getEndCityBean(charterDataUtils.currentDay);
                if (cityBean == null) {
                    return;
                }
                //TODO cityBean.name 点加城市名 需要加经纬度
                String location = cityBean.location;
                if(!TextUtils.isEmpty(location)){
                    HbcLantLng hbcLantLng = CharterDataUtils.getHbcLantLng(cityBean.cityId,location);
                    if (hbcLantLng != null) {
                        mapView.addMarker(getIconView(R.drawable.map_pop_city,R.drawable.map_green_point,cityBean.name),hbcLantLng);
                        mapView.getaMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(hbcLantLng.latitude,hbcLantLng.longitude),12));
                        ArrayList<HbcLantLng> hbcLantLngList = new ArrayList<>();
                        hbcLantLngList.add(hbcLantLng);
                        mapView.getaMap().moveCamera(CameraUpdateFactory.newLatLngBounds(HbcMapViewTools.getMapLatLngBounds(urbanList, hbcLantLngList),0));
                    }
                }

            } else if (nextFences != null && nextFences.size() >= 1) {
                CityBean nextCityBean = charterDataUtils.getEndCityBean();
                if(nextFences != null && nextFences.get(0) != null){
                    CityRouteBean.Fence nextFence = nextFences.get(0);
                    ArrayList<HbcLantLng> nextHbcLantLngList = charterDataUtils.getHbcLantLngList(nextCityBean.cityId,nextFence);
                    if (nextHbcLantLngList!=null && nextHbcLantLngList.size()>0) {
                        mapView.addPolygon(nextHbcLantLngList,Color.argb(150,125,211,32),8,Color.argb(90,125,211,32));
                        if(nextCityBean != null && !TextUtils.isEmpty(nextCityBean.name)){
                            mapView.addText(nextCityBean.name,UIUtils.dip2px(30),Color.argb(125,30,55,1),nextHbcLantLngList);
                            mapView.getaMap().moveCamera(CameraUpdateFactory.newLatLngBounds(HbcMapViewTools.getMapLatLngBounds(nextHbcLantLngList),0));
                        }
                        CityBean cityBean = charterDataUtils.getCurrentDayStartCityBean();
                        String location = cityBean.location;
                        if(!TextUtils.isEmpty(location)){
                            HbcLantLng hbcLantLng = CharterDataUtils.getHbcLantLng(cityBean.cityId,location);
                            if (hbcLantLng != null) {
                                ArrayList<HbcLantLng> hbcLantLngList = new ArrayList<>();
                                hbcLantLngList.add(hbcLantLng);
                                mapView.getaMap().moveCamera(CameraUpdateFactory.newLatLngBounds(HbcMapViewTools.getMapLatLngBounds(hbcLantLngList,nextHbcLantLngList),0));
                            }
                        }
                    }
                }
            } else {
                CityBean endCityBean = charterDataUtils.getEndCityBean(charterDataUtils.currentDay);
                if (endCityBean == null) {
                    return;
                }
                if(!TextUtils.isEmpty(endCityBean.location)){
                    HbcLantLng hbcLantLng = CharterDataUtils.getHbcLantLng(endCityBean.cityId,endCityBean.location);
                    if (hbcLantLng != null) {
                        mapView.addMarker(getIconView(R.drawable.map_pop_city,R.drawable.map_green_point,endCityBean.name),hbcLantLng);
                        mapView.getaMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(hbcLantLng.latitude,hbcLantLng.longitude),12));
                        ArrayList<HbcLantLng> hbcLantLngList = new ArrayList<>();
                        hbcLantLngList.add(hbcLantLng);

                        CityBean cityBean = charterDataUtils.getCurrentDayStartCityBean();
                        String location = cityBean.location;
                        if(!TextUtils.isEmpty(location)){
                            HbcLantLng startHbcLantLng = CharterDataUtils.getHbcLantLng(cityBean.cityId,location);
                            hbcLantLngList.add(startHbcLantLng);
                        }
                        mapView.getaMap().moveCamera(CameraUpdateFactory.newLatLngBounds(HbcMapViewTools.getMapLatLngBounds(hbcLantLngList),0));
                    }
                }
            }

        } else if (isOpeanFence) {
            ArrayList<CityRouteBean.Fence> fences = charterDataUtils.getCurrentDayFences();
            if (fences == null || fences.size() < 1) {
                return;
            }
            CityBean currentCityBean = charterDataUtils.getCurrentDayStartCityBean();
            ArrayList<HbcLantLng> urbanList = charterDataUtils.getHbcLantLngList(currentCityBean.cityId,fences.get(0));//市内围栏
            ArrayList<HbcLantLng> outsideList = fences.size() > 1 ? charterDataUtils.getHbcLantLngList(currentCityBean.cityId,fences.get(1)) : null;//周边围栏
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

    private void locationMapToCity(CityBean cityBean){
        if(cityBean==null){
            return;
        }
        HbcLantLng hbcLantLng =  CharterDataUtils.getHbcLantLng(cityBean.cityId, cityBean.location);
        mapView.getaMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(hbcLantLng.latitude,hbcLantLng.longitude),16));
    }

    @Override
    public void finish() {
        super.finish();
        //离开接送机界面,将选航班号的类型初始化按选航班号类型=1
        SharedPre sharedPre = new SharedPre(CharterSecondStepActivity.this);
        sharedPre.saveIntValue("chooseAirType",1);
    }
}
