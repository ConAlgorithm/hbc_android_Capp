package com.hugboga.custom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ScrollView;

import com.huangbaoche.hbcframe.data.net.ErrorHandler;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CarBean;
import com.hugboga.custom.data.bean.CarListBean;
import com.hugboga.custom.data.bean.ChooseDateBean;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.CouponsOrderTipBean;
import com.hugboga.custom.data.bean.GuideCarBean;
import com.hugboga.custom.data.bean.GuidesDetailData;
import com.hugboga.custom.data.bean.PoiBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestCheckGuide;
import com.hugboga.custom.data.request.RequestCheckPrice;
import com.hugboga.custom.data.request.RequestCheckPriceForSingle;
import com.hugboga.custom.data.request.RequestNewCars;
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
import com.hugboga.custom.utils.IntentUtils;
import com.hugboga.custom.utils.OrderUtils;
import com.hugboga.custom.widget.ConponsTipView;
import com.hugboga.custom.widget.CsDialog;
import com.hugboga.custom.widget.OrderBottomView;
import com.hugboga.custom.widget.OrderGuidanceView;
import com.hugboga.custom.widget.OrderGuideLayout;
import com.hugboga.custom.widget.OrderInfoItemView;
import com.hugboga.custom.widget.SendAddressView;
import com.hugboga.custom.widget.SkuOrderCarTypeView;
import com.hugboga.custom.widget.SkuOrderEmptyView;
import com.hugboga.custom.widget.title.TitleBar;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.squareup.timessquare.CalendarListBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by qingcha on 17/5/23.
 */
