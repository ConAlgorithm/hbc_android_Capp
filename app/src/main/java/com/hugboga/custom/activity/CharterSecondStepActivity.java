package com.hugboga.custom.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyModel;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapsInitializer;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.Polygon;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.amap.entity.HbcLantLng;
import com.hugboga.amap.view.HbcMapView;
import com.hugboga.amap.view.HbcMapViewTools;
import com.hugboga.custom.MyApplication;
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
public class CharterSecondStepActivity extends BaseActivity implements CharterSecondBottomView.OnBottomClickListener, CityRouteAdapter.OnCharterItemClickListener{

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

    LayoutInflater mLayoutInflater;

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

        adapter = new CityRouteAdapter();
        adapter.setOnCharterItemClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        bottomView.setOnBottomClickListener(this);

        CityBean cityBean = charterDataUtils.getStartCityBean();
        requestCityRoute("" + cityBean.cityId, 1);
        bottomView.updateConfirmView();
    }

    public void requestCityRoute(String cityId, int type) {
        requestData(new RequestCityRoute(this, cityId, type));
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
            RequestCityRoute request= (RequestCityRoute) _request;
            CityRouteBean _cityRouteBean = request.getData();
            if (_cityRouteBean == null) {
                return;
            }
            if (request.getType() == 2) {//跨城市
                charterDataUtils.addFences(charterDataUtils.currentDay, _cityRouteBean.fences, false);
            } else {//更新
                int routeType = charterDataUtils.getRouteType(charterDataUtils.currentDay - 1);
                charterDataUtils.addFences(charterDataUtils.currentDay, _cityRouteBean.fences, true);
                currentDay = charterDataUtils.currentDay;
                adapter.notifyAllModelsChanged(_cityRouteBean, routeType);
                bottomView.updateConfirmView();
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
                charterDataUtils.isSelectedPickUp = true;
                adapter.showPickupModel();
                adapter.updateSubtitleModel();
                break;
            case CHOOSE_POI_BACK:
                PoiBean poiBean = (PoiBean) action.getData();
                if (poiBean.mBusinessType == Constants.BUSINESS_TYPE_PICK) {
                    if (charterDataUtils.pickUpPoiBean == poiBean) {
                        break;
                    }
                    charterDataUtils.pickUpPoiBean = poiBean;
                    CityBean startCityBean = charterDataUtils.getStartCityBean();
                    requestDirection(charterDataUtils.flightBean.arrLocation, charterDataUtils.pickUpPoiBean.location, startCityBean.placeId);
                } else if(poiBean.mBusinessType == Constants.BUSINESS_TYPE_SEND) {
                    if (charterDataUtils.sendPoiBean == poiBean) {
                        break;
                    }
                    charterDataUtils.sendPoiBean = poiBean;
                    adapter.showSendModel();
//                    CityBean startCityBean = charterDataUtils.getCurrentDayCityBean();
//                    requestDirection(charterDataUtils.sendPoiBean.location, charterDataUtils.airPortBean.location, startCityBean.placeId);
                }
                break;
            case CHOOSE_END_CITY_BACK:
                CityBean cityBean = (CityBean) action.getData();
                CityBean oldCityBean = charterDataUtils.getEndCityBean();
                if (cityBean == null || oldCityBean == cityBean) {
                    return;
                }
                charterDataUtils.addEndCityBean(cityBean);
                adapter.updateSelectedModel();
                requestCityRoute("" + cityBean.cityId, 2);//跨城市
                break;
            case AIR_PORT_BACK:
                AirPort airPortBean = (AirPort) action.getData();
                if (charterDataUtils.airPortBean != null && charterDataUtils.airPortBean != airPortBean) {
                    charterDataUtils.sendPoiBean = null;
                    charterDataUtils.sendServerTime = null;
                }
                charterDataUtils.airPortBean = airPortBean;
                charterDataUtils.isSelectedSend = true;
                adapter.insertSendModel();
                adapter.updateSubtitleModel();
                break;
            case CHARTER_LIST_REFRESH:
                int selectedDay = (int) action.getData();
                if (selectedDay == currentDay) {
                    return;
                }
                charterDataUtils.currentDay = selectedDay;
                CityBean currentCityBean = charterDataUtils.getStartCityBean(selectedDay);
                if (currentCityBean == null) {
                   currentCityBean = charterDataUtils.setDefaultCityBean();
                }
                requestCityRoute("" + currentCityBean.cityId, 1);
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
            CityBean nextCityBean = charterDataUtils.setDefaultCityBean();
            bottomView.updateConfirmView();
//            adapter.notifyAllModelsChanged(cityRouteBean, getRouteType(charterDataUtils.currentDay));
            requestCityRoute("" + nextCityBean.cityId, 1);
            currentDay = charterDataUtils.currentDay;
        }
    }

    @Override
    public void intentTravelList() {
        Intent intent = new Intent(this, TravelListActivity.class);
        intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
        startActivity(intent);
    }

    public boolean checkPickUpFlightBean(FlightBean flightBean) {
//        final boolean checkCity = charterDataUtils.params.startBean.cityId != flightBean.arrCityId;
//        final boolean checkDate = charterDataUtils.params.chooseDateBean.start_date != flightBean.arrDate;
//        if (checkCity && checkDate) {//选航班后降落日期，不等于开始日期；且降落城市，不等于开始城市
//            return false;
//        } else if (checkCity) {//选航班后降落城市，不等于开始城市
//            return false;
//        } else if (checkDate) {//选航班后降落日期，不等于开始日期
//            return false;
//        } else {
            return true;
//        }
    }

    @Override
    public void onCharterItemClick(CityRouteBean.CityRouteScope cityRouteScope) {
        if (cityRouteScope.routeType == CityRouteBean.RouteType.OUTTOWN) {

        } else {

        }
        charterDataUtils.addCityRouteScope(cityRouteScope);
        drawFences(cityRouteScope.routeType, cityRouteScope.isOpeanFence());
    }

    public void drawFences(int routeType, boolean isOpeanFence) {
        if (routeType == CityRouteBean.RouteType.AT_WILL) {//随便转转
            //TODO 关闭围栏
            mapView.getaMap().clear();
            return;
        }
        mapView.getaMap().clear();
        boolean isDrawFences = isOpeanFence;;
        HbcLantLng startCoordinate = null;
        if (charterDataUtils.isFirstDay() && charterDataUtils.isSelectedPickUp && charterDataUtils.flightBean != null) {//接机点
            if (charterDataUtils.pickUpPoiBean == null) {//点
                startCoordinate = charterDataUtils.getHbcLantLng(charterDataUtils.flightBean.arrLocation);
                //TODO  点 startCoordinate;
                if(startCoordinate!=null){
                    mapView.addMarker(getIconView(R.mipmap.map_icon_plane,R.drawable.map_read_point,""),startCoordinate);
                    mapView.getaMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(startCoordinate.latitude,startCoordinate.longitude),16));
                }
            } else {//点到点
                isDrawFences = false;
                //TODO 点到点 charterDataUtils.flightBean.arrLocation   charterDataUtils.pickUpPoiBean.location  酒店charterDataUtils.pickUpPoiBean.placeName
                //mapView.getaMap().clear();
                DirectionBean directionBean = charterDataUtils.pickUpDirectionBean;
                if (directionBean.isHaveLines()) {//是否画点到点间的线
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
            if (charterDataUtils.sendPoiBean == null) {//点加围栏
                startCoordinate = charterDataUtils.getHbcLantLng(charterDataUtils.airPortBean.location);
                //TODO  点
                if(startCoordinate!=null){
                    mapView.addMarker(getIconView(R.mipmap.map_icon_plane,R.drawable.map_read_point,""),startCoordinate);
                    mapView.getaMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(startCoordinate.latitude,startCoordinate.longitude),16));
                }
            } else {//点到点
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
            CityBean cityBean = charterDataUtils.getStartCityBean();
            //TODO cityBean.name 点加城市名 需要加经纬度
            isDrawFences = false;
            String location = cityBean.location;
            if(!TextUtils.isEmpty(location)){
                HbcLantLng hbcLantLng = CharterDataUtils.getHbcLantLng(location);
                mapView.addMarker(getIconView(R.drawable.map_pop_city,R.drawable.map_green_point,cityBean.name),hbcLantLng);
                mapView.getaMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(startCoordinate.latitude,hbcLantLng.longitude),16));
            }
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
            CityBean currentCityBean = charterDataUtils.getStartCityBean();
            CityBean nextCityBean = charterDataUtils.getEndCityBean();
            // TODO 跨城市 画两个围栏 hbcLantLngList nextHbcLantLngList currentCityBean.name  nextCityBean.name
            if(urbanList!=null && urbanList.size()>0){
                mapView.addPolygon(urbanList,Color.argb(150,125,211,32),8,Color.argb(90,125,211,32));
                if(!TextUtils.isEmpty(currentCityBean.name)){
                    mapView.addText(currentCityBean.name,100,Color.argb(125,30,55,1),urbanList);
                }
            }
            if(nextHbcLantLngList!=null && nextHbcLantLngList.size()>0){
                mapView.addPolygon(nextHbcLantLngList,Color.argb(150,125,211,32),8,Color.argb(90,125,211,32));
                if(!TextUtils.isEmpty(nextCityBean.name)){
                    mapView.addText(nextCityBean.name,100,Color.argb(125,30,55,1),nextHbcLantLngList);
                }
            }
            mapView.getaMap().moveCamera(CameraUpdateFactory.newLatLngBounds(HbcMapViewTools.getMapLatLngBounds(urbanList,outsideList),0));
        } else {
            if (startCoordinate != null) {
                // TODO 围栏 startCoordinate hbcLantLngList nextHbcLantLngList 判断坐标点在 市内（一个圈）、周边（俩圈）、超出周边（俩圈）
                if(urbanList!=null && urbanList.size()>0){
                    Polygon polygon = mapView.addPolygon(urbanList,Color.argb(150,125,211,32),8,Color.argb(90,125,211,32));
                    if(!polygon.contains(new LatLng(startCoordinate.latitude,startCoordinate.longitude))){
                        CityRouteBean.Fence nextFence = nextFences.get(0);
                        if(nextFence != null ){
                            ArrayList<HbcLantLng> nextHbcLantLngList = CharterDataUtils.getHbcLantLngList(nextFence);
                            if(nextHbcLantLngList!=null && nextHbcLantLngList.size()>0){
                                mapView.addPolygon(nextHbcLantLngList,Color.argb(150,125,211,32),8,Color.argb(90,125,211,32));
                                mapView.getaMap().moveCamera(CameraUpdateFactory.newLatLngBounds(HbcMapViewTools.getMapLatLngBounds(nextHbcLantLngList,urbanList),0));
                            }else{
                                mapView.getaMap().moveCamera(CameraUpdateFactory.newLatLngBounds(HbcMapViewTools.getMapLatLngBounds(urbanList),0));
                            }
                        }else{
                            mapView.getaMap().moveCamera(CameraUpdateFactory.newLatLngBounds(HbcMapViewTools.getMapLatLngBounds(urbanList),0));
                        }
                    }else{
                        mapView.getaMap().moveCamera(CameraUpdateFactory.newLatLngBounds(HbcMapViewTools.getMapLatLngBounds(urbanList),0));
                    }
                }

            } else if (routeType == CityRouteBean.RouteType.SUBURBAN) {
                // TODO outsideList 周边围栏
                if(outsideList!=null && outsideList.size()>0){
                    mapView.addPolygon(outsideList,Color.argb(150,125,211,32),8,Color.argb(90,125,211,32));
                    mapView.getaMap().moveCamera(CameraUpdateFactory.newLatLngBounds(HbcMapViewTools.getMapLatLngBounds(outsideList),0));
                }
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
}
