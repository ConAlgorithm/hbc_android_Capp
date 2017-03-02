package com.hugboga.custom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.airbnb.epoxy.EpoxyModel;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.amap.entity.HbcLantLng;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.CityRouteAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.AirPort;
import com.hugboga.custom.data.bean.ChooseDateBean;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.CityRouteBean;
import com.hugboga.custom.data.bean.DirectionBean;
import com.hugboga.custom.data.bean.FlightBean;
import com.hugboga.custom.data.bean.PoiBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.request.RequestCarMaxCapaCity;
import com.hugboga.custom.data.request.RequestCityRoute;
import com.hugboga.custom.data.request.RequestDirection;
import com.hugboga.custom.utils.CharterDataUtils;
import com.hugboga.custom.widget.charter.CharterSecondBottomView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/2/21.
 */
public class CharterSecondStepActivity extends BaseActivity implements CharterSecondBottomView.OnBottomClickListener, CityRouteAdapter.OnCharterItemClickListener{

    @Bind(R.id.charter_second_recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.charter_second_bottom_view)
    CharterSecondBottomView bottomView;

    private CharterSecondStepActivity.Params params;
    private CityRouteAdapter adapter;
    private CharterDataUtils charterDataUtils;
    private CityRouteBean cityRouteBean;

    public static class Params implements Serializable {
        public CityBean startBean;
        public ChooseDateBean chooseDateBean;
        public int adultCount;
        public int childCount;
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
        EventBus.getDefault().register(this);

        requestCityRoute("" + params.startBean.cityId);
        initView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (params != null) {
            outState.putSerializable(Constants.PARAMS_DATA, params);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        charterDataUtils.onDestroy();
    }

    public void requestCityRoute(String cityId) {
        requestData(new RequestCityRoute(this, cityId));
    }

    public void requestDirection(String origin, String destination, String countryId) {
        requestData(new RequestDirection(this, origin, destination, countryId));
    }

    public void requestCarMaxCapaCity(String cityId) {
        requestData(new RequestCarMaxCapaCity(this, cityId));
    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        super.onDataRequestSucceed(_request);
        if (_request instanceof RequestCityRoute) {
            CityRouteBean _cityRouteBean = ((RequestCityRoute) _request).getData();
            if (_cityRouteBean == null) {
                return;
            }
            if (this.cityRouteBean == null) {
                charterDataUtils.fencesMap.put(charterDataUtils.currentDay, _cityRouteBean.fences);
                adapter.setCityRouteBean(_cityRouteBean);
            } else {
                charterDataUtils.fencesMap.put(charterDataUtils.currentDay + 1, _cityRouteBean.fences);
            }
            this.cityRouteBean = _cityRouteBean;
        } else if (_request instanceof RequestDirection) {
            DirectionBean directionBean = ((RequestDirection) _request).getData();
            if (charterDataUtils.isFirstDay()) {//接机
                charterDataUtils.pickUpDirectionBean = directionBean;
                adapter.updatePickupModel();
            } else {//送机
                charterDataUtils.sendDirectionBean = directionBean;
            }
        }
    }

    public void initView() {
        charterDataUtils = CharterDataUtils.getInstance();
        charterDataUtils.init(params);

        adapter = new CityRouteAdapter();
        adapter.setOnCharterItemClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        bottomView.setOnBottomClickListener(this);
    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case AIR_NO:
                FlightBean flightBean = (FlightBean) action.getData();
//                if (!checkPickUpFlightBean(flightBean)) {
//                    break;
//                }
                if (charterDataUtils.flightBean != null && charterDataUtils.flightBean != flightBean) {
                    charterDataUtils.pickUpPoiBean = null;
                }
                charterDataUtils.flightBean = flightBean;
                adapter.insertPickupModel();
                adapter.updateSubtitleModel();
                break;
            case CHOOSE_POI_BACK:
                PoiBean poiBean = (PoiBean) action.getData();
                if (poiBean.mBusinessType == Constants.BUSINESS_TYPE_PICK) {
                    if (charterDataUtils.pickUpPoiBean == poiBean) {
                        break;
                    }
                    charterDataUtils.pickUpPoiBean = poiBean;
                    CityBean startCityBean = charterDataUtils.getCurrentDayCityBean();
                    requestDirection(charterDataUtils.flightBean.arrLocation, charterDataUtils.pickUpPoiBean.location, startCityBean.placeId);
                } else if(poiBean.mBusinessType == Constants.BUSINESS_TYPE_SEND) {
                    if (charterDataUtils.sendPoiBean == poiBean) {
                        break;
                    }
                    charterDataUtils.sendPoiBean = poiBean;
                    adapter.updateSendModel();
//                    CityBean startCityBean = charterDataUtils.getCurrentDayCityBean();
//                    requestDirection(charterDataUtils.sendPoiBean.location, charterDataUtils.airPortBean.location, startCityBean.placeId);
                }
                break;
            case CHOOSE_END_CITY_BACK:
                CityBean cityBean = (CityBean) action.getData();
                CityBean oldCityBean = charterDataUtils.getNextDayCityBean();
                if (cityBean == null || oldCityBean == cityBean) {
                    return;
                }
                charterDataUtils.cityBeanMap.put(charterDataUtils.currentDay + 1, cityBean);
                adapter.updateSelectedModel();
                requestCityRoute("" + cityBean.cityId);
                break;
            case AIR_PORT_BACK:
                AirPort airPortBean = (AirPort) action.getData();
                if (charterDataUtils.airPortBean != null && charterDataUtils.airPortBean != airPortBean) {
                    charterDataUtils.sendPoiBean = null;
                    charterDataUtils.sendServerTime = null;
                }
                charterDataUtils.airPortBean = airPortBean;
                adapter.insertSendModel();
                adapter.updateSubtitleModel();
                break;
        }
    }

