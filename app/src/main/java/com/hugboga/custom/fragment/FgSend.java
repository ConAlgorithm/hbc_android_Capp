package com.hugboga.custom.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.huangbaoche.hbcframe.data.net.ErrorHandler;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.ChooseAirPortActivity;
import com.hugboga.custom.activity.DatePickerActivity;
import com.hugboga.custom.activity.OrderActivity;
import com.hugboga.custom.activity.PickSendActivity;
import com.hugboga.custom.activity.PoiSearchActivity;
import com.hugboga.custom.activity.UnicornServiceActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.AirPort;
import com.hugboga.custom.data.bean.CarBean;
import com.hugboga.custom.data.bean.CarListBean;
import com.hugboga.custom.data.bean.ChooseDateBean;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.CouponsOrderTipBean;
import com.hugboga.custom.data.bean.GuideCarBean;
import com.hugboga.custom.data.bean.GuidesDetailData;
import com.hugboga.custom.data.bean.PoiBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.request.RequestCheckGuide;
import com.hugboga.custom.data.request.RequestCheckPrice;
import com.hugboga.custom.data.request.RequestCheckPriceForTransfer;
import com.hugboga.custom.data.request.RequestNewCars;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.ApiReportHelper;
import com.hugboga.custom.utils.CarUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.DBHelper;
import com.hugboga.custom.utils.DatabaseManager;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.utils.GuideCalendarUtils;
import com.hugboga.custom.widget.ConponsTipView;
import com.hugboga.custom.widget.CsDialog;
import com.hugboga.custom.widget.OrderBottomView;
import com.hugboga.custom.widget.OrderGuidanceView;
import com.hugboga.custom.widget.OrderGuideLayout;
import com.hugboga.custom.widget.OrderInfoItemView;
import com.hugboga.custom.widget.SkuOrderCarTypeView;
import com.hugboga.custom.widget.SkuOrderEmptyView;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.squareup.timessquare.CalendarListBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 17/5/18.
 */
public class FgSend extends BaseFragment implements SkuOrderCarTypeView.OnSelectedCarListener, OrderBottomView.OnConfirmListener{

    public static final String TAG = FgSend.class.getSimpleName();
    private static final int ORDER_TYPE = 2;

    @BindView(R.id.send_bottom_view)
    OrderBottomView bottomView;
    @BindView(R.id.send_conpons_tipview)
    ConponsTipView conponsTipView;

    @BindView(R.id.send_guide_layout)
    OrderGuideLayout guideLayout;
    @BindView(R.id.send_guidance_layout)
    OrderGuidanceView guidanceLayout;

    @BindView(R.id.send_airport_layout)
    OrderInfoItemView airportLayout;
    @BindView(R.id.send_poi_layout)
    OrderInfoItemView startPoiLayout;
    @BindView(R.id.send_time_layout)
    OrderInfoItemView timeLayout;

    @BindView(R.id.send_car_type_view)
    SkuOrderCarTypeView carTypeView;
    @BindView(R.id.send_empty_layout)
    SkuOrderEmptyView emptyLayout;

    @BindView(R.id.send_scrollview)
    ScrollView scrollView;

    private AirPort airPortBean;
    private PoiBean poiBean;
    private String serverDate;
    private String serverTime;
    private CarListBean carListBean;
    private CarBean carBean;
    private CityBean cityBean;
    private CsDialog csDialog;

    private GuidesDetailData guidesDetailData;
    private ArrayList<GuideCarBean> guideCarBeanList;
    private String airportCode;

    private boolean isOperated = true;//在页面有任意点击操作就记录下来，只记录第一次，统计需要

    private PickSendActivity.Params params;

