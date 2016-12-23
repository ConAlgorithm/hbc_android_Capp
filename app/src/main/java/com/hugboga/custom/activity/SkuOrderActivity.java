package com.hugboga.custom.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.AreaCodeBean;
import com.hugboga.custom.data.bean.CarBean;
import com.hugboga.custom.data.bean.CarListBean;
import com.hugboga.custom.data.bean.ChooseDateBean;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.ContactUsersBean;
import com.hugboga.custom.data.bean.CouponBean;
import com.hugboga.custom.data.bean.DeductionBean;
import com.hugboga.custom.data.bean.ManLuggageBean;
import com.hugboga.custom.data.bean.MostFitAvailableBean;
import com.hugboga.custom.data.bean.MostFitBean;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.OrderInfoBean;
import com.hugboga.custom.data.bean.PoiBean;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.request.RequestCancleTips;
import com.hugboga.custom.data.request.RequestDeduction;
import com.hugboga.custom.data.request.RequestMostFit;
import com.hugboga.custom.data.request.RequestPayNo;
import com.hugboga.custom.data.request.RequestPriceSku;
import com.hugboga.custom.data.request.RequestSubmitBase;
import com.hugboga.custom.data.request.RequestSubmitDaily;
import com.hugboga.custom.data.request.RequestSubmitLine;
import com.hugboga.custom.statistic.bean.EventPayBean;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.AlertDialogUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.OrderUtils;
import com.hugboga.custom.utils.PhoneInfo;
import com.hugboga.custom.widget.OrderExplainView;
import com.hugboga.custom.widget.SkuOrderBottomView;
import com.hugboga.custom.widget.SkuOrderCarTypeView;
import com.hugboga.custom.widget.SkuOrderChooseDateView;
import com.hugboga.custom.widget.SkuOrderCountView;
import com.hugboga.custom.widget.SkuOrderDescriptionView;
import com.hugboga.custom.widget.SkuOrderDiscountView;
import com.hugboga.custom.widget.SkuOrderTravelerInfoView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 16/12/16.
 */
