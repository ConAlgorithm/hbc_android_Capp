package com.hugboga.custom.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ErrorHandler;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.ChooseAirActivity;
import com.hugboga.custom.activity.OrderActivity;
import com.hugboga.custom.activity.PickSendActivity;
import com.hugboga.custom.activity.PoiSearchActivity;
import com.hugboga.custom.activity.UnicornServiceActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CarBean;
import com.hugboga.custom.data.bean.CarListBean;
import com.hugboga.custom.data.bean.FlightBean;
import com.hugboga.custom.data.bean.GuideCarBean;
import com.hugboga.custom.data.bean.GuideCropBean;
import com.hugboga.custom.data.bean.GuidesDetailData;
import com.hugboga.custom.data.bean.PoiBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.request.RequestCheckGuide;
import com.hugboga.custom.data.request.RequestCheckPrice;
import com.hugboga.custom.data.request.RequestCheckPriceForPickup;
import com.hugboga.custom.data.request.RequestGuideCrop;
import com.hugboga.custom.data.request.RequestNewCars;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.utils.AlertDialogUtils;
import com.hugboga.custom.utils.ApiReportHelper;
import com.hugboga.custom.utils.CarUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.DBHelper;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.widget.DialogUtil;
import com.hugboga.custom.widget.OrderGuideLayout;
import com.hugboga.custom.widget.OrderInfoItemView;
import com.hugboga.custom.widget.OrderBottomView;
import com.hugboga.custom.widget.SkuOrderCarTypeView;
import com.hugboga.custom.widget.SkuOrderEmptyView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 17/5/17.
 */
public class FgPickup extends BaseFragment implements SkuOrderCarTypeView.OnSelectedCarListener, OrderBottomView.OnConfirmListener {

    public static final String TAG = FgPickup.class.getSimpleName();
    private static final int ORDER_TYPE = 1;

    @Bind(R.id.pickup_bottom_view)
    OrderBottomView bottomView;
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

    @Bind(R.id.pickup_scrollview)
    ScrollView scrollView;

    private FlightBean flightBean;
    private PoiBean poiBean;
    private CarListBean carListBean;
    private CarBean carBean;

    private GuidesDetailData guidesDetailData;
    private ArrayList<GuideCarBean> guideCarBeanList;
    private ArrayList<GuideCropBean> guideCropList;

    private PickSendActivity.Params params;

