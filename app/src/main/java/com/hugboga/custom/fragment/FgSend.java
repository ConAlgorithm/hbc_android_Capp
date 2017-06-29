package com.hugboga.custom.fragment;

import android.app.Activity;
import android.content.DialogInterface;
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
import com.hugboga.custom.data.bean.GuideCarBean;
import com.hugboga.custom.data.bean.GuidesDetailData;
import com.hugboga.custom.data.bean.PoiBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.request.RequestCheckGuide;
import com.hugboga.custom.data.request.RequestCheckPrice;
import com.hugboga.custom.data.request.RequestCheckPriceForTransfer;
import com.hugboga.custom.data.request.RequestGuideConflict;
import com.hugboga.custom.data.request.RequestNewCars;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.utils.AlertDialogUtils;
import com.hugboga.custom.utils.ApiReportHelper;
import com.hugboga.custom.utils.CarUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.DBHelper;
import com.hugboga.custom.utils.DatabaseManager;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.utils.GuideCalendarUtils;
import com.hugboga.custom.widget.DialogUtil;
import com.hugboga.custom.widget.OrderBottomView;
import com.hugboga.custom.widget.OrderGuideLayout;
import com.hugboga.custom.widget.OrderInfoItemView;
import com.hugboga.custom.widget.SkuOrderCarTypeView;
import com.hugboga.custom.widget.SkuOrderEmptyView;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.sensorsdata.analytics.android.sdk.exceptions.InvalidDataException;
import com.squareup.timessquare.CalendarListBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 17/5/18.
 */
public class FgSend extends BaseFragment implements SkuOrderCarTypeView.OnSelectedCarListener, OrderBottomView.OnConfirmListener{

    public static final String TAG = FgSend.class.getSimpleName();
    private static final int ORDER_TYPE = 2;

    @Bind(R.id.send_bottom_view)
    OrderBottomView bottomView;

    @Bind(R.id.send_guide_layout)
    OrderGuideLayout guideLayout;

    @Bind(R.id.send_airport_layout)
    OrderInfoItemView airportLayout;
    @Bind(R.id.send_poi_layout)
    OrderInfoItemView startPoiLayout;
    @Bind(R.id.send_time_layout)
    OrderInfoItemView timeLayout;

    @Bind(R.id.send_car_type_view)
    SkuOrderCarTypeView carTypeView;
    @Bind(R.id.send_empty_layout)
    SkuOrderEmptyView emptyLayout;

    @Bind(R.id.send_scrollview)
    ScrollView scrollView;

    private AirPort airPortBean;
    private PoiBean poiBean;
    private String serverDate;
    private String serverTime;
    private CarListBean carListBean;
    private CarBean carBean;
    private CityBean cityBean;

    private GuidesDetailData guidesDetailData;
    private ArrayList<GuideCarBean> guideCarBeanList;

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
        if (params != null && params.guidesDetailData != null) {
            guidesDetailData = params.guidesDetailData;
            List<AirPort> airPortList = DatabaseManager.queryAirPortByCityId("" + guidesDetailData.cityId);
            if (airPortList != null && airPortList.size() > 0 && airPortList.get(0) != null) {
                airPortBean = airPortList.get(0);
                airportLayout.setDesc(airPortBean.cityName + " " + airPortBean.airportName);
            }
            guideLayout.setData(guidesDetailData);
            carTypeView.setGuidesDetailData(guidesDetailData);
            GuideCalendarUtils.getInstance().sendRequest(getContext(), guidesDetailData.guideId, ORDER_TYPE);
        }
        carTypeView.setOnSelectedCarListener(this);
        carTypeView.setOrderType(ORDER_TYPE);
        bottomView.setOnConfirmListener(this);
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

        setUmengEvent();
        setSensorsEvent();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        GuideCalendarUtils.getInstance().onDestory();
    }

    public boolean isAirPortNull() {
        return airPortBean == null;
    }

    @OnClick({R.id.send_airport_layout, R.id.send_poi_layout, R.id.send_time_layout})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.send_airport_layout:
                intent = new Intent(getActivity(),ChooseAirPortActivity.class);
                if (guidesDetailData != null) {
                    intent.putExtra(ChooseAirPortActivity.KEY_CITY_ID, guidesDetailData.cityId);
                }
                getActivity().startActivity(intent);
                break;
            case R.id.send_poi_layout:
                if (airPortBean == null) {
                    CommonUtils.showToast("请先选择机场");
                } else {
                    intent = new Intent(getActivity(), PoiSearchActivity.class);
                    intent.putExtra(Constants.REQUEST_SOURCE, getEventSource());
                    intent.putExtra(PoiSearchActivity.KEY_CITY_ID, airPortBean.cityId);
                    intent.putExtra(PoiSearchActivity.KEY_LOCATION, airPortBean.location);
                    intent.putExtra(PoiSearchActivity.PARAM_BUSINESS_TYPE, ORDER_TYPE);
                    getActivity().startActivity(intent);
                }
                break;
            case R.id.send_time_layout:
                if (airPortBean == null) {
                    CommonUtils.showToast("请先选择机场");
                } else if (poiBean == null) {
                    CommonUtils.showToast("请先填写出发地点");
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
                if (_airPortBean == null || (airPortBean != null && _airPortBean.airportId == airPortBean.airportId)) {
                    break;
                }
                airPortBean = _airPortBean;
                airportLayout.setDesc(airPortBean.cityName + " " + airPortBean.airportName);

                emptyLayout.setVisibility(View.GONE);
                carTypeView.setVisibility(View.GONE);
                bottomView.setVisibility(View.GONE);
                startPoiLayout.resetUI();
                poiBean = null;
                cityBean = DBHelper.findCityById("" + airPortBean.cityId);
                break;
            case CHOOSE_POI_BACK:
                PoiBean _poiBean = (PoiBean) action.getData();
                if (_poiBean == null || _poiBean.mBusinessType != ORDER_TYPE || (poiBean != null && TextUtils.equals(_poiBean.placeName, poiBean.placeName))) {
                    break;
                }
                poiBean = _poiBean;
                startPoiLayout.setDesc(poiBean.placeName, poiBean.placeDetail);
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
                        checkGuideTimeCoflict();
                        break;
                    }
                }
                getCars();
                break;
        }
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
            }
        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        super.onDataRequestError(errorInfo, request);
        if (request.errorType != BaseRequest.ERROR_TYPE_PROCESSED && request instanceof RequestCheckPriceForTransfer) {
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
                if (request.errorType != BaseRequest.ERROR_TYPE_PROCESSED) {
                    checkDataIsEmpty(null, 0, ErrorHandler.getErrorMessage(errorInfo, request));
                }
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
                CommonUtils.apiErrorShowService(getContext(), errorInfo, request, FgSend.this.getEventSource(), false);
            }
        }, true);
    }    private void checkGuideTimeCoflict() {
        RequestGuideConflict requestGuideConflict = new RequestGuideConflict(getContext()
                , ORDER_TYPE
                , airPortBean.cityId
                , guidesDetailData.guideId
                , serverDate + " " + serverTime + ":00"
                , airPortBean.location
                , poiBean.location
                , cityBean != null ? cityBean.placeId : "");
        HttpRequestUtils.request(getContext(), requestGuideConflict, new HttpRequestListener() {
            @Override
            public void onDataRequestSucceed(BaseRequest request) {
                ApiReportHelper.getInstance().addReport(request);
                getCars();
            }

            @Override
            public void onDataRequestCancel(BaseRequest request) {

            }

            @Override
            public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
                AlertDialogUtils.showAlertDialogCancelable(getContext(), "很抱歉，您指定的司导该期间无法服务", "返回上一步", "不找Ta服务了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((Activity) getContext()).finish();
                        dialog.dismiss();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        guidesDetailData = null;
                        getCars();
                        dialog.dismiss();
                    }
                });
            }
        }, true);
    }



    @Override
    public String getEventSource() {
        return "送机下单";
    }

    @Override
    public String getEventId() {
        return StatisticConstant.LAUNCH_S;
    }

    private void setUmengEvent() {
        Map map = new HashMap();
        map.put(Constants.PARAMS_SOURCE, source);
        MobClickUtils.onEvent(getEventId(), map);
    }

    //神策统计_初始页浏览
    private void setSensorsEvent() {
        try {
            JSONObject properties = new JSONObject();
            properties.put("hbc_sku_type", "送机");
            properties.put("hbc_refer", source);
            SensorsDataAPI.sharedInstance(getActivity()).track("buy_view", properties);
        } catch (InvalidDataException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //神策统计_确认行程
    private void setSensorsConfirmEvent() {
        try {
            JSONObject properties = new JSONObject();
            properties.put("hbc_sku_type", "送机");
            properties.put("hbc_is_appoint_guide", null != guidesDetailData ? true : false);// 指定司导下单
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
}