public class SkuOrderActivity extends BaseActivity implements SkuOrderChooseDateView.OnSelectedDateListener
        , SkuOrderCarTypeView.OnSelectedCarListener, SkuOrderDiscountView.DiscountOnClickListener
        , SkuOrderCountView.OnCountChangeListener, SkuOrderBottomView.OnSubmitOrderListener{

    private static final String SERVER_TIME = "09:00";
    public static final int REQUEST_CODE_PICK_CONTACTS = 101;

    @Bind(R.id.sku_order_scrollview)
    ScrollView scrollView;
    @Bind(R.id.sku_order_description_view)
    SkuOrderDescriptionView descriptionView;
    @Bind(R.id.sku_order_choose_date_view)
    SkuOrderChooseDateView chooseDateView;
    @Bind(R.id.sku_order_car_type_view)
    SkuOrderCarTypeView carTypeView;
    @Bind(R.id.sku_order_count_view)
    SkuOrderCountView countView;
    @Bind(R.id.sku_order_traveler_info_view)
    SkuOrderTravelerInfoView travelerInfoView;
    @Bind(R.id.sku_order_discount_view)
    SkuOrderDiscountView discountView;
    @Bind(R.id.sku_order_bottom_view)
    SkuOrderBottomView bottomView;
    @Bind(R.id.sku_order_explain_view)
    OrderExplainView explainView;
    @Bind(R.id.sku_order_empty_layout)
    LinearLayout emptyLayout;

    private SkuOrderActivity.Params params;
    private CarListBean carListBean;
    private CarBean carBean;
    private String serverDate;
    private int orderType;
    private OrderInfoBean orderInfoBean;
    private OrderBean orderBean;

    private DeductionBean deductionBean;

    private MostFitBean mostFitBean;
    private CouponBean couponBean;
    private String couponId;

    public static class Params implements Serializable {
        public SkuItemBean skuItemBean;
        public CityBean cityBean;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            params = (SkuOrderActivity.Params) savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                params = (SkuOrderActivity.Params) bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }
        setContentView(R.layout.activity_sku_order);
        ButterKnife.bind(this);
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
        if (params == null || params.skuItemBean == null || params.cityBean == null) {
            finish();
            return;
        }

        orderType = params.skuItemBean.goodsClass == 1 ? 5 : 6;

        initTitleBar();

        descriptionView.update(params.skuItemBean);
        chooseDateView.setOnSelectedDateListener(this);
        carTypeView.setOnSelectedCarListener(this);
        discountView.setDiscountOnClickListener(this);
        countView.setOnCountChangeListener(this);
        bottomView.setOnSubmitOrderListener(this);
        chooseDateView.setStartDate("2016-12-24");//TODO
        explainView.setTermsLayoutVisibility(View.VISIBLE);
    }

    public void initTitleBar() {
        initDefaultTitleBar();
        fgTitle.setText("确认订单");
        fgLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftInput();
                AlertDialogUtils.showAlertDialog(SkuOrderActivity.this, "订单未填写完，要离开吗?", "离开", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            }
        });
        fgRightBtn.setVisibility(View.GONE);
    }

    @Subscribe
    public void onEventMainThread(final EventAction action) {
        switch (action.getType()) {
            case CHOOSE_DATE:
                ChooseDateBean chooseDateBean = (ChooseDateBean) action.getData();
                if (chooseDateBean.type == 3) {
                    chooseDateView.setChooseDateBean(chooseDateBean);
                }
                break;
            case CHOOSE_COUNTRY_BACK:
                AreaCodeBean areaCodeBean = (AreaCodeBean) action.getData();
                travelerInfoView.setAreaCode(areaCodeBean.getCode());
                break;
            case CHOOSE_POI_BACK:
                PoiBean poiBean = (PoiBean) action.getData();
                travelerInfoView.setPlace(poiBean);
                break;
            case CHOOSE_POI:
                Bundle bundle = new Bundle();
                if (params.cityBean != null) {
                    bundle.putInt(PoiSearchActivity.KEY_CITY_ID, params.cityBean.cityId);
                    bundle.putString(PoiSearchActivity.KEY_LOCATION, params.cityBean.location);
                }
                Intent intent = new Intent(activity, PoiSearchActivity.class);
                intent.putExtras(bundle);
                intent.putExtra("mBusinessType", orderType);
                startActivity(intent);
                break;
            case SELECT_COUPON_BACK:
                couponBean = (CouponBean) action.getData();
                if (couponBean.couponID.equalsIgnoreCase(couponId)) {
                    couponId = null;
                    couponBean = null;
                }
                mostFitBean = null;
                Log.i("aa", "SELECT_COUPON_BACK 清空");
                discountView.setCouponBean(couponBean);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (REQUEST_CODE_PICK_CONTACTS == requestCode) {
            Uri result = data.getData();
            String[] contact = PhoneInfo.getPhoneContacts(this, result);
            if (contact == null || contact.length < 2) {
                return;
            }
            travelerInfoView.setTravelerName(contact[0]);
            String phone = contact[1];
            if (!TextUtils.isEmpty(phone)) {
                phone = phone.replace("+86", "");//此处拷贝自以前代码。。。
            }
            travelerInfoView.setTravelerPhone(phone);
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        super.onDataRequestSucceed(_request);
        if (_request instanceof RequestPriceSku) {
            carListBean = ((RequestPriceSku) _request).getData();
            if (!checkDataIsEmpty(carListBean)) {
                carTypeView.update(carListBean);
            }
        } else if (_request instanceof RequestMostFit) {
            mostFitBean = ((RequestMostFit) _request).getData();
            discountView.setMostFitBean(mostFitBean);
            Log.i("aa", "onDataRequestSucceed mostFitBean");
        } else if (_request instanceof RequestDeduction) {
            deductionBean = ((RequestDeduction) _request).getData();
            discountView.setDeductionBean(deductionBean);
        } else if (_request instanceof RequestSubmitBase) {
            orderInfoBean = ((RequestSubmitBase) _request).getData();
            if (orderInfoBean.getPriceActual() == 0) {
                requestPayNo(orderInfoBean.getOrderno());
            } else {
                ChoosePaymentActivity.RequestParams requestParams = new ChoosePaymentActivity.RequestParams();
                requestParams.couponId = couponId;
                requestParams.orderId = orderInfoBean.getOrderno();
                requestParams.shouldPay = orderInfoBean.getPriceActual();
                requestParams.payDeadTime = orderInfoBean.getPayDeadTime();
                requestParams.source = source;
                requestParams.needShowAlert = true;
                requestParams.eventPayBean = getChoosePaymentStatisticParams();
                Intent intent = new Intent(activity, ChoosePaymentActivity.class);
                intent.putExtra(Constants.PARAMS_DATA, requestParams);
                intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                startActivity(intent);
            }
        } else if (_request instanceof RequestPayNo) {
            RequestPayNo mParser = (RequestPayNo) _request;
            if (mParser.payType == Constants.PAY_STATE_ALIPAY) {
                if ("travelFundPay".equals(mParser.getData()) || "couppay".equals(mParser.getData())) {
                    PayResultActivity.Params params = new PayResultActivity.Params();
                    params.payResult = true;
                    params.orderId =  orderInfoBean.getOrderno();
                    Intent intent = new Intent(this, PayResultActivity.class);
                    intent.putExtra(Constants.PARAMS_DATA, params);
                    startActivity(intent);
                    SensorsUtils.setSensorsPayResultEvent(getChoosePaymentStatisticParams(), "支付宝", true);
                }
            }
        } else if (_request instanceof RequestCancleTips) {
            String cancleTips = "";
            List<String> datas = (List<String>) _request.getData();
            for (String str : datas) {
                cancleTips += str + "\n";
            }
            explainView.setCancleTips(cancleTips);
        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        super.onDataRequestError(errorInfo, request);
    }

    private boolean checkDataIsEmpty(CarListBean carListBean) {
        boolean isEmpty = false;
        if (carListBean == null || carListBean.carList == null || carListBean.carList.size() <= 0) {
            isEmpty = true;
        } else {
            isEmpty = false;
        }
        emptyLayout.setVisibility(isEmpty ? View.VISIBLE : View.GONE);

        int itemVisibility = !isEmpty ? View.VISIBLE : View.GONE;
        carTypeView.setVisibility(itemVisibility);
        countView.setVisibility(itemVisibility);
        travelerInfoView.setVisibility(itemVisibility);
        discountView.setVisibility(itemVisibility);
        bottomView.setVisibility(itemVisibility);
        explainView.setVisibility(itemVisibility);

        return isEmpty;
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

    /* 选择日期 */
    @Override
    public void onSelectedDate(String date) {
        requestPriceSku(date);
        scrollToTop();
    }

    /* 选择车辆 */
    @Override
    public void onSelectedCar(CarBean carBean) {
        this.carBean = carBean;
        countView.update(carBean, carListBean, serverDate);
        int additionalPrice = countView.getAdditionalPrice();
        requestMostFit(additionalPrice);
        requestTravelFund(additionalPrice);
        requestCancleTips();
    }

    /* 是否点击更多车型 */
    @Override
    public void onClickHideMoreCar(boolean isShow) {
        if (!isShow) {
            scrollToTop();
        }
    }

    /* 出行人数改变 */
    @Override
    public void onCountChange(ManLuggageBean bean) {
        if (bean == null) {
            return;
        }
        discountView.setInsuranceCount(bean.mans + bean.childs);
    }

    /* 儿童座椅+酒店价格发生改变 */
    @Override
    public void onAdditionalPriceChange(int price) {
        requestMostFit(price);
        requestTravelFund(price);
    }

    /* 选择优惠方式 */
    @Override
    public void chooseDiscount(int type) {
        if (carBean == null) {
            return;
        }
        final int additionalPrice = countView.getAdditionalPrice();
        int totalPrice = carBean.price + additionalPrice;
        int actualPrice = totalPrice;
        int deductionPrice = 0;

        switch (type) {
            case SkuOrderDiscountView.TYPE_COUPON:
                if (mostFitBean == null || mostFitBean.actualPrice == null  ||  mostFitBean.actualPrice == 0) {
                    if (couponBean != null) {
                        actualPrice = couponBean.actualPrice.intValue();
                        Log.i("aa", "chooseDiscount  111");
                    }
                } else {
                    Log.i("aa", "chooseDiscount  222222");
                    actualPrice = mostFitBean.actualPrice.intValue();
                }
                deductionPrice = totalPrice - actualPrice > 0 ? totalPrice - actualPrice : 0;
                break;
            case SkuOrderDiscountView.TYPE_TRAVEL_FUND:
                if (deductionBean != null && deductionBean.priceToPay != null) {
                    deductionPrice = CommonUtils.getCountInteger(deductionBean.deduction);
                    actualPrice = carBean.price - deductionPrice + additionalPrice;
                }
                break;
            case SkuOrderDiscountView.TYPE_INVALID:
                break;
        }
        bottomView.updatePrice(actualPrice, deductionPrice);
    }

    /* 进入优惠券列表 */
    @Override
    public void intentCouponList() {
        Bundle bundle = new Bundle();
        MostFitAvailableBean mostFitAvailableBean = new MostFitAvailableBean();
        mostFitAvailableBean.carSeatNum = carBean.seatCategory + "";
        mostFitAvailableBean.carTypeId = carBean.carType + "";
        mostFitAvailableBean.distance = carListBean.distance + "";
        mostFitAvailableBean.expectedCompTime = (null == carBean.expectedCompTime) ? "" : carBean.expectedCompTime + "";
        mostFitAvailableBean.limit = 20 + "";
        mostFitAvailableBean.offset = 0 + "";
        String channelPrice = "" + (carBean.price + countView.getAdditionalPrice());
        mostFitAvailableBean.priceChannel = channelPrice;
        mostFitAvailableBean.useOrderPrice = channelPrice;
        mostFitAvailableBean.serviceCityId = params.cityBean.cityId + "";
        mostFitAvailableBean.serviceCountryId = params.cityBean.areaCode + "";
        mostFitAvailableBean.serviceLocalDays = "0";
        mostFitAvailableBean.serviceNonlocalDays = params.skuItemBean.daysCount + "";
        mostFitAvailableBean.serviceTime = serverDate + " " + "00:00:00";
        mostFitAvailableBean.userId = UserEntity.getUser().getUserId(this);
        mostFitAvailableBean.totalDays = params.skuItemBean.daysCount + "";
        mostFitAvailableBean.orderType = params.skuItemBean.goodsClass == 1 ? "5" : "6";
        mostFitAvailableBean.carModelId = carBean.carId + "";
        bundle.putSerializable(Constants.PARAMS_DATA, mostFitAvailableBean);
        if (null != mostFitBean) {
            couponId = mostFitBean.couponId;
            bundle.putString("idStr", mostFitBean.couponId);
        } else if (null != couponBean) {
            couponId = couponBean.couponID;
            bundle.putString("idStr", couponBean.couponID);
        } else {
            bundle.putString("idStr", "");
        }
        bundle.putString(Constants.PARAMS_SOURCE, getEventSource());
        Intent intent = new Intent(this, CouponActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /* 进入旅游基金 */
    @Override
    public void intentTravelFund() {
        Intent intent = new Intent(this, TravelFundActivity.class);
        intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
        startActivity(intent);
    }

    /* 去支付 */
    @Override
    public void onSubmitOrder() {
        hideSoftInput();
        if (!travelerInfoView.checkTravelerInfo()) {
            return;
        }
        if (!CommonUtils.isLogin(this)) {
            return;
        }
        requestSubmitOrder();
    }

    /*
    * 获取车辆列表
    * @params serverDate 出发日期
    * */
    private void requestPriceSku(String serverDate) {
        this.serverDate = serverDate;
        String serverDayTime = serverDate + " " + SERVER_TIME + ":00";
        RequestPriceSku request = new RequestPriceSku(activity, params.skuItemBean.goodsNo, serverDayTime, "" + params.cityBean.cityId);
        requestData(request);
    }

    /*
    * 获取优惠券
    * @params additionalPrice 儿童座椅 + 酒店价格
    * */
    private void requestMostFit(int additionalPrice) {
        RequestMostFit requestMostFit = new RequestMostFit(this
                , carBean.price + additionalPrice + ""
                , carBean.price + ""
                , serverDate + " " + "00:00:00"
                , carBean.carType + ""
                , carBean.seatCategory + ""
                , params.cityBean.cityId + ""
                , params.cityBean.areaCode + ""
                , params.skuItemBean.daysCount + ""
                , carListBean.distance + ""
                , "0"
                , params.skuItemBean.daysCount + ""
                , params.skuItemBean.daysCount + ""
                , orderType + ""
                , carBean.carId + "");
        requestData(requestMostFit);
    }

    /*
    * 获取旅游基金
    * @params additionalPrice 儿童座椅 + 酒店价格
    * */
    private void requestTravelFund(int additionalPrice) {
        RequestDeduction requestDeduction = new RequestDeduction(this, carBean.price + additionalPrice + "");
        requestData(requestDeduction);
    }

    /*
    * 提交订单
    * */
    private void requestSubmitOrder() {
        orderBean = getSKUOrderByInput();
        switch (orderType) {
            case 5:
                RequestSubmitDaily requestSubmitBase = new RequestSubmitDaily(this, orderBean);
                requestData(requestSubmitBase);
                break;
            case 6:
                RequestSubmitLine requestSubmitLine = new RequestSubmitLine(this, orderBean);
                requestData(requestSubmitLine);
                break;
        }
    }

    /*
    * 金额为零，直接请求支付接口（支付宝）
    * */
    private void requestPayNo(String orderNo) {
        RequestPayNo pequestPayNo = new RequestPayNo(this, orderNo, 0, Constants.PAY_STATE_ALIPAY, couponId);
        requestData(pequestPayNo);
    }

    /*
    * 获取退改规则
    * */
    private void requestCancleTips() {
        RequestCancleTips requestCancleTips = new RequestCancleTips(activity
                , carBean
                , params.cityBean.cityId + ""
                , params.skuItemBean.goodsType + ""
                , carBean.carType + ""
                , carBean.seatCategory + ""
                , serverDate + " " + SERVER_TIME + ":00"
                , "0"
                , params.skuItemBean.goodsVersion + ""
                , params.skuItemBean.goodsNo + ""
                , orderType + "");
        requestData(requestCancleTips);
    }

    /*
    * 下单参数，参考OrderNewActivity
    * */
    private OrderBean getSKUOrderByInput() {
        ManLuggageBean manLuggageBean = countView.getManLuggageBean();
        SkuOrderTravelerInfoView.TravelerInfoBean travelerInfoBean = travelerInfoView.getTravelerInfoBean();
        ContactUsersBean contactUsersBean = new ContactUsersBean();
        contactUsersBean.userName = travelerInfoBean.travelerName;
        contactUsersBean.userPhone = travelerInfoBean.travelerPhone;
        contactUsersBean.phoneCode = travelerInfoBean.getAreaCode();

        return new OrderUtils().getSKUOrderByInput(""
                , params.skuItemBean
                , chooseDateView.getServiceDate()
                , SERVER_TIME
                , carListBean.distance + ""
                , carBean
                , manLuggageBean.mans + ""
                , manLuggageBean.childs + ""
                , params.cityBean
                , OrderUtils.getPassCityStr(params.skuItemBean)
                , contactUsersBean
                , travelerInfoBean.mark
                , travelerInfoBean.travelerName
                , travelerInfoBean.poiBean
                , discountView.isCheckedTravelFund()
                , deductionBean != null ? CommonUtils.getCountInteger(deductionBean.deduction) + "" : "0"
                , couponBean
                , mostFitBean
                , carListBean
                , manLuggageBean
                , manLuggageBean.roomCount
                , countView.getHotelTotalPrice()
                , orderType
                , manLuggageBean.luggages + "");
    }

    /*
    * 后续页面需要的统计参数
    * */
    private EventPayBean getChoosePaymentStatisticParams() {
        EventPayBean eventPayBean = new EventPayBean();
        eventPayBean.guideCollectId = "";
        eventPayBean.paysource = "下单过程中";
        eventPayBean.orderType = orderType;

        if (carBean != null) {
            eventPayBean.carType = carBean.carDesc;
            eventPayBean.seatCategory = carBean.seatCategory;
            eventPayBean.guestcount = carBean.capOfPerson+"";
            eventPayBean.shouldPay = carBean.vehiclePrice + carBean.servicePrice;
        }
        if (orderInfoBean != null) {
            eventPayBean.orderId = orderInfoBean.getOrderno();
            eventPayBean.actualPay = orderInfoBean.getPriceActual();
        }
        if (orderBean != null) {
            eventPayBean.isFlightSign = orderBean.isFlightSign;
            eventPayBean.isCheckin = orderBean.isCheckin;
            eventPayBean.orderStatus = orderBean.orderStatus;
            eventPayBean.isSelectedGuide = !TextUtils.isEmpty(orderBean.guideCollectId);
            eventPayBean.couponPrice = CommonUtils.getCountInteger(orderBean.coupPriceInfo);
            eventPayBean.travelFundPrice = CommonUtils.getCountInteger(orderBean.travelFund);
        }
        return eventPayBean;
    }

    @Override
    public String getEventSource() {
        return "线路下单页";
    }
}