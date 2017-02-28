package com.hugboga.custom.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.airbnb.epoxy.EpoxyModel;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.CityRouteAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.AirPort;
import com.hugboga.custom.data.bean.ChooseDateBean;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.CityRouteBean;
import com.hugboga.custom.data.bean.FlightBean;
import com.hugboga.custom.data.bean.PoiBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.request.RequestCityRoute;
import com.hugboga.custom.models.CharterItemModel;
import com.hugboga.custom.models.CharterPickupModel;
import com.hugboga.custom.models.CharterSendModel;
import com.hugboga.custom.utils.CharterDataUtils;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.SpaceItemDecoration;
import com.hugboga.custom.widget.charter.CharterSecondBottomView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/2/21.
 */
public class CharterSecondStepActivity extends BaseActivity implements CharterSecondBottomView.OnBottomClickListener {

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

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        super.onDataRequestSucceed(_request);
        if (_request instanceof RequestCityRoute) {
            CityRouteBean _cityRouteBean = ((RequestCityRoute) _request).getData();
            if (_cityRouteBean == null) {
                return;
            }
            if (cityRouteBean != null && charterDataUtils.currentDay > 1) {
                adapter.notifyAllModelsChanged(_cityRouteBean, CityRouteBean.RouteType.URBAN);
            } else {
                adapter.setCityRouteBean(_cityRouteBean);
            }
            this.cityRouteBean = _cityRouteBean;
        }
    }

    public void initView() {
        charterDataUtils = CharterDataUtils.getInstance();
        charterDataUtils.init(params);

        adapter = new CityRouteAdapter();
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
                    charterDataUtils.pickUpPoiBean = poiBean;
                    adapter.updatePickupModel();
                } else if(poiBean.mBusinessType == Constants.BUSINESS_TYPE_SEND) {
                    charterDataUtils.sendPoiBean = poiBean;
                    adapter.updateSendModel();
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
            CityBean currentDayCityBean = charterDataUtils.getCurrentDayCityBean();
            CityBean nextStartCityBean = charterDataUtils.getNextDayCityBean();

            EpoxyModel lastSelectedModel = adapter.lastSelectedModel;
            CityRouteBean.CityRouteScope cityRouteScope = null;
            if (lastSelectedModel instanceof CharterPickupModel) {//只接机不包车
                cityRouteScope =  new CityRouteBean.CityRouteScope();
                cityRouteScope.routeType = CityRouteBean.RouteType.PICKUP;
            } else if (lastSelectedModel instanceof CharterSendModel) {//只送机机不包车
                cityRouteScope =  new CityRouteBean.CityRouteScope();
                cityRouteScope.routeType = CityRouteBean.RouteType.SEND;
            } else if (lastSelectedModel instanceof CharterItemModel) {//包车、随便转转
                cityRouteScope = ((CharterItemModel) lastSelectedModel).getCityRouteScope();
            }
            charterDataUtils.addCityRouteScope(cityRouteScope);//currentDay++
            bottomView.updateConfirmView();

            if (nextStartCityBean != null) {
                requestCityRoute("" + nextStartCityBean.cityId);
            } else if (currentDayCityBean != null && currentDayCityBean != params.startBean) {
                requestCityRoute("" + currentDayCityBean.cityId);
            } else {//开始城市未改变，不请求，直接刷新
                adapter.notifyAllModelsChanged(cityRouteBean, CityRouteBean.RouteType.URBAN);
            }
        }
    }

    @Override
    public void intentTravelList() {

    }
}