public class SingleActivity extends BaseActivity implements SendAddressView.OnAddressClickListener
        , SkuOrderCarTypeView.OnSelectedCarListener, OrderBottomView.OnConfirmListener, TitleBar.OnTitleBarBackListener{

    public static final String TAG = SingleActivity.class.getSimpleName();
    private static final int ORDER_TYPE = 4;

    @BindView(R.id.single_titlebar)
    TitleBar titlebar;
    @BindView(R.id.single_bottom_view)
    OrderBottomView bottomView;
    @BindView(R.id.single_guide_layout)
    OrderGuideLayout guideLayout;
    @BindView(R.id.single_city_layout)
    OrderInfoItemView cityLayout;
    @BindView(R.id.single_address_layout)
    SendAddressView addressLayout;
    @BindView(R.id.single_time_layout)
    OrderInfoItemView timeLayout;
    @BindView(R.id.single_car_type_view)
    SkuOrderCarTypeView carTypeView;
    @BindView(R.id.single_empty_layout)
    SkuOrderEmptyView emptyLayout;
    @BindView(R.id.single_scrollview)
    ScrollView scrollView;
    @BindView(R.id.single_conpons_tipview)
    ConponsTipView conponsTipView;
    @BindView(R.id.single_guidance_layout)
    OrderGuidanceView guidanceLayout;

    private CarListBean carListBean;
    private CarBean carBean;
    private CityBean cityBean;
    private PoiBean startPoiBean, endPoiBean;
    private String serverDate;
    private String serverTime;
    private CsDialog csDialog;

    private GuidesDetailData guidesDetailData;
    private ArrayList<GuideCarBean> guideCarBeanList;
    private int guideCityId;

    private boolean isOperated = true;//在页面有任意点击操作就记录下来，只记录第一次，统计需要

    private SingleActivity.Params params;

    public static class Params implements Serializable {
        public GuidesDetailData guidesDetailData;
        public String cityId;
        public PoiBean startPoiBean;
        public PoiBean endPoiBean;
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_single;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            params = (SingleActivity.Params) savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                params = (SingleActivity.Params) bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }
        EventBus.getDefault().register(this);
        initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        GuideCalendarUtils.getInstance().onDestory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (params != null) {
            outState.putSerializable(Constants.PARAMS_DATA, params);
        }
    }

    private void initView() {
        titlebar.setTitleBarBackListener(this);
        titlebar.setRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showServiceDialog();
            }
        });

        if (params != null) {
            if (params.guidesDetailData != null) {
                guidesDetailData = params.guidesDetailData;
                guideLayout.setData(guidesDetailData);
                if (!TextUtils.isEmpty("" + guidesDetailData.cityId)) {
                    setCityBean(DBHelper.findCityById("" + guidesDetailData.cityId));
                    guideCityId = guidesDetailData.cityId;
                }
                carTypeView.setGuidesDetailData(guidesDetailData);
                GuideCalendarUtils.getInstance().sendRequest(this, guidesDetailData.guideId, ORDER_TYPE);
                guidanceLayout.setVisibility(View.GONE);
            } else {
                guidanceLayout.setVisibility(View.VISIBLE);
                guideLayout.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(params.cityId)) {
                setCityBean(DBHelper.findCityById(params.cityId));
            }
            if (params.startPoiBean != null) {
                setStartPoiBean(params.startPoiBean);
            }
            if (params.endPoiBean != null){
                setEndPoiBean(params.endPoiBean);
            }
        }

        carTypeView.setOnSelectedCarListener(this);
        carTypeView.setOrderType(ORDER_TYPE);
        carTypeView.showLuggageExplain();
        bottomView.setOnConfirmListener(this);
        addressLayout.setOnAddressClickListener(this);

        emptyLayout.setOnClickServicesListener(new SkuOrderEmptyView.OnClickServicesListener() {
            @Override
            public void onClickServices() {
                showServiceDialog();
            }
        });
        emptyLayout.setOnRefreshDataListener(new SkuOrderEmptyView.OnRefreshDataListener() {
            @Override
            public void onRefresh() {
                scrollToTop();
                getCars();
            }
        });
        emptyLayout.setOnClickCharterListener(new SkuOrderEmptyView.OnClickCharterListener() {
            @Override
            public void onClickCharter() {
                IntentUtils.intentCharterActivity(SingleActivity.this, getEventSource());
            }
        });
        updateConponsTipView();

        setSensorsBuyRouteEvent();
    }

    @OnClick({R.id.single_city_layout, R.id.single_time_layout})
    public void onClick(View view) {
        setSensorsOnOperated();
        Intent intent;
        switch (view.getId()) {
            case R.id.single_city_layout:
                if (guidesDetailData != null) {
                    intent = new Intent(this, ChooseGuideCityActivity.class);
                    intent.putExtra(Constants.PARAMS_ID, guidesDetailData.guideId);
                    intent.putExtra(Constants.PARAMS_TAG, TAG);
                    intent.putExtra(Constants.PARAMS_DATA, DatabaseManager.getCityBean("" + guidesDetailData.cityId));
                    intent.putExtra(Constants.PARAMS_SOURCE, getReferH5EventSource(getEventSource()));
                    startActivity(intent);
                } else {
                    intent = new Intent(this, ChooseCityActivity.class);
                    intent.putExtra(ChooseCityActivity.KEY_FROM_TAG, TAG);
                    intent.putExtra(Constants.PARAMS_SOURCE, getReferH5EventSource(getEventSource()));
                    intent.putExtra(KEY_BUSINESS_TYPE, ORDER_TYPE);
                    startActivity(intent);
                }
                SensorsUtils.onAppClick(getEventSource(),"用车城市",getIntentSource());
                break;
            case R.id.single_time_layout:
                if (cityBean == null) {
                    CommonUtils.showToast(R.string.single_check_city_toast);
                } else if (startPoiBean == null) {
                    CommonUtils.showToast(R.string.single_start_address_toast);
                } else if (endPoiBean == null) {
                    CommonUtils.showToast(R.string.single_end_address_toast);
                } else {
                    showTimePicker();
                }
                SensorsUtils.onAppClick(getEventSource(),"出发时间",getIntentSource());
                break;
        }
    }

    public void setCityBean(CityBean _cityBean) {
        if (_cityBean == null || (cityBean != null && cityBean.cityId == _cityBean.cityId)) {
            return;
        }
        cityBean = _cityBean;
        cityLayout.setDesc(_cityBean.name);

        emptyLayout.setVisibility(View.GONE);
        carTypeView.setVisibility(View.GONE);
        bottomView.setVisibility(View.GONE);
        addressLayout.resetUI();
        startPoiBean = null;
        endPoiBean = null;
        hintConponsTipView();
        if (params == null || params.guidesDetailData == null) {
            guidanceLayout.setData("" + cityBean.cityId, cityBean.name);
        }
    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case CHOOSE_START_CITY_BACK://选择城市返回
            case CHOOSE_GUIDE_CITY_BACK://指定司导选城市返回
                CityBean _cityBean = null;
                if (action.getType() == EventType.CHOOSE_START_CITY_BACK) {
                    _cityBean = (CityBean) action.getData();
                    if (!SingleActivity.TAG.equals(_cityBean.fromTag)) {
                        break;
                    }
                } else {
                    ChooseGuideCityActivity.GuideServiceCitys guideServiceCitys = (ChooseGuideCityActivity.GuideServiceCitys) action.getData();
                    if (!SingleActivity.TAG.equals(guideServiceCitys.sourceTag)) {
                        break;
                    }
                    _cityBean = guideServiceCitys.getSelectedCityBean();
                }
                setCityBean(_cityBean);
                break;
            case CHOOSE_POI_BACK:
                PoiBean poiBean = (PoiBean) action.getData();
                if ("from".equals(poiBean.type)) {
                    if (poiBean == null || (startPoiBean != null && TextUtils.equals(poiBean.placeName, startPoiBean.placeName))) {
                        break;
                    }
                    setStartPoiBean(poiBean);
                } else if ("to".equals(poiBean.type)) {
                    if (poiBean == null || (endPoiBean != null && TextUtils.equals(poiBean.placeName, endPoiBean.placeName))) {
                        break;
                    }
                    setEndPoiBean(poiBean);
                }
                getCars();
                break;
            case ORDER_REFRESH://价格或数量变更 刷新
                scrollToTop();
                getCars();
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

    public void setStartPoiBean(PoiBean poiBean) {
        startPoiBean = poiBean;
        addressLayout.setStartAddress(startPoiBean.placeName, startPoiBean.placeDetail);
    }

    public void setEndPoiBean(PoiBean poiBean) {
        endPoiBean = poiBean;
        addressLayout.setEndAddress(endPoiBean.placeName, endPoiBean.placeDetail);
    }

    public void showServiceDialog() {
        csDialog = CommonUtils.csDialog(SingleActivity.this, null, null, null, UnicornServiceActivity.SourceType.TYPE_CHARTERED, getEventSource(), new CsDialog.OnCsListener() {
            @Override
            public void onCs() {
                if (csDialog != null && csDialog.isShowing()) {
                    csDialog.dismiss();
                }
            }
        });
    }

    public void showTimePicker() {
        Intent intent = new Intent(this, DatePickerActivity.class);
        if (guidesDetailData != null) {
            intent.putExtra(DatePickerActivity.PARAM_ASSIGN_GUIDE, true);
        }
        intent.putExtra(Constants.PARAMS_ORDER_TYPE, Constants.BUSINESS_TYPE_RENT);
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
        this.startActivity(intent);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RequestCheckPriceForSingle) {
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
        if (isFinishing()) {
            return;
        }
        if (request.errorType != BaseRequest.ERROR_TYPE_PROCESSED && request instanceof RequestCheckPriceForSingle) {
            String errorCode = ErrorHandler.getErrorCode(errorInfo, request);
            String errorMessage = getString(R.string.single_errormessage);
            checkDataIsEmpty(null, 0, String.format(errorMessage, errorCode));
            return;
        } else {
            emptyLayout.setErrorVisibility(View.VISIBLE);
            setItemVisibility(View.GONE);
        }
    }

    @Override
    public void onStartAddressClick() {
        setSensorsOnOperated();
        intentPoiSearch("from");
        SensorsUtils.onAppClick(getEventSource(),"出发地",getIntentSource());
    }

    @Override
    public void onEndAddressClick() {
        setSensorsOnOperated();
        intentPoiSearch("to");
        SensorsUtils.onAppClick(getEventSource(),"目的地",getIntentSource());
    }

    private void intentPoiSearch(String keyFrom) {
        if (cityBean == null) {
            CommonUtils.showToast(R.string.single_check_city_toast);
        } else {
            Intent intent = new Intent(activity,PoiSearchActivity.class);
            intent.putExtra(KEY_FROM, keyFrom);
            intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
            intent.putExtra(PoiSearchActivity.KEY_CITY_ID, cityBean.cityId);
            intent.putExtra(PoiSearchActivity.KEY_LOCATION, cityBean.location);
            intent.putExtra(PoiSearchActivity.PARAM_BUSINESS_TYPE, ORDER_TYPE);
            startActivity(intent);
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

    @Override
    public boolean onTitleBarBack() {
        return isShowSaveDialog();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            if (isShowSaveDialog()) {
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    private boolean isShowSaveDialog() {
        if ((cityBean != null && cityBean.cityId != guideCityId) || startPoiBean != null || endPoiBean != null) {
            OrderUtils.showSaveDialog(this);
            return true;
        } else {
            return false;
        }
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
        if (cityBean == null || startPoiBean == null || endPoiBean == null || TextUtils.isEmpty(serverDate) || TextUtils.isEmpty(serverTime)) {
            return;
        }
        if (guidesDetailData != null) {
            getGuideCars();
        } else {
            requestCarPriceList();
        }
    }

    private void requestCarPriceList() {
        RequestCheckPriceForSingle requestCheckPriceForSingle = new RequestCheckPriceForSingle(this
                , ORDER_TYPE
                , cityBean.cityId
                , startPoiBean.location
                , endPoiBean.location
                , serverDate + " " + serverTime
                , guidesDetailData == null ? "" : guidesDetailData.getCarIds()
                , guidesDetailData == null ? 0 : guidesDetailData.isQuality);
        requestData(requestCheckPriceForSingle);
    }

    @Override
    public void onConfirm() {
        if (!CommonUtils.isLogin(this,getEventSource())) {
            return;
        }
        if (guidesDetailData != null) {
            checkGuideCoflict();
        } else {
            initOrderActivity();
        }
        SensorsUtils.onAppClick(getEventSource(),"下一步",getIntentSource());
    }

    public void initOrderActivity() {
        OrderActivity.Params orderParams = new OrderActivity.Params();
        orderParams.startPoiBean = startPoiBean;
        orderParams.endPoiBean = endPoiBean;
        orderParams.carListBean = carListBean;
        orderParams.carBean = carBean;
        orderParams.cityBean = cityBean;
        orderParams.orderType = ORDER_TYPE;
        orderParams.serverDate = serverDate;
        orderParams.serverTime = serverTime;
        if (guidesDetailData != null) {
            orderParams.guidesDetailData = guidesDetailData;
        }
        Intent intent = new Intent(this, OrderActivity.class);
        intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
        intent.putExtra(Constants.PARAMS_DATA, orderParams);
        startActivity(intent);

        setSensorsConfirmEvent();
        StatisticClickEvent.singleSkuClick(StatisticConstant.CONFIRM_C, source, carBean.desc + "");
    }

    private void getGuideCars() {
        RequestNewCars requestCars = new RequestNewCars(this, ORDER_TYPE, guidesDetailData.guideId, null);
        HttpRequestUtils.request(this, requestCars, new HttpRequestListener() {
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
                if (isFinishing()) {
                    return;
                }
                if (request.errorType != BaseRequest.ERROR_TYPE_PROCESSED) {
                    checkDataIsEmpty(null, ErrorHandler.getServerErrorCode(errorInfo, request), ErrorHandler.getErrorMessage(errorInfo, request));
                }
            }
        }, true);
    }

    private void checkGuideCoflict() {
        RequestCheckGuide.CheckGuideBean checkGuideBean = new RequestCheckGuide.CheckGuideBean();
        checkGuideBean.startTime = serverDate + " " + serverTime + ":00";
        checkGuideBean.endTime = DateUtils.getDifferenceTime2(checkGuideBean.startTime, CommonUtils.getCountInteger(carListBean.estTime) * 60 * 1000);
        checkGuideBean.cityId = cityBean.cityId;
        checkGuideBean.guideId = guidesDetailData.guideId;
        checkGuideBean.orderType = ORDER_TYPE;

        RequestCheckGuide requestCheckGuide = new RequestCheckGuide(this, checkGuideBean);
        HttpRequestUtils.request(this, requestCheckGuide, new HttpRequestListener() {
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
                if (isFinishing()) {
                    return;
                }
                CommonUtils.apiErrorShowService(SingleActivity.this, errorInfo, request, SingleActivity.this.getEventSource(), false);
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
        if (emptyLayout.getVisibility() == View.VISIBLE || carTypeView.getVisibility() == View.VISIBLE || carListBean != null) {
            conponsTipView.setVisibility(View.GONE);
        } else {
            conponsTipView.showView();
        }
    }

    @Override
    public String getEventSource() {
        return "单次";
    }

    @Override
    public String getEventId() {
        return StatisticConstant.LAUNCH_C;
    }

    //神策统计_确认行程
    private void setSensorsConfirmEvent() {
        try {
            double total = carBean.price;
            JSONObject properties = new JSONObject();
            properties.put("hbc_sku_type", "单次");
            properties.put("hbc_guide_id", null != guidesDetailData ? guidesDetailData.guideId : "");// 指定司导下单
            properties.put("hbc_car_type", carBean.desc);//车型选择
            properties.put("hbc_price_total", total);//费用总计
            properties.put("hbc_distance", carListBean.distance);// 全程公里数
            properties.put("hbc_geton_time", serverDate + " " + serverTime);// 出发时间
            properties.put("hbc_geton_location", startPoiBean.placeName);// 出发地
            properties.put("hbc_dest_location", endPoiBean.placeName);// 送达地
            properties.put("hbc_service_city", cityBean.name);// 用车城市
            SensorsDataAPI.sharedInstance(this).track("buy_confirm", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //神策统计_下单-有操作
    private void setSensorsOnOperated() {
        if (isOperated) {
            isOperated = false;
            SensorsUtils.onOperated(getIntentSource(), getEventSource());
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
            properties.put("hbc_refer", getIntentSource());
            properties.put("hbc_sku_type", "单次");
            properties.put("hbc_guide_id", guidesDetailData != null ? guidesDetailData.guideId : "");
            SensorsDataAPI.sharedInstance(this).track("buy_route", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
