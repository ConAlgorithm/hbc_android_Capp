package com.hugboga.custom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CarBean;
import com.hugboga.custom.data.bean.CarListBean;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.CollectGuideBean;
import com.hugboga.custom.data.bean.PoiBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestCheckPrice;
import com.hugboga.custom.data.request.RequestCheckPriceForSingle;
import com.hugboga.custom.utils.CommonUtils;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.ParseException;
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

    private DateTimePicker dateTimePicker;

    private CarListBean carListBean;
    private CarBean carBean;
    private CityBean cityBean;
    private PoiBean startPoiBean, endPoiBean;
    private String serverDate;
    private String serverTime;

    private CollectGuideBean collectGuideBean;
    private String carIds = null;

    @Override
    public int getContentViewId() {
        return R.layout.activity_single;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        titlebar.setTitleBarBackListener(this);
        titlebar.setRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtil.showServiceDialog(SingleActivity.this, null, UnicornServiceActivity.SourceType.TYPE_CHARTERED, null, null, getEventSource());
            }
        });

        carTypeView.setOnSelectedCarListener(this);
        bottomView.setOnConfirmListener(this);
        carTypeView.setOrderType(ORDER_TYPE);
        addressLayout.setOnAddressClickListener(this);
    }

    @OnClick({R.id.single_city_layout, R.id.single_time_layout})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.single_city_layout:
                if (collectGuideBean != null) {
                    intent = new Intent(this, ChooseGuideCityActivity.class);
                    intent.putExtra(Constants.PARAMS_ID, collectGuideBean.guideId);
                    startActivity(intent);
                } else {
                    intent = new Intent(this, ChooseCityActivity.class);
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

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case CHOOSE_START_CITY_BACK://选择城市返回
            case CHOOSE_GUIDE_CITY_BACK://指定司导选城市返回
                CityBean _cityBean = null;
                if (action.getType() == EventType.CHOOSE_START_CITY_BACK) {
                    _cityBean = (CityBean) action.getData();
                } else {
                    ChooseGuideCityActivity.GuideServiceCitys guideServiceCitys = (ChooseGuideCityActivity.GuideServiceCitys) action.getData();
                    _cityBean = guideServiceCitys.getSelectedCityBean();
                }
                if (_cityBean == null || (cityBean != null && cityBean.cityId == _cityBean.cityId)) {
                    break;
                }
                cityBean = _cityBean;
                cityLayout.setDesc(_cityBean.name);

                emptyLayout.setVisibility(View.GONE);
                carTypeView.setVisibility(View.GONE);
                bottomView.setVisibility(View.GONE);
                addressLayout.resetUI();
                startPoiBean = null;
                endPoiBean = null;
                break;
            case CHOOSE_POI_BACK:
                PoiBean poiBean = (PoiBean) action.getData();
                if ("from".equals(poiBean.type)) {
                    if (poiBean == null || (startPoiBean != null && TextUtils.equals(poiBean.placeName, startPoiBean.placeName))) {
                        break;
                    }
                    startPoiBean = poiBean;
                    addressLayout.setStartAddress(startPoiBean.placeName, startPoiBean.placeDetail);
                    requestCarPriceList();
                } else if ("to".equals(poiBean.type)) {
                    if (poiBean == null || (endPoiBean != null && TextUtils.equals(poiBean.placeName, endPoiBean.placeName))) {
                        break;
                    }
                    endPoiBean = poiBean;
                    addressLayout.setEndAddress(endPoiBean.placeName, endPoiBean.placeDetail);
                    requestCarPriceList();
                }
                break;
        }
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
                        requestCarPriceList();
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
        dateTimePicker.show();
    }

    private void requestCarPriceList() {
        if (cityBean == null || startPoiBean == null || endPoiBean == null || TextUtils.isEmpty(serverDate) || TextUtils.isEmpty(serverTime)) {
            return;
        }
        RequestCheckPriceForSingle requestCheckPriceForSingle = new RequestCheckPriceForSingle(this
                , ORDER_TYPE
                , cityBean.cityId
                , startPoiBean.location
                , endPoiBean.location
                , serverDate + " " + serverTime
                , carIds
                , collectGuideBean == null ? 0 : collectGuideBean.isQuality);
        requestData(requestCheckPriceForSingle);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RequestCheckPriceForSingle) {
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
    public void onConfirm() {
        if (!CommonUtils.isLogin(this)) {
            return;
        }
        OrderActivity.Params params = new OrderActivity.Params();
        params.startPoiBean = startPoiBean;
        params.endPoiBean = endPoiBean;
        params.carListBean = carListBean;
        params.carBean = carBean;
        params.cityBean = cityBean;
        params.orderType = ORDER_TYPE;
        params.serverDate = serverDate;
        params.serverTime = serverTime;
        Intent intent = new Intent(this, OrderActivity.class);
        intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
        intent.putExtra(Constants.PARAMS_DATA, params);
        startActivity(intent);
    }

    @Override
    public String getEventSource() {
        return "单次接送下单";
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
}