    @Override
    public String getEventSource() {
        return getString(R.string.custom_chartered);
    }

    @Override
    public void confirm() {
        if (cityRouteBean == null) {
            return;
        }

        if (charterDataUtils.isLastDay()) {//最后一天"查看报价"

        } else {
            charterDataUtils.currentDay++;
            bottomView.updateConfirmView();
            adapter.notifyAllModelsChanged(cityRouteBean, CityRouteBean.RouteType.URBAN);
        }
    }

    @Override
    public void intentTravelList() {
        Intent intent = new Intent(this, TravelListActivity.class);
        intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
        startActivity(intent);
    }

    public boolean checkPickUpFlightBean(FlightBean flightBean) {
        final boolean checkCity = charterDataUtils.params.startBean.cityId != flightBean.arrCityId;
        final boolean checkDate = charterDataUtils.params.chooseDateBean.start_date != flightBean.arrDate;
        if (checkCity && checkDate) {//选航班后降落日期，不等于开始日期；且降落城市，不等于开始城市
            return false;
        } else if (checkCity) {//选航班后降落城市，不等于开始城市
            return false;
        } else if (checkDate) {//选航班后降落日期，不等于开始日期
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onCharterItemClick(CityRouteBean.CityRouteScope cityRouteScope) {
        charterDataUtils.addCityRouteScope(cityRouteScope);
        drawFences(cityRouteScope.routeType, cityRouteScope.isOpeanFence());
    }

    public void drawFences(int routeType, boolean isOpeanFence) {
        if (routeType == CityRouteBean.RouteType.AT_WILL) {//随便转转
            //TODO 关闭围栏
            return;
        }
        boolean isDrawFences = isOpeanFence;;
        HbcLantLng startCoordinate = null;
        if (charterDataUtils.isFirstDay() && charterDataUtils.isSelectedPickUp && charterDataUtils.flightBean != null) {//接机点
            if (charterDataUtils.pickUpPoiBean == null) {//点加围栏
                startCoordinate = charterDataUtils.getHbcLantLng(charterDataUtils.flightBean.arrLocation);
                //TODO  点 startCoordinate;
            } else {//点到点
                isDrawFences = false;
                //TODO 点到点 charterDataUtils.flightBean.arrLocation   charterDataUtils.pickUpPoiBean.location  酒店charterDataUtils.pickUpPoiBean.placeName
                DirectionBean directionBean = charterDataUtils.pickUpDirectionBean;
                if (directionBean.isHaveLines()) {//是否画点到点间的线
                    ArrayList<HbcLantLng> hbcLantLngList = charterDataUtils.getHbcLantLngList(directionBean.steps);
                    //TODO 线
                }
            }
        } else if ((charterDataUtils.isLastDay() && charterDataUtils.isSelectedSend && charterDataUtils.airPortBean != null)) {//送机点
            if (charterDataUtils.sendPoiBean == null) {//点加围栏

                startCoordinate = charterDataUtils.getHbcLantLng(charterDataUtils.airPortBean.location);
                //TODO  点
            } else {//点到点
                isDrawFences = false;
                //TODO  点到点 charterDataUtils.airPortBean.location    charterDataUtils.sendPoiBean.location  charterDataUtils.sendPoiBean.placeName
                DirectionBean directionBean = charterDataUtils.sendDirectionBean;
                if (directionBean.isHaveLines()) {
                    ArrayList<HbcLantLng> hbcLantLngList = charterDataUtils.getHbcLantLngList(directionBean.steps);
                    //TODO  线
                }
            }
        } else if (!isOpeanFence) {//未开启围栏
            CityBean cityBean = charterDataUtils.getCurrentDayCityBean();
            //TODO cityBean.name 点加城市名
            isDrawFences = false;
        }

        if (!isDrawFences) {
            return;
        }
        ArrayList<CityRouteBean.Fence> fences = charterDataUtils.getCurrentDayFences();
        if (fences == null) {
            return;
        }
        ArrayList<HbcLantLng> urbanList = charterDataUtils.getHbcLantLngList(fences.get(0));//市内围栏
        ArrayList<HbcLantLng> outsideList = charterDataUtils.getHbcLantLngList(fences.get(1));//周边围栏
        ArrayList<CityRouteBean.Fence> nextFences = charterDataUtils.getCurrentDayFences();
        if (routeType == CityRouteBean.RouteType.OUTTOWN && nextFences != null && nextFences.get(0) != null) {//跨城市 画两个围栏
            CityRouteBean.Fence nextFence = nextFences.get(0);
            ArrayList<HbcLantLng> nextHbcLantLngList = charterDataUtils.getHbcLantLngList(nextFence);
            CityBean currentCityBean = charterDataUtils.getCurrentDayCityBean();
            CityBean nextCityBean = charterDataUtils.getNextDayCityBean();
            // TODO 跨城市 画两个围栏 hbcLantLngList nextHbcLantLngList currentCityBean.name  nextCityBean.name
        } else {
            if (startCoordinate != null) {
                // TODO 围栏 startCoordinate hbcLantLngList nextHbcLantLngList 判断坐标点在 市内（一个圈）、周边（俩圈）、超出周边（俩圈）
            } else if (routeType == CityRouteBean.RouteType.SUBURBAN) {
                // TODO outsideList 周边围栏
            } else {
                // TODO urbanList 市内围栏
            }
        }
    }

}
