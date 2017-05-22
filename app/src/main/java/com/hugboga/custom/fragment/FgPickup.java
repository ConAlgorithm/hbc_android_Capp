package com.hugboga.custom.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.ChooseAirActivity;
import com.hugboga.custom.activity.OrderActivity;
import com.hugboga.custom.activity.PoiSearchActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CarBean;
import com.hugboga.custom.data.bean.CarListBean;
import com.hugboga.custom.data.bean.CollectGuideBean;
import com.hugboga.custom.data.bean.FlightBean;
import com.hugboga.custom.data.bean.PoiBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.request.RequestCheckPrice;
import com.hugboga.custom.data.request.RequestCheckPriceForPickup;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.DBHelper;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.widget.OrderGuideLayout;
import com.hugboga.custom.widget.OrderInfoItemView;
import com.hugboga.custom.widget.PickupOrderBottomView;
import com.hugboga.custom.widget.SkuOrderCarTypeView;
import com.hugboga.custom.widget.SkuOrderEmptyView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 17/5/17.
 */
public class FgPickup extends BaseFragment implements SkuOrderCarTypeView.OnSelectedCarListener, PickupOrderBottomView.OnConfirmListener {

    public static final String TAG = FgPickup.class.getSimpleName();

    @Bind(R.id.pickup_bottom_view)
    PickupOrderBottomView bottomView;
    @Bind(R.id.pickup_guide_layout)
    OrderGuideLayout guideLayout;
    @Bind(R.id.pickup_flight_layout)
    OrderInfoItemView flightLayout;
    @Bind(R.id.pickup_start_time_tv)
    TextView startTimeTv;
    @Bind(R.id.pickup_start_time_layout)
    LinearLayout startTimeLayout;
    @Bind(R.id.pickup_city_layout)
    OrderInfoItemView cityLayout;
    @Bind(R.id.pickup_car_type_view)
    SkuOrderCarTypeView carTypeView;
    @Bind(R.id.pickup_empty_layout)
    SkuOrderEmptyView emptyLayout;

    private FlightBean flightBean;
    private PoiBean poiBean;
    private CarListBean carListBean;
    private CarBean carBean;

    String carIds = null;
    CollectGuideBean collectGuideBean;

    @Override
    public int getContentViewId() {
        return R.layout.fg_pickup;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

    @Override
    protected void initView() {
        carTypeView.setOnSelectedCarListener(this);
        bottomView.setOnConfirmListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.pickup_flight_layout, R.id.pickup_city_layout})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.pickup_flight_layout:
                intent = new Intent(getActivity(), ChooseAirActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.pickup_city_layout:
                if (flightBean == null) {
                    CommonUtils.showToast("请先选择航班");
                } else {
                    intent = new Intent(getActivity(), PoiSearchActivity.class);
                    intent.putExtra(Constants.REQUEST_SOURCE, getEventSource());
                    intent.putExtra(PoiSearchActivity.KEY_CITY_ID, flightBean.arrCityId);
                    intent.putExtra(PoiSearchActivity.KEY_LOCATION, flightBean.arrLocation);
                    intent.putExtra(PoiSearchActivity.PARAM_BUSINESS_TYPE, 1);
                    getActivity().startActivity(intent);
                }
                break;
        }
    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case AIR_NO://接机航班
                FlightBean _flightBean = (FlightBean) action.getData();
                if (mBusinessType == Constants.BUSINESS_TYPE_SEND || _flightBean == null) {
                    break;
                }
                if (flightBean == _flightBean || (flightBean != null && TextUtils.equals(_flightBean.flightNo, flightBean.flightNo))) {
                    break;
                }
                flightBean = _flightBean;
                String desc1 = flightBean.flightNo + " " + flightBean.depCityName + "-" + flightBean.arrCityName;
                String desc2 = "当地时间" + DateUtils.getPointStrFromDate2(flightBean.arrDate) + flightBean.arrivalTime + "降落";
                flightLayout.setDesc(flightBean.arrAirportName, desc1, desc2);

                startTimeLayout.setVisibility(View.VISIBLE);//用车时间
                startTimeTv.setText(DateUtils.orderChooseDateTransform(flightBean.arrDate) + flightBean.arrivalTime);

                emptyLayout.setVisibility(View.GONE);
                carTypeView.setVisibility(View.GONE);
                bottomView.setVisibility(View.GONE);
                cityLayout.resetUI();
                break;
            case CHOOSE_POI_BACK:
                poiBean = (PoiBean) action.getData();
                cityLayout.setDesc(poiBean.placeName, poiBean.placeDetail);
                requestCarPriceList();
                break;
        }
    }

    protected void requestCarPriceList() {
        RequestCheckPriceForPickup requestCheckPriceForPickup = new RequestCheckPriceForPickup(getActivity(), 1
                , flightBean.arrAirportCode
                , flightBean.arrCityId
                , flightBean.arrLocation
                , poiBean.location
                , flightBean.arrDate + " " + flightBean.arrivalTime
                , carIds
                , collectGuideBean == null ? 0 : collectGuideBean.isQuality);
        requestData(requestCheckPriceForPickup);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RequestCheckPriceForPickup) {
            RequestCheckPrice requestCheckPrice = (RequestCheckPrice) request;
            carListBean = (CarListBean) requestCheckPrice.getData();
            if (carListBean.carList.size() > 0) {
                bottomView.setVisibility(View.VISIBLE);
                emptyLayout.setVisibility(View.GONE);
                carTypeView.update(carListBean);
                carTypeView.setVisibility(View.VISIBLE);
            } else {
                bottomView.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.VISIBLE);
                carTypeView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public String getEventSource() {
        return "接机下单";
    }

    @Override
    public void onSelectedCar(CarBean _carBean) {
        this.carBean = _carBean;
        bottomView.setData(carListBean, _carBean);
    }

    @Override
    public void onClickHideMoreCar(boolean isShow) {

    }

    @Override
    public void onConfirm() {
        if (!CommonUtils.isLogin(getContext())) {
            return;
        }
        carBean.expectedCompTime = carListBean.estTime;
        OrderActivity.Params params = new OrderActivity.Params();
        params.flightBean = flightBean;
        params.poiBean = poiBean;
        params.carListBean = carListBean;
        params.carBean = carBean;
        params.cityBean = DBHelper.findCityById("" + flightBean.arrCityId);
        params.orderType = 1;
        Intent intent = new Intent(getContext(), OrderActivity.class);
        intent.putExtra(Constants.PARAMS_DATA, params);
        startActivity(intent);
    }
}
