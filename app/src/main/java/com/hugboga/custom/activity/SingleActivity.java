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
import com.hugboga.custom.data.bean.CityBean;
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
import com.hugboga.custom.utils.ApiReportHelper;
import com.hugboga.custom.utils.CarUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.DBHelper;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.utils.OrderUtils;
import com.hugboga.custom.widget.DialogUtil;
import com.hugboga.custom.widget.OrderBottomView;
import com.hugboga.custom.widget.OrderGuideLayout;
import com.hugboga.custom.widget.OrderInfoItemView;
import com.hugboga.custom.widget.SendAddressView;
import com.hugboga.custom.widget.SkuOrderCarTypeView;
import com.hugboga.custom.widget.SkuOrderEmptyView;
import com.hugboga.custom.widget.title.TitleBar;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.sensorsdata.analytics.android.sdk.exceptions.InvalidDataException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DateTimePicker;

/**
 * Created by qingcha on 17/5/23.
 */
public class SingleActivity extends BaseActivity implements SendAddressView.OnAddressClickListener
        , SkuOrderCarTypeView.OnSelectedCarListener, OrderBottomView.OnConfirmListener, TitleBar.OnTitleBarBackListener{

    public static final String TAG = SingleActivity.class.getSimpleName();
    private static final int ORDER_TYPE = 4;

    @Bind(R.id.single_titlebar)
    TitleBar titlebar;
    @Bind(R.id.single_bottom_view)
    OrderBottomView bottomView;
    @Bind(R.id.single_guide_layout)
    OrderGuideLayout guideLayout;
    @Bind(R.id.single_city_layout)
    OrderInfoItemView cityLayout;
    @Bind(R.id.single_address_layout)
    SendAddressView addressLayout;
    @Bind(R.id.single_time_layout)
    OrderInfoItemView timeLayout;
    @Bind(R.id.single_car_type_view)
    SkuOrderCarTypeView carTypeView;
    @Bind(R.id.single_empty_layout)
    SkuOrderEmptyView emptyLayout;
    @Bind(R.id.single_scrollview)
    ScrollView scrollView;

    private DateTimePicker dateTimePicker;

    private CarListBean carListBean;
    private CarBean carBean;
    private CityBean cityBean;
    private PoiBean startPoiBean, endPoiBean;
    private String serverDate;
    private String serverTime;

    private GuidesDetailData guidesDetailData;
    private ArrayList<GuideCarBean> guideCarBeanList;

    private SingleActivity.Params params;

    public static class Params implements Serializable {
        public GuidesDetailData guidesDetailData;
        public String cityId;
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
                }
            }
            if (!TextUtils.isEmpty(params.cityId)) {
                setCityBean(DBHelper.findCityById(params.cityId));
            }
        }

        carTypeView.setOnSelectedCarListener(this);
        bottomView.setOnConfirmListener(this);
        carTypeView.setOrderType(ORDER_TYPE);
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
        emptyLayout.setonClickCharterListener(new SkuOrderEmptyView.OnClickCharterListener() {
            @Override
            public void onClickCharter() {
                Intent intent = new Intent(SingleActivity.this, CharterFirstStepActivity.class);
                intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                SingleActivity.this.startActivity(intent);
            }
        });
        setSensorsEvent();
    }

    @OnClick({R.id.single_city_layout, R.id.single_time_layout})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.single_city_layout:
                if (guidesDetailData != null) {
                    intent = new Intent(this, ChooseGuideCityActivity.class);
                    intent.putExtra(Constants.PARAMS_ID, guidesDetailData.guideId);
                    intent.putExtra(Constants.PARAMS_TAG, TAG);
                    startActivity(intent);
                } else {
                    intent = new Intent(this, ChooseCityActivity.class);
                    intent.putExtra(ChooseCityActivity.KEY_FROM_TAG, TAG);
                    intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                    intent.putExtra(KEY_BUSINESS_TYPE, ORDER_TYPE);
                    startActivity(intent);
                }
                break;
            case R.id.single_time_layout:
                if (cityBean == null) {
                    CommonUtils.showToast("请先选择城市");
                } else {
                    showTimePicker();
                }
                break;
        }
    }

    public boolean setCityBean(CityBean _cityBean) {
        boolean isBreak = false;
        if (_cityBean == null || (cityBean != null && cityBean.cityId == _cityBean.cityId)) {
            isBreak = true;
        }
        cityBean = _cityBean;
        cityLayout.setDesc(_cityBean.name);

        emptyLayout.setVisibility(View.GONE);
        carTypeView.setVisibility(View.GONE);
        bottomView.setVisibility(View.GONE);
        addressLayout.resetUI();
        startPoiBean = null;
        endPoiBean = null;
        return isBreak;
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
                    startPoiBean = poiBean;
                    addressLayout.setStartAddress(startPoiBean.placeName, startPoiBean.placeDetail);
                    getCars();
                } else if ("to".equals(poiBean.type)) {
                    if (poiBean == null || (endPoiBean != null && TextUtils.equals(poiBean.placeName, endPoiBean.placeName))) {
                        break;
                    }
                    endPoiBean = poiBean;
                    addressLayout.setEndAddress(endPoiBean.placeName, endPoiBean.placeDetail);
                    getCars();
                }
                break;
            case ORDER_REFRESH://价格或数量变更 刷新
                scrollToTop();
                getCars();
        }
    }

    public void showServiceDialog() {
        DialogUtil.showServiceDialog(SingleActivity.this, null, UnicornServiceActivity.SourceType.TYPE_CHARTERED, null, null, getEventSource());
    }

    public void showTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        if (dateTimePicker == null) {
            dateTimePicker = new DateTimePicker(this, DateTimePicker.YEAR_MONTH_DAY);
            dateTimePicker.setRange(calendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR) + 1);
            dateTimePicker.setOnDateTimePickListener(new DateTimePicker.OnYearMonthDayTimePickListener() {
                @Override
                public void onDateTimePicked(String year, String month, String day, String hour, String minute) {
                    String tmpDate = year + "-" + month + "-" + day;
                    String startDate = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);

                    if (DateUtils.getDateByStr(tmpDate).before(DateUtils.getDateByStr(startDate))) {
                        CommonUtils.showToast("不能选择今天之前的时间");
                        return;
                    }
                    if (DateUtils.getDistanceDays(startDate,tmpDate) > 180) {
                        CommonUtils.showToast(R.string.time_out_180);
                    } else {
                        serverDate = tmpDate;
                        serverTime = hour + ":" + minute;
                        timeLayout.setDesc(DateUtils.getPointStrFromDate2(serverDate) + " " + serverTime);
                        getCars();
                        dateTimePicker.dismiss();
                    }
                }
            });
        }

        if (!TextUtils.isEmpty(serverDate) && !TextUtils.isEmpty(serverTime)) {
            try {
                calendar.setTime(DateUtils.dateTimeFormat2.parse(serverDate + " " + serverTime));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        dateTimePicker.setSelectedItem(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        dateTimePicker.setLineColor(0xffaaaaaa);
        dateTimePicker.show();
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
            }
        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        super.onDataRequestError(errorInfo, request);
        if (request.errorType != BaseRequest.ERROR_TYPE_PROCESSED && request instanceof RequestCheckPriceForSingle) {
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
    public void onStartAddressClick() {
        intentPoiSearch("from");
    }

    @Override
    public void onEndAddressClick() {
        intentPoiSearch("to");
    }

    private void intentPoiSearch(String keyFrom) {
        if (cityBean == null) {
            CommonUtils.showToast("请先选择城市");
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
        if (cityBean != null) {
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
        if (!CommonUtils.isLogin(this)) {
            return;
        }
        if (guidesDetailData != null) {
            checkGuideCoflict();
        } else {
            initOrderActivity();
        }
    }

    public void initOrderActivity() {
        setSensorsConfirmEvent();
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
                if (request.errorType != BaseRequest.ERROR_TYPE_PROCESSED) {
                    checkDataIsEmpty(null, 0, ErrorHandler.getErrorMessage(errorInfo, request));
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
                CommonUtils.apiErrorShowService(SingleActivity.this, errorInfo, request, SingleActivity.this.getEventSource());
            }
        }, true);
    }

    @Override
    public String getEventSource() {
        return "单次接送下单";
    }

    @Override
    public String getEventId() {
        return StatisticConstant.LAUNCH_C;
    }

    //神策统计_初始页浏览
    private void setSensorsEvent() {
        try {
            JSONObject properties = new JSONObject();
            properties.put("hbc_sku_type", "单次");
            properties.put("hbc_refer", source);
            SensorsDataAPI.sharedInstance(this).track("buy_view", properties);
        } catch (InvalidDataException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //神策统计_确认行程
    private void setSensorsConfirmEvent() {
        try {
            int total = carBean.price;
            JSONObject properties = new JSONObject();
            properties.put("hbc_sku_type", "单次接送");
            properties.put("hbc_is_appoint_guide", null != guidesDetailData ? true : false);// 指定司导下单
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

}
