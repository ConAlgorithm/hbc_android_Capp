package com.hugboga.custom.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

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
import com.hugboga.custom.data.bean.CouponsOrderTipBean;
import com.hugboga.custom.data.bean.FlightBean;
import com.hugboga.custom.data.bean.GuideCarBean;
import com.hugboga.custom.data.bean.GuidesDetailData;
import com.hugboga.custom.data.bean.PoiBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.request.RequestCheckGuide;
import com.hugboga.custom.data.request.RequestCheckPrice;
import com.hugboga.custom.data.request.RequestCheckPriceForPickup;
import com.hugboga.custom.data.request.RequestNewCars;
import com.hugboga.custom.data.request.RequestSeckillsPickupPrice;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.AlertDialogUtils;
import com.hugboga.custom.utils.ApiReportHelper;
import com.hugboga.custom.utils.CarUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.DBHelper;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.widget.ConponsTipView;
import com.hugboga.custom.widget.CsDialog;
import com.hugboga.custom.widget.OrderBottomView;
import com.hugboga.custom.widget.OrderGuidanceView;
import com.hugboga.custom.widget.OrderGuideLayout;
import com.hugboga.custom.widget.OrderInfoItemView;
import com.hugboga.custom.widget.SkuOrderCarTypeView;
import com.hugboga.custom.widget.SkuOrderEmptyView;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 17/5/17.
 */
public class FgPickup extends BaseFragment implements SkuOrderCarTypeView.OnSelectedCarListener, OrderBottomView.OnConfirmListener {

    public static final String TAG = FgPickup.class.getSimpleName();
    private static final int ORDER_TYPE = 1;

    @BindView(R.id.pickup_seckills_layout)
    RelativeLayout seckillsLayout;
    @BindView(R.id.pickup_bottom_view)
    OrderBottomView bottomView;
    @BindView(R.id.pickup_guide_layout)
    OrderGuideLayout guideLayout;
    @BindView(R.id.pickup_flight_layout)
    OrderInfoItemView flightLayout;
    @BindView(R.id.pickup_start_time_layout)
    LinearLayout startTimeLayout;
    @BindView(R.id.pickup_city_layout)
    OrderInfoItemView cityLayout;
    @BindView(R.id.pickup_car_type_view)
    SkuOrderCarTypeView carTypeView;
    @BindView(R.id.pickup_empty_layout)
    SkuOrderEmptyView emptyLayout;
    @BindView(R.id.pickup_conpons_tipview)
    ConponsTipView conponsTipView;
    @BindView(R.id.pickup_guidance_layout)
    OrderGuidanceView guidanceLayout;

    @BindView(R.id.pickup_scrollview)
    ScrollView scrollView;

    private FlightBean flightBean;
    private PoiBean poiBean;
    private CarListBean carListBean;
    private CarBean carBean;

    private GuidesDetailData guidesDetailData;
    private ArrayList<GuideCarBean> guideCarBeanList;

    private boolean isOperated = true;//在页面有任意点击操作就记录下来，只记录第一次，统计需要

    private PickSendActivity.Params params;
    CsDialog csDialog;
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
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            hintConponsTipView();
        }
    }

    @Override
    protected void initView() {
        if (params != null) {
            if (params.guidesDetailData != null) {
                guidesDetailData = params.guidesDetailData;
                guideLayout.setData(guidesDetailData);
                carTypeView.setGuidesDetailData(guidesDetailData);
            } else if (params.isSeckills) {
                carTypeView.setIsSeckills(true);
                seckillsLayout.setVisibility(View.VISIBLE);
            }
            if (params.flightBean != null) {
                setFlightBean(params.flightBean);
            } else if (params.airPortBean != null) {//航班信息为空，默认显示送机机场所在城市
                setGuidanceLayout("" +  params.airPortBean.cityId, params.airPortBean.cityName);
                setSensorsBuyRouteEvent();
            } else if (!TextUtils.isEmpty(params.cityId) && !TextUtils.isEmpty(params.cityName)) {
                setGuidanceLayout("" +  params.cityId, params.cityName);
                setSensorsBuyRouteEvent();
            }
        }
        carTypeView.setOnSelectedCarListener(this);
        carTypeView.setOrderType(ORDER_TYPE);
        carTypeView.showLuggageExplain();
        bottomView.setOnConfirmListener(this);
        emptyLayout.setOnClickServicesListener(new SkuOrderEmptyView.OnClickServicesListener() {
            @Override
            public void onClickServices() {
                //DialogUtil.getInstance((Activity) getContext()).showServiceDialog(getContext(), null, UnicornServiceActivity.SourceType.TYPE_CHARTERED, null, null, getEventSource());
                csDialog = CommonUtils.csDialog(getContext(), null, null, null, UnicornServiceActivity.SourceType.TYPE_CHARTERED, getEventSource(), new CsDialog.OnCsListener() {
                    @Override
                    public void onCs() {
                        if (csDialog != null && csDialog.isShowing()) {
                            csDialog.dismiss();
                        }
                    }
                });
            }
        });
        emptyLayout.setOnRefreshDataListener(new SkuOrderEmptyView.OnRefreshDataListener() {
            @Override
            public void onRefresh() {
                scrollToTop();
                getCars();
            }
        });

        setUmengEvent();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateConponsTipView();
    }

    public boolean isFlightBeanNull() {
        return flightBean == null;
    }

    @OnClick({R.id.pickup_flight_layout, R.id.pickup_city_layout})
    public void onClick(View view) {
        setSensorsOnOperated();
        Intent intent;
        switch (view.getId()) {
            case R.id.pickup_flight_layout:
                intent = new Intent(getActivity(), ChooseAirActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("flightBean",flightBean);
                intent.putExtra("flightBean",bundle);
                intent.putExtra(Constants.PARAMS_SOURCE, getReferH5EventSource());
                getActivity().startActivity(intent);
                break;
            case R.id.pickup_city_layout:
                if (flightBean == null) {
                    CommonUtils.showToast(R.string.pick_flight);
                } else {
                    intent = new Intent(getActivity(), PoiSearchActivity.class);
                    intent.putExtra(Constants.PARAMS_SOURCE, getReferH5EventSource());
                    intent.putExtra(PoiSearchActivity.KEY_CITY_ID, flightBean.arrCityId);
                    intent.putExtra(PoiSearchActivity.KEY_LOCATION, flightBean.arrLocation);
                    intent.putExtra(PoiSearchActivity.PARAM_BUSINESS_TYPE, ORDER_TYPE);
                    getActivity().startActivity(intent);
                }
                break;
        }
    }

    private void setFlightBean(FlightBean _flightBean) {
        if (_flightBean == null) {
            return;
        }
        boolean checkFlightBean = flightBean != null
                && TextUtils.equals(_flightBean.flightNo, flightBean.flightNo)
                && TextUtils.equals(_flightBean.arrDate, flightBean.arrDate);
        if (checkFlightBean) {
            return;
        }
        if (flightBean == null) {
            scrollView.setVisibility(View.VISIBLE);
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            Fragment fragment = getChildFragmentManager().findFragmentById(R.id.fg_pickup_fragment_choose_air);
            ft.remove(fragment);
            ft.commitAllowingStateLoss();
            setSensorsBuyRouteEvent();
        }

        flightBean = _flightBean;
        String desc1 = flightBean.flightNo + " " + flightBean.depCityName + "-" + flightBean.arrCityName;
        String desc2 = CommonUtils.getString(R.string.pick_time_arrival, (DateUtils.getPointStrFromDate2(flightBean.arrDate) + flightBean.arrivalTime));
        flightLayout.setDesc(flightBean.arrAirportName, desc1, desc2);

        startTimeLayout.setVisibility(View.VISIBLE);//用车时间

        emptyLayout.setVisibility(View.GONE);
        carTypeView.setVisibility(View.GONE);
        bottomView.setVisibility(View.GONE);
        cityLayout.resetUI();
        poiBean = null;
        hintConponsTipView();
        setGuidanceLayout("" + flightBean.arrCityId, flightBean.arrCityName);
    }

    public void setGuidanceLayout(String cityId, String cityName) {
        if (params == null || params.guidesDetailData == null) {
            guidanceLayout.setVisibility(View.VISIBLE);
            guidanceLayout.setData("" + cityId, cityName);
        }
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
                isOperated = false;
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
            case ORDER_SECKILLS_REFRESH:
                refreshData();
                break;
            case CLICK_USER_LOGIN:
            case CLICK_USER_LOOUT:
                updateConponsTipView();
                break;
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RequestCheckPriceForPickup) {
            RequestCheckPrice requestCheckPrice = (RequestCheckPrice) request;
            CarListBean _carListBean = (CarListBean) requestCheckPrice.getData();
            setCarListBean(_carListBean);
        } else if (request instanceof RequestSeckillsPickupPrice) {
            RequestSeckillsPickupPrice requestSeckillsPickupPrice = (RequestSeckillsPickupPrice) request;
            CarListBean _carListBean = (CarListBean) requestSeckillsPickupPrice.getData();
            _carListBean.isSeckills = true;
            _carListBean.timeLimitedSaleNo = params.timeLimitedSaleNo;
            _carListBean.timeLimitedSaleScheduleNo = params.timeLimitedSaleScheduleNo;
            setCarListBean(_carListBean);
        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        super.onDataRequestError(errorInfo, request);
        if (checkActivityIsFinished()) {
            return;
        }
        if (request.errorType != BaseRequest.ERROR_TYPE_PROCESSED && request instanceof RequestCheckPriceForPickup) {
            String errorCode = ErrorHandler.getErrorCode(errorInfo, request);
            String errorMessage = CommonUtils.getString(R.string.single_errormessage);
            if(errorMessage!= null && errorCode != null){
                checkDataIsEmpty(null, 0, String.format(errorMessage, errorCode));
            }
        } else if (request.errorType != BaseRequest.ERROR_TYPE_PROCESSED && request instanceof RequestSeckillsPickupPrice) {
            String errorMessage = ErrorHandler.getErrorMessage(errorInfo, request);
            showCheckSeckillsDialog(errorMessage);
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

    private void showCheckSeckillsDialog(String content) {
        AlertDialogUtils.showAlertDialog(getContext(), content, CommonUtils.getString(R.string.order_empty_continue), CommonUtils.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                refreshData();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    public void refreshData() {
        seckillsLayout.setVisibility(View.GONE);
        if (params != null) {
            params.isSeckills = false;
        }
        updateConponsTipView();
        requestCarPriceList();
        scrollToTop();
    }

    public void setCarListBean(CarListBean carListBean) {
        this.carListBean = carListBean;
        if (!checkDataIsEmpty(carListBean == null ? null : carListBean.carList, carListBean.noneCarsState, carListBean.noneCarsReason)) {
            if (guidesDetailData != null && guideCarBeanList != null) {
                ArrayList<CarBean> carList = CarUtils.getCarBeanList(carListBean.carList, guideCarBeanList);
                if (checkDataIsEmpty(carList)) {
                    return;
                }
                carListBean.carList = carList;
            }
            carTypeView.update(carListBean);
            setSensorsPriceEvent(true);
        }
    }

    public boolean checkPickUpFlightBean(FlightBean _flightBean) {
        if (guidesDetailData == null) {
            return true;
        }
        if (guidesDetailData.cityId != _flightBean.arrCityId) {
            showGuideCheckPickUpDialog(_flightBean);
            return false;
        } else {
            return true;
        }
    }

    public void showGuideCheckPickUpDialog(final FlightBean _flightBean) {
        AlertDialogUtils.showAlertDialogCancelable(getContext(), CommonUtils.getString(R.string.daily_second_guide_cannot_service, _flightBean.arrCityName)
                , CommonUtils.getString(R.string.cancel), CommonUtils.getString(R.string.daily_second_no_assign), new DialogInterface.OnClickListener() {
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
                carTypeView.setGuidesDetailData(null);
                setFlightBean(_flightBean);
                dialog.dismiss();
            }
        });
    }

    private boolean checkDataIsEmpty(ArrayList<CarBean> _carList) {
        return checkDataIsEmpty(_carList, 0, null);
    }

    private boolean checkDataIsEmpty(ArrayList<CarBean> _carList, int noneCarsState, String noneCarsReason) {
        boolean isEmpty = emptyLayout.setEmptyVisibility(_carList, noneCarsState, noneCarsReason, guidesDetailData != null, ORDER_TYPE);
        int itemVisibility = !isEmpty ? View.VISIBLE : View.GONE;
        setItemVisibility(itemVisibility);
        if (isEmpty) {
            setSensorsPriceEvent(false);
        }
        return isEmpty;
    }

    private void setItemVisibility(int visibility) {
        carTypeView.setVisibility(visibility);
        bottomView.setVisibility(visibility);
        hintConponsTipView();
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
        } else if (params != null && params.isSeckills) {
            requestSeckillsPickupPrice();
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

    private void requestSeckillsPickupPrice() {
        RequestSeckillsPickupPrice.Builder builder = new RequestSeckillsPickupPrice.Builder();
        builder.timeLimitedSaleNo = params.timeLimitedSaleNo;
        builder.timeLimitedSaleScheduleNo = params.timeLimitedSaleScheduleNo;
        builder.airportCode = flightBean.arrAirportCode;
        builder.serviceDate = flightBean.arrDate + " " + flightBean.arrivalTime + ":00";
        builder.startLocation = flightBean.arrLocation;
        builder.endLocation = poiBean.location;
        builder.endAddress = poiBean.placeName;
        builder.endDetailAddress = poiBean.placeDetail;
        RequestSeckillsPickupPrice requestSeckillsPickupPrice = new RequestSeckillsPickupPrice(getActivity(), builder);
        requestData(requestSeckillsPickupPrice);
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
                if (checkActivityIsFinished()) {
                    return;
                }
                if (request.errorType != BaseRequest.ERROR_TYPE_PROCESSED) {
                    checkDataIsEmpty(null, ErrorHandler.getServerErrorCode(errorInfo, request), ErrorHandler.getErrorMessage(errorInfo, request));
                }
            }
        }, true);
    }

    @Override
    public void onConfirm() {
        if (!CommonUtils.isLogin(getContext(),getEventSource())) {
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

        if (params != null && params.isSeckills) {
            StatisticClickEvent.pickClick(StatisticConstant.CONFIRM_J_MS, source, carBean.desc + "");
        } else {
            StatisticClickEvent.pickClick(StatisticConstant.CONFIRM_J, source, carBean.desc + "");
        }
        setSensorsConfirmEvent();
        SensorsUtils.onAppClick(getEventSource(), "下一步", source);
    }

    private void checkGuideCoflict() {
        RequestCheckGuide.CheckGuideBean checkGuideBean = new RequestCheckGuide.CheckGuideBean();
        checkGuideBean.startTime = flightBean.arrDate + " " + flightBean.arrivalTime + ":00";
        checkGuideBean.endTime = DateUtils.getDifferenceTime2(checkGuideBean.startTime, CommonUtils.getCountInteger(carListBean.estTime) * 60 * 1000);
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
                if (checkActivityIsFinished()) {
                    return;
                }
                CommonUtils.apiErrorShowService(getContext(), errorInfo, request, FgPickup.this.getEventSource(), false);
            }
        }, true);
    }

    public void updateConponsTipView() {
        if (params != null && params.isSeckills) {
            conponsTipView.setVisibility(View.GONE);
            return;
        }
        conponsTipView.update(ORDER_TYPE);
        conponsTipView.setOnCouponsTipRequestSucceedListener(new ConponsTipView.OnCouponsTipRequestSucceedListener() {
            @Override
            public void onCouponsTipRequestSucceed(CouponsOrderTipBean couponsOrderTipBean) {
                bottomView.setConponsTip(couponsOrderTipBean != null ? couponsOrderTipBean.couponCountTips : null);
                hintConponsTipView();
            }
        });
    }

    public void hintConponsTipView() {
        if (emptyLayout == null || conponsTipView == null) {
            return;
        }
        if (emptyLayout.getVisibility() == View.VISIBLE || carTypeView.getVisibility() == View.VISIBLE || carListBean != null) {
            conponsTipView.setVisibility(View.GONE);
        } else {
            conponsTipView.showView();
        }
    }

    @Override
    public String getEventSource() {
        return "接机";
    }

    public String getReferH5EventSource() {
        String eventSource = getEventSource();
        if (getContext() instanceof PickSendActivity) {
            return ((PickSendActivity)getContext()).getReferH5EventSource(eventSource);
        } else {
            return eventSource;
        }
    }

    @Override
    public String getEventId() {
        if (params != null && params.isSeckills) {
            return StatisticConstant.LAUNCH_J_MS;
        } else {
            return StatisticConstant.LAUNCH_J;
        }
    }

    public boolean checkActivityIsFinished() {
        if (getContext() instanceof Activity && ((Activity) getContext()).isFinishing()) {
            return true;
        } else {
            return false;
        }
    }

    private void setUmengEvent() {
        Map map = new HashMap();
        map.put(Constants.PARAMS_SOURCE, source);
        MobClickUtils.onEvent(getEventId(), map);
    }

    //神策统计_确认行程
    private void setSensorsConfirmEvent() {
        try {
            JSONObject properties = new JSONObject();
            properties.put("hbc_sku_type", "接机");
            properties.put("hbc_guide_id", null != guidesDetailData ? guidesDetailData.guideId : "");// 指定司导下单
            properties.put("hbc_car_type", carBean.desc);//车型选择
            properties.put("hbc_price_total", carBean.price);//费用总计
            properties.put("hbc_distance", carListBean.distance);// 全程公里数
            properties.put("hbc_flight_no", flightBean.flightNo);// 航班
            properties.put("hbc_airport", flightBean.arrAirportName);// 机场
            properties.put("hbc_geton_time", flightBean.arrDate + " " + flightBean.arrivalTime);// 出发时间
            properties.put("hbc_dest_location", poiBean.placeName);// 送达地
            SensorsDataAPI.sharedInstance(getActivity()).track("buy_confirm", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //神策统计_下单-有操作
    private void setSensorsOnOperated() {
        if (isOperated) {
            isOperated = false;
            SensorsUtils.onOperated(source, getEventSource());
        }
    }

    //神策统计_展示报价
    private void setSensorsPriceEvent(boolean isHavePrice) {
        String guideId = guidesDetailData != null ? guidesDetailData.guideId : "";
        SensorsUtils.setSensorsPriceEvent("" + ORDER_TYPE, guideId, isHavePrice);
    }

    //神策统计_来到填行程页
    private void setSensorsBuyRouteEvent() {
        try {
            JSONObject properties = new JSONObject();
            properties.put("hbc_refer", source);
            properties.put("hbc_guide_id", guidesDetailData != null ? guidesDetailData.guideId : "");
            properties.put("hbc_sku_type", "接机");
            SensorsDataAPI.sharedInstance(getContext()).track("buy_route", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