    @Override
    public int getContentViewId() {
        return R.layout.fg_send;
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
        if (params != null) {
            if (params.guidesDetailData != null) {
                guidesDetailData = params.guidesDetailData;
                List<AirPort> airPortList = DatabaseManager.queryAirPortByCityId("" + guidesDetailData.cityId);
                if (airPortList != null && airPortList.size() > 0 && airPortList.get(0) != null) {
                    airPortBean = airPortList.get(0);
                    airportCode = airPortBean.airportCode;
                    airportLayout.setDesc(airPortBean.cityName + " " + airPortBean.airportName);
                    cityBean = DBHelper.findCityById("" + airPortBean.cityId);
                }
                guideLayout.setData(guidesDetailData);
                carTypeView.setGuidesDetailData(guidesDetailData);
                GuideCalendarUtils.getInstance().sendRequest(getContext(), guidesDetailData.guideId, ORDER_TYPE);
            }
            if (params.airPortBean != null) {
                setAirPortBean(params.airPortBean);
            } else if (params.flightBean != null) {//机场信息为空，默认显示接机机场所在城市
                setGuidanceLayout("" + params.flightBean.arrCityId, params.flightBean.arrCityName);
            } else if (!TextUtils.isEmpty(params.cityId) && !TextUtils.isEmpty(params.cityName)) {
                setGuidanceLayout("" +  params.cityId, params.cityName);
            }
            if (params.startPoiBean != null) {
                setStartPoiBean(params.startPoiBean);
            }
        }
        carTypeView.setOnSelectedCarListener(this);
        carTypeView.setOrderType(ORDER_TYPE);
        carTypeView.showLuggageExplain();
        bottomView.setOnConfirmListener(this);
        emptyLayout.setOnClickServicesListener(new SkuOrderEmptyView.OnClickServicesListener() {
            @Override
            public void onClickServices() {
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

        updateConponsTipView();

        setUmengEvent();
        setSensorsBuyRouteEvent();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        GuideCalendarUtils.getInstance().onDestory();
    }

    public boolean isAirPortNull() {
        return airPortBean == null;
    }

    public boolean isShowSaveDialog() {
        return (airPortBean != null && !TextUtils.equals(airPortBean.airportCode, airportCode)) || poiBean != null;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            hintConponsTipView();
        }
    }

    @OnClick({R.id.send_airport_layout, R.id.send_poi_layout, R.id.send_time_layout})
    public void onClick(View view) {
        setSensorsOnOperated();
        Intent intent;
        switch (view.getId()) {
            case R.id.send_airport_layout:
                intent = new Intent(getActivity(), ChooseAirPortActivity.class);
                if (guidesDetailData != null) {
                    intent.putExtra(ChooseAirPortActivity.KEY_CITY_ID, guidesDetailData.cityId);
                }
                intent.putExtra(Constants.PARAMS_SOURCE, getReferH5EventSource());
                getActivity().startActivity(intent);
                SensorsUtils.onAppClick(getEventSource(), "送达机场", source);
                break;
            case R.id.send_poi_layout:
                if (airPortBean == null) {
                    CommonUtils.showToast(R.string.send_check_airport_hint);
                } else {
                    intent = new Intent(getActivity(), PoiSearchActivity.class);
                    intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                    intent.putExtra(PoiSearchActivity.KEY_CITY_ID, airPortBean.cityId);
                    intent.putExtra(PoiSearchActivity.KEY_LOCATION, airPortBean.location);
                    intent.putExtra(PoiSearchActivity.PARAM_BUSINESS_TYPE, ORDER_TYPE);
                    getActivity().startActivity(intent);
                    SensorsUtils.onAppClick(getEventSource(), "出发地点", source);
                }
                break;
            case R.id.send_time_layout:
                if (airPortBean == null) {
                    CommonUtils.showToast(R.string.send_check_airport_hint);
                } else if (poiBean == null) {
                    CommonUtils.showToast(R.string.send_check_start_address_hint);
                } else {
                    showTimePicker();
                }
                break;
        }
    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case AIR_PORT_BACK:
                AirPort _airPortBean = (AirPort) action.getData();
                if (_airPortBean == null || (airPortBean != null && TextUtils.equals(_airPortBean.airportCode, airPortBean.airportCode))) {
                    break;
                }
                setAirPortBean(_airPortBean);
                break;
            case CHOOSE_POI_BACK:
                PoiBean _poiBean = (PoiBean) action.getData();
                if (_poiBean == null || _poiBean.mBusinessType != ORDER_TYPE || (poiBean != null && TextUtils.equals(_poiBean.placeName, poiBean.placeName))) {
                    break;
                }
                setStartPoiBean(_poiBean);
                getCars();
                break;
            case ORDER_REFRESH://价格或数量变更 刷新
                scrollToTop();
                getCars();
                break;
            case CHOOSE_DATE:
                ChooseDateBean chooseDateBean = (ChooseDateBean) action.getData();
                serverDate = chooseDateBean.halfDateStr;
                serverTime = chooseDateBean.serverTime;
                timeLayout.setDesc(DateUtils.getPointStrFromDate2(serverDate) + " " + serverTime);
                if (guidesDetailData != null) {
                    CalendarListBean calendarListBean = GuideCalendarUtils.getInstance().getCalendarListBean(chooseDateBean.halfDateStr);
                    if (calendarListBean != null && calendarListBean.isCanHalfService()) {
                        getCars();
                        break;
                    }
                }
                getCars();
                break;
            case CLICK_USER_LOGIN:
            case CLICK_USER_LOOUT:
                updateConponsTipView();
                break;
        }
    }

    public void setGuidanceLayout(String cityId, String cityName) {
        if (params == null || params.guidesDetailData == null) {
            guidanceLayout.setVisibility(View.VISIBLE);
            guidanceLayout.setData(cityId, cityName);
        }
    }

    public void setAirPortBean(AirPort airPort) {
        airPortBean = airPort;
        airportLayout.setDesc(airPortBean.cityName + " " + airPortBean.airportName);

        emptyLayout.setVisibility(View.GONE);
        carTypeView.setVisibility(View.GONE);
        bottomView.setVisibility(View.GONE);
        startPoiLayout.resetUI();
        poiBean = null;
        cityBean = DBHelper.findCityById("" + airPortBean.cityId);
        hintConponsTipView();
        setGuidanceLayout("" + airPortBean.cityId, airPortBean.cityName);
    }

    public void setStartPoiBean(PoiBean _poiBean) {
        poiBean = _poiBean;
        startPoiLayout.setDesc(poiBean.placeName, poiBean.placeDetail);
    }

    public void showTimePicker() {
        Intent intent = new Intent(getContext(), DatePickerActivity.class);
        if (guidesDetailData != null) {
            intent.putExtra(DatePickerActivity.PARAM_ASSIGN_GUIDE, true);
        }
        intent.putExtra(Constants.PARAMS_ORDER_TYPE, Constants.BUSINESS_TYPE_SEND);
        intent.putExtra(DatePickerActivity.PARAM_TYPE, DatePickerActivity.PARAM_TYPE_SINGLE_NOTEXT);
        if (!TextUtils.isEmpty(serverDate)) {
            try {
                ChooseDateBean chooseDateBean = new ChooseDateBean();
                chooseDateBean.halfDateStr = serverDate;
                chooseDateBean.halfDate = DateUtils.dateDateFormat.parse(serverDate);
                chooseDateBean.type = DatePickerActivity.PARAM_TYPE_SINGLE_NOTEXT;
                chooseDateBean.serverTime = serverTime;
                intent.putExtra(DatePickerActivity.PARAM_BEAN, chooseDateBean);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        getContext().startActivity(intent);
        SensorsUtils.onAppClick(getEventSource(), "出发时间", source);
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

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RequestCheckPriceForTransfer) {
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
                setSensorsPriceEvent(true);
            }
        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        super.onDataRequestError(errorInfo, request);
        if (checkActivityIsFinished()) {
            return;
        }
        if (request.errorType != BaseRequest.ERROR_TYPE_PROCESSED && request instanceof RequestCheckPriceForTransfer) {
            String errorCode = ErrorHandler.getErrorCode(errorInfo, request);
            String errorMessage = CommonUtils.getString(R.string.single_errormessage);
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

    private void getCars() {
        if (airPortBean == null || poiBean == null || TextUtils.isEmpty(serverDate) || TextUtils.isEmpty(serverTime)) {
            return;
        }
        if (guidesDetailData != null) {
            getGuideCars();
        } else {
            requestCarPriceList();
        }
    }

    private void requestCarPriceList() {
        RequestCheckPriceForTransfer requestCheckPriceForTransfer = new RequestCheckPriceForTransfer(getActivity()
                , ORDER_TYPE
                , airPortBean.airportCode
                , airPortBean.cityId
                , poiBean.location
                , airPortBean.location
                , serverDate + " " + serverTime
                , guidesDetailData == null ? "" : guidesDetailData.getCarIds()
                , guidesDetailData == null ? 0 : guidesDetailData.isQuality);
        requestData(requestCheckPriceForTransfer);
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
        SensorsUtils.onAppClick(getEventSource(), "下一步", source);
    }

    public void initOrderActivity() {
        OrderActivity.Params orderParams = new OrderActivity.Params();
        orderParams.airPortBean = airPortBean;
        orderParams.startPoiBean = poiBean;
        orderParams.carListBean = carListBean;
        orderParams.carBean = carBean;
        orderParams.cityBean = cityBean;
        orderParams.orderType = ORDER_TYPE;
        orderParams.serverDate = serverDate;
        orderParams.serverTime = serverTime;
        if (guidesDetailData != null) {
            orderParams.guidesDetailData = guidesDetailData;
        }
        Intent intent = new Intent(getContext(), OrderActivity.class);
        intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
        intent.putExtra(Constants.PARAMS_DATA, orderParams);
        startActivity(intent);

        StatisticClickEvent.sendClick(StatisticConstant.CONFIRM_S, source, carBean.desc + "");
        setSensorsConfirmEvent();
    }

    private void checkGuideCoflict() {
        RequestCheckGuide.CheckGuideBean checkGuideBean = new RequestCheckGuide.CheckGuideBean();
        checkGuideBean.startTime = serverDate + " " + serverTime + ":00";
        checkGuideBean.endTime = DateUtils.getDifferenceTime2(checkGuideBean.startTime, CommonUtils.getCountInteger(carListBean.estTime) * 60 * 1000);
        checkGuideBean.cityId = airPortBean.cityId;
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
                CommonUtils.apiErrorShowService(getContext(), errorInfo, request, FgSend.this.getEventSource(), false);
            }
        }, true);
    }

    public void updateConponsTipView() {
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
        return "送机";
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
        return StatisticConstant.LAUNCH_S;
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
            properties.put("hbc_sku_type", "送机");
            properties.put("hbc_guide_id", null != guidesDetailData ? guidesDetailData.guideId : "");// 指定司导下单
            properties.put("hbc_car_type", carBean.desc);//车型选择
            properties.put("hbc_price_total", carBean.price);//费用总计
            properties.put("hbc_distance", carListBean.distance);// 全程公里数
            properties.put("hbc_airport", airPortBean.airportName);// 机场
            properties.put("hbc_geton_time", serverDate + " " + serverTime);// 出发时间
            properties.put("hbc_geton_location", poiBean.placeName);// 出发地
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
            properties.put("hbc_sku_type", "送机");
            properties.put("hbc_guide_id", guidesDetailData != null ? guidesDetailData.guideId : "");
            SensorsDataAPI.sharedInstance(getContext()).track("buy_route", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
