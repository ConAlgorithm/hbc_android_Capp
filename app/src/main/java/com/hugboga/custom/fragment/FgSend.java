package com.hugboga.custom.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.ChooseAirPortActivity;
import com.hugboga.custom.activity.OrderActivity;
import com.hugboga.custom.activity.PoiSearchActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.AirPort;
import com.hugboga.custom.data.bean.CarBean;
import com.hugboga.custom.data.bean.CarListBean;
import com.hugboga.custom.data.bean.CollectGuideBean;
import com.hugboga.custom.data.bean.PoiBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.request.RequestCheckPrice;
import com.hugboga.custom.data.request.RequestCheckPriceForTransfer;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.DBHelper;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.widget.OrderBottomView;
import com.hugboga.custom.widget.OrderGuideLayout;
import com.hugboga.custom.widget.OrderInfoItemView;
import com.hugboga.custom.widget.SkuOrderCarTypeView;
import com.hugboga.custom.widget.SkuOrderEmptyView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.ParseException;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DateTimePicker;

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

    private DateTimePicker dateTimePicker;

    private AirPort airPortBean;
    private PoiBean poiBean;
    private String serverDate;
    private String serverTime;
    private CarListBean carListBean;
    private CarBean carBean;

    private CollectGuideBean collectGuideBean;
    private String carIds = null;

    @Override
    public int getContentViewId() {
        return R.layout.fg_send;
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
        carTypeView.setOrderType(ORDER_TYPE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
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
                if (collectGuideBean != null) {
                    intent.putExtra(ChooseAirPortActivity.KEY_CITY_ID, collectGuideBean.cityId);
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
                if (_airPortBean == null || (airPortBean != null && TextUtils.equals(_airPortBean.airportName, airPortBean.airportName))) {
                    break;
                }
                airPortBean = _airPortBean;
                airportLayout.setDesc(airPortBean.airportName);

                emptyLayout.setVisibility(View.GONE);
                carTypeView.setVisibility(View.GONE);
                bottomView.setVisibility(View.GONE);
                startPoiLayout.resetUI();
                poiBean = null;
                break;
            case CHOOSE_POI_BACK:
                PoiBean _poiBean = (PoiBean) action.getData();
                if (_poiBean == null || _poiBean.mBusinessType != ORDER_TYPE || (poiBean != null && TextUtils.equals(_poiBean.placeName, poiBean.placeName))) {
                    break;
                }
                poiBean = _poiBean;
                startPoiLayout.setDesc(poiBean.placeName, poiBean.placeDetail);
                requestCarPriceList();
                break;
        }
    }

    public void showTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        if (dateTimePicker == null) {
            dateTimePicker = new DateTimePicker(getActivity(), DateTimePicker.HOUR_OF_DAY);
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

                    if (DateUtils.getDistanceDays(startDate, tmpDate) > 180) {
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
        if (airPortBean == null || poiBean == null || TextUtils.isEmpty(serverDate) || TextUtils.isEmpty(serverTime)) {
            return;
        }
        RequestCheckPriceForTransfer requestCheckPriceForTransfer = new RequestCheckPriceForTransfer(getActivity()
                , ORDER_TYPE
                , airPortBean.airportCode
                , airPortBean.cityId
                , poiBean.location
                , airPortBean.location
                , serverDate + " " + serverTime
                , carIds
                , collectGuideBean == null ? 0 : collectGuideBean.isQuality);
        requestData(requestCheckPriceForTransfer);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RequestCheckPriceForTransfer) {
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
        OrderActivity.Params params = new OrderActivity.Params();
        params.airPortBean = airPortBean;
        params.startPoiBean = poiBean;
        params.carListBean = carListBean;
        params.carBean = carBean;
        params.cityBean = DBHelper.findCityById("" + airPortBean.cityId);
        params.orderType = ORDER_TYPE;
        params.serverDate = serverDate;
        params.serverTime = serverTime;
        Intent intent = new Intent(getContext(), OrderActivity.class);
        intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
        intent.putExtra(Constants.PARAMS_DATA, params);
        startActivity(intent);
    }

    @Override
    public String getEventSource() {
        return "送机下单";
    }
}