    @Override
    public int getContentViewId() {
        return R.layout.fg_pickup;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            params = (PickSendActivity.Params) savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = getArguments();
            if (bundle != null) {
                params = (PickSendActivity.Params) bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
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
    }

    @Override
    protected void initView() {
        if (params != null && params.guidesDetailData != null) {
            guidesDetailData = params.guidesDetailData;
            guideLayout.setData(guidesDetailData);
            requestData(new RequestGuideCrop(getContext(), guidesDetailData.guideId));
        }
        carTypeView.setOnSelectedCarListener(this);
        bottomView.setOnConfirmListener(this);
        carTypeView.setOrderType(ORDER_TYPE);
        emptyLayout.setOnClickServicesListener(new SkuOrderEmptyView.OnClickServicesListener() {
            @Override
            public void onClickServices() {
                DialogUtil.getInstance((Activity) getContext()).showServiceDialog(getContext(), null, UnicornServiceActivity.SourceType.TYPE_CHARTERED, null, null, getEventSource());
            }
        });
        emptyLayout.setOnRefreshDataListener(new SkuOrderEmptyView.OnRefreshDataListener() {
            @Override
            public void onRefresh() {
                scrollToTop();
                getCars();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public boolean isFlightBeanNull() {
        return flightBean == null;
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
                    intent.putExtra(PoiSearchActivity.PARAM_BUSINESS_TYPE, ORDER_TYPE);
                    getActivity().startActivity(intent);
                }
                break;
        }
    }

    private void setFlightBean(FlightBean _flightBean) {
        if (_flightBean == null || (flightBean != null && TextUtils.equals(_flightBean.flightNo, flightBean.flightNo))) {
            return;
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
        poiBean = null;
    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case AIR_NO://接机航班
                FlightBean _flightBean = (FlightBean) action.getData();
                if (!checkPickUpFlightBean(_flightBean)) {
                    break;
                }
                setFlightBean(_flightBean);
                break;
            case CHOOSE_POI_BACK:
                PoiBean _poiBean = (PoiBean) action.getData();
                if (_poiBean == null || _poiBean.mBusinessType != ORDER_TYPE || (poiBean != null && TextUtils.equals(_poiBean.placeName, poiBean.placeName))) {
                    break;
                }
                poiBean = _poiBean;
                cityLayout.setDesc(poiBean.placeName, poiBean.placeDetail);
                getCars();
                break;
            case ORDER_REFRESH://价格或数量变更 刷新
                scrollToTop();
                getCars();
                break;
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RequestCheckPriceForPickup) {
            RequestCheckPrice requestCheckPrice = (RequestCheckPrice) request;
            carListBean = (CarListBean) requestCheckPrice.getData();
            if (!checkDataIsEmpty(carListBean == null ? null : carListBean.carList, carListBean.noneCarsState, carListBean.noneCarsReason)) {
                if (guidesDetailData != null && guideCarBeanList != null) {
                    ArrayList<CarBean> carList = CarUtils.getCarBeanList(carListBean.carList, guideCarBeanList);
                    if (checkDataIsEmpty(carList)) {
                        return;
                    }
                    carListBean.carList = carList;
                }
                carTypeView.update(carListBean);
            }
        } else if (request instanceof RequestGuideCrop) {
            guideCropList = ((RequestGuideCrop) request).getData();
        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        super.onDataRequestError(errorInfo, request);
        if (request instanceof RequestCheckPriceForPickup) {
            String errorCode = ErrorHandler.getErrorCode(errorInfo, request);
            String errorMessage = "很抱歉，该城市暂时无法提供服务(%1$s)";
            checkDataIsEmpty(null, 0, String.format(errorMessage, errorCode));
            return;
        } else {
            emptyLayout.setErrorVisibility(View.VISIBLE);
            setItemVisibility(View.GONE);
        }
    }

    @Override
    public void onSelectedCar(CarBean _carBean) {
        this.carBean = _carBean;
        bottomView.setData(carListBean, _carBean);
    }

    @Override
    public void onClickHideMoreCar(boolean isShow) {

    }

    public boolean checkPickUpFlightBean(FlightBean _flightBean) {
        if (guidesDetailData == null || guideCropList == null) {
            return true;
        }
        boolean isContain = false;
        final int size = guideCropList.size();
        for (int i = 0; i < size; i++) {
            if (guideCropList.get(i) != null && _flightBean.arrCityName.equals(guideCropList.get(i).cityName)) {
                isContain = true;
                break;
            }
        }
        if (!isContain) {
            showGuideCheckPickUpDialog(_flightBean);
            return false;
        } else {
            return true;
        }
    }

    public void showGuideCheckPickUpDialog(final FlightBean _flightBean) {
        AlertDialogUtils.showAlertDialogCancelable(getContext(), String.format("很抱歉，您指定的司导无法服务%1$s城市", _flightBean.arrCityName), "取消", "不找Ta服务了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                params = null;
                guidesDetailData = null;
                guideLayout.setVisibility(View.GONE);
                setFlightBean(_flightBean);
                dialog.dismiss();
            }
        });
    }

    private boolean checkDataIsEmpty(ArrayList<CarBean> _carList) {
        return checkDataIsEmpty(_carList, 0, null);
    }

    private boolean checkDataIsEmpty(ArrayList<CarBean> _carList, int noneCarsState, String noneCarsReason) {
        boolean isEmpty = emptyLayout.setEmptyVisibility(_carList, noneCarsState, noneCarsReason, guidesDetailData != null);
        int itemVisibility = !isEmpty ? View.VISIBLE : View.GONE;
        setItemVisibility(itemVisibility);
        return isEmpty;
    }

    private void setItemVisibility(int visibility) {
        carTypeView.setVisibility(visibility);
        bottomView.setVisibility(visibility);
    }

    /* 滚动到顶部 */
    private void scrollToTop() {
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.smoothScrollTo(0, 0);
            }
        });
    }

    private void getCars() {
        if (flightBean == null || poiBean == null) {
            return;
        }
        if (guidesDetailData != null) {
            getGuideCars();
        } else {
            requestCarPriceList();
        }
    }

    private void requestCarPriceList() {
        RequestCheckPriceForPickup requestCheckPriceForPickup = new RequestCheckPriceForPickup(getActivity()
                , ORDER_TYPE
                , flightBean.arrAirportCode
                , flightBean.arrCityId
                , flightBean.arrLocation
                , poiBean.location
                , flightBean.arrDate + " " + flightBean.arrivalTime
                , guidesDetailData == null ? "" : guidesDetailData.getCarIds()
                , guidesDetailData == null ? 0 : guidesDetailData.isQuality);
        requestData(requestCheckPriceForPickup);
    }

    private void getGuideCars() {
        RequestNewCars requestCars = new RequestNewCars(getContext(), ORDER_TYPE, guidesDetailData.guideId, null);
        HttpRequestUtils.request(getContext(), requestCars, new HttpRequestListener() {
            @Override
            public void onDataRequestSucceed(BaseRequest request) {
                ApiReportHelper.getInstance().addReport(request);
                guideCarBeanList = ((RequestNewCars) request).getData();
                if (guideCarBeanList == null || guideCarBeanList.size() <= 0) {
                    checkDataIsEmpty(null);
                    return;
                }
                guidesDetailData.guideCars = guideCarBeanList;
                guidesDetailData.guideCarCount = guideCarBeanList.size();
                requestCarPriceList();
            }

            @Override
            public void onDataRequestCancel(BaseRequest request) {

            }

            @Override
            public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
                checkDataIsEmpty(null, 0, ErrorHandler.getErrorMessage(errorInfo, request));
            }
        }, true);
    }

    @Override
    public void onConfirm() {
        if (!CommonUtils.isLogin(getContext())) {
            return;
        }
        if (guidesDetailData != null) {
            checkGuideCoflict();
        } else {
            initOrderActivity();
        }
    }

    public void initOrderActivity() {
        OrderActivity.Params orderParams = new OrderActivity.Params();
        orderParams.flightBean = flightBean;
        orderParams.endPoiBean = poiBean;
        orderParams.carListBean = carListBean;
        orderParams.carBean = carBean;
        orderParams.cityBean = DBHelper.findCityById("" + flightBean.arrCityId);
        orderParams.orderType = ORDER_TYPE;
        orderParams.serverDate = flightBean.arrDate;
        orderParams.serverTime = flightBean.arrivalTime;
        if (guidesDetailData != null) {
            orderParams.guidesDetailData = guidesDetailData;
        }
        Intent intent = new Intent(getContext(), OrderActivity.class);
        intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
        intent.putExtra(Constants.PARAMS_DATA, orderParams);
        startActivity(intent);
    }

    private void checkGuideCoflict() {
        RequestCheckGuide.CheckGuideBean checkGuideBean = new RequestCheckGuide.CheckGuideBean();
        checkGuideBean.startTime = flightBean.arrDate + " " + flightBean.arrivalTime + ":00";
        checkGuideBean.endTime = flightBean.arrDate + " " + Constants.SERVER_TIME_END;
        checkGuideBean.cityId = flightBean.arrCityId;
        checkGuideBean.guideId = guidesDetailData.guideId;
        checkGuideBean.orderType = ORDER_TYPE;

        RequestCheckGuide requestCheckGuide = new RequestCheckGuide(getContext(), checkGuideBean);
        HttpRequestUtils.request(getContext(), requestCheckGuide, new HttpRequestListener() {
            @Override
            public void onDataRequestSucceed(BaseRequest request) {
                ApiReportHelper.getInstance().addReport(request);
                initOrderActivity();
            }

            @Override
            public void onDataRequestCancel(BaseRequest request) {

            }

            @Override
            public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
                CommonUtils.apiErrorShowService(getContext(), errorInfo, request, FgPickup.this.getEventSource());
            }
        }, true);
    }

    @Override
    public String getEventSource() {
        return "接机下单";
    }

    @Override
    public String getEventId() {
        return StatisticConstant.LAUNCH_J;
    }
}
