package com.hugboga.custom.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.AreaCodeBean;
import com.hugboga.custom.data.bean.CarAdditionalServicePrice;
import com.hugboga.custom.data.bean.CarBean;
import com.hugboga.custom.data.bean.CarListBean;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.CouponBean;
import com.hugboga.custom.data.bean.DeductionBean;
import com.hugboga.custom.data.bean.FlightBean;
import com.hugboga.custom.data.bean.ManLuggageBean;
import com.hugboga.custom.data.bean.MostFitAvailableBean;
import com.hugboga.custom.data.bean.MostFitBean;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.OrderInfoBean;
import com.hugboga.custom.data.bean.PoiBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.request.RequestCancleTips;
import com.hugboga.custom.data.request.RequestDeduction;
import com.hugboga.custom.data.request.RequestMostFit;
import com.hugboga.custom.data.request.RequestPayNo;
import com.hugboga.custom.data.request.RequestSubmitBase;
import com.hugboga.custom.data.request.RequestSubmitPick;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.bean.EventPayBean;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.OrderUtils;
import com.hugboga.custom.utils.PhoneInfo;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.DialogUtil;
import com.hugboga.custom.widget.OrderDescriptionView;
import com.hugboga.custom.widget.OrderExplainView;
import com.hugboga.custom.widget.SkuOrderBottomView;
import com.hugboga.custom.widget.SkuOrderCountView;
import com.hugboga.custom.widget.SkuOrderDiscountView;
import com.hugboga.custom.widget.SkuOrderTravelerInfoView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.List;

import butterknife.Bind;

/**
 * Created by qingcha on 17/5/19.
 */
public class OrderActivity extends BaseActivity implements SkuOrderDiscountView.DiscountOnClickListener,
        SkuOrderCountView.OnCountChangeListener, SkuOrderBottomView.OnSubmitOrderListener, SkuOrderTravelerInfoView.OnSwitchPickOrSendListener{

    @Bind(R.id.order_scrollview)
    ScrollView scrollView;
    @Bind(R.id.order_bottom_view)
    SkuOrderBottomView bottomView;
    @Bind(R.id.order_desc_view)
    OrderDescriptionView descriptionView;
    @Bind(R.id.order_count_view)
    SkuOrderCountView countView;
    @Bind(R.id.order_traveler_info_view)
    SkuOrderTravelerInfoView travelerInfoView;
    @Bind(R.id.order_discount_view)
    SkuOrderDiscountView discountView;
    @Bind(R.id.order_explain_view)
    OrderExplainView explainView;

    private OrderActivity.Params params;
    private String serverDate;
    private OrderInfoBean orderInfoBean;
    private OrderBean orderBean;

    private DeductionBean deductionBean;

    private MostFitBean mostFitBean;
    private CouponBean couponBean;
    private String couponId;

    private int sensorsActualPrice = 0;

    public static class Params implements Serializable {
        public FlightBean flightBean;
        public PoiBean poiBean;
        public CarListBean carListBean;
        public CarBean carBean;
        public int orderType;
        public CityBean cityBean;
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_order;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            params = (OrderActivity.Params) savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                params = (OrderActivity.Params) bundle.getSerializable(Constants.PARAMS_DATA);
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
        initTitleBar();

        if (params.orderType == 1) {
            descriptionView.setPickData(params);
            serverDate = params.flightBean.arrDate + " " + params.flightBean.arrivalTime + ":00";
        }

        discountView.setDiscountOnClickListener(this);
        countView.setOnCountChangeListener(this);
        countView.update(params.carBean, params.carListBean, params.flightBean.arrDate);
        bottomView.setOnSubmitOrderListener(this);
        explainView.setTermsTextViewVisibility("去支付", View.VISIBLE);
        travelerInfoView.setOrderType(params.orderType);
        travelerInfoView.setCarListBean(params.carListBean);
        travelerInfoView.setOnSwitchPickOrSendListener(this);

        int additionalPrice = countView.getAdditionalPrice() + travelerInfoView.getAdditionalPrice();
        requestMostFit(additionalPrice);
        requestTravelFund(additionalPrice);
        requestCancleTips();

        scrollToTop();
    }

    public void initTitleBar() {
        initDefaultTitleBar();
        fgTitle.setText("确认订单");
        fgRightTV.setVisibility(View.GONE);

        RelativeLayout.LayoutParams headerRightImageParams = new RelativeLayout.LayoutParams(UIUtils.dip2px(38), UIUtils.dip2px(38));
        headerRightImageParams.rightMargin = UIUtils.dip2px(18);
        headerRightImageParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        headerRightImageParams.addRule(RelativeLayout.CENTER_VERTICAL);
        fgRightBtn.setLayoutParams(headerRightImageParams);
        fgRightBtn.setPadding(0,0,0,0);
        fgRightBtn.setImageResource(R.mipmap.topbar_cs);
        fgRightBtn.setVisibility(View.VISIBLE);
        fgRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtil.getInstance(OrderActivity.this).showServiceDialog(OrderActivity.this, null, UnicornServiceActivity.SourceType.TYPE_CHARTERED, null, null, getEventSource());
            }
        });
    }

    @Subscribe
    public void onEventMainThread(final EventAction action) {
        switch (action.getType()) {
            case CHOOSE_COUNTRY_BACK:
                AreaCodeBean areaCodeBean = (AreaCodeBean) action.getData();
                if (areaCodeBean.viewId == R.id.sku_order_traveler_info_code_tv) {
                    travelerInfoView.setAreaCode(areaCodeBean.getCode());
                } else if (areaCodeBean.viewId == R.id.sku_order_traveler_info_other_code_tv) {
                    travelerInfoView.setOtherAreaCode(areaCodeBean.getCode());
                }
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
                Intent intent = new Intent(this, PoiSearchActivity.class);
                intent.putExtras(bundle);
                intent.putExtra(PoiSearchActivity.PARAM_BUSINESS_TYPE, params.orderType);
                startActivity(intent);
                break;
            case SELECT_COUPON_BACK:
                couponBean = (CouponBean) action.getData();
                if (couponBean.couponID.equalsIgnoreCase(couponId)) {
                    couponId = null;
                    couponBean = null;
                }
                mostFitBean = null;
                discountView.setCouponBean(couponBean);
                break;
        }
    }

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
        int additionalPrice = price + travelerInfoView.getAdditionalPrice();
        requestMostFit(additionalPrice);
        requestTravelFund(additionalPrice);
    }

    @Override
    public void onSwitchPickOrSend(boolean isSelect, int _additionalPrice) {
        if (params.carListBean.additionalServicePrice != null) {
            CarAdditionalServicePrice additionalServicePrice = params.carListBean.additionalServicePrice;
            boolean isPickup = params.orderType == 1 && CommonUtils.getCountInteger(additionalServicePrice.pickupSignPrice) > 0;
            boolean isSend = params.orderType == 2 && CommonUtils.getCountInteger(additionalServicePrice.checkInPrice) > 0;
            if (isPickup || isSend) {
                int additionalPrice = _additionalPrice + countView.getAdditionalPrice();
                requestMostFit(additionalPrice);
                requestTravelFund(additionalPrice);
            }
        }
    }

    /* 选择优惠方式 */
    @Override
    public void chooseDiscount(int type) {
        if (params.carBean == null) {
            return;
        }
        final int additionalPrice = countView.getAdditionalPrice() + travelerInfoView.getAdditionalPrice();
        int totalPrice = params.carBean.price + additionalPrice;
        int actualPrice = totalPrice;
        int deductionPrice = 0;

        switch (type) {
            case SkuOrderDiscountView.TYPE_COUPON:
                if (mostFitBean == null || mostFitBean.actualPrice == null) {
                    if (couponBean != null) {
                        actualPrice = couponBean.actualPrice.intValue();
                    }
                } else {
                    actualPrice = mostFitBean.actualPrice.intValue();
                }
                deductionPrice = totalPrice - actualPrice > 0 ? totalPrice - actualPrice : 0;
                break;
            case SkuOrderDiscountView.TYPE_TRAVEL_FUND:
                if (deductionBean != null && deductionBean.priceToPay != null) {
                    deductionPrice = CommonUtils.getCountInteger(deductionBean.deduction);
                    actualPrice = params.carBean.price - deductionPrice + additionalPrice;
                }
                break;
            case SkuOrderDiscountView.TYPE_INVALID:
                break;
        }
        bottomView.updatePrice(actualPrice, deductionPrice);
        sensorsActualPrice = actualPrice;
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

    /* 进入优惠券列表 */
    @Override
    public void intentCouponList() {
        Bundle bundle = new Bundle();
        MostFitAvailableBean mostFitAvailableBean = new MostFitAvailableBean();
        mostFitAvailableBean.carSeatNum = params.carBean.seatCategory + "";
        mostFitAvailableBean.carTypeId = params.carBean.carType + "";
        mostFitAvailableBean.distance = params.carListBean.distance + "";
        mostFitAvailableBean.expectedCompTime = params.carBean.expectedCompTime == null ? "" : params.carBean.expectedCompTime;
        mostFitAvailableBean.limit = 20 + "";
        mostFitAvailableBean.offset = 0 + "";
        String channelPrice = "" + (params.carBean.price + countView.getAdditionalPrice() + travelerInfoView.getAdditionalPrice());
        mostFitAvailableBean.priceChannel = channelPrice;
        mostFitAvailableBean.useOrderPrice = channelPrice;
        mostFitAvailableBean.serviceCityId = params.cityBean.cityId + "";
        mostFitAvailableBean.serviceCountryId = params.cityBean.areaCode + "";
        mostFitAvailableBean.serviceTime = serverDate;
        mostFitAvailableBean.userId = UserEntity.getUser().getUserId(this);
        mostFitAvailableBean.totalDays = "0";
        mostFitAvailableBean.orderType = params.orderType + "";
        mostFitAvailableBean.carModelId = params.carBean.carId + "";
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

    /*
    * 获取优惠券
    * @params additionalPrice 儿童座椅 + 酒店价格 + 接机/举牌
    * */
    private void requestMostFit(int additionalPrice) {
        RequestMostFit requestMostFit = new RequestMostFit(this
                , params.carBean.price + additionalPrice + ""
                , params.carBean.price + additionalPrice + ""
                , serverDate
                , params.carBean.carType + ""
                , params.carBean.seatCategory + ""
                , params.cityBean.cityId + ""
                , params.cityBean.areaCode + ""
                , "0"
                , params.carListBean.distance + ""
                , params.carBean.expectedCompTime == null ? "" : params.carBean.expectedCompTime
                , params.orderType + ""
                , params.carBean.carId + ""
                , null);
        requestData(requestMostFit);
    }

    /*
     * 获取旅游基金
     * @params additionalPrice 儿童座椅 + 酒店价格 + 接机/举牌
     * */
    private void requestTravelFund(int additionalPrice) {
        RequestDeduction requestDeduction = new RequestDeduction(this, params.carBean.price + additionalPrice + "");
        requestData(requestDeduction);
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
        RequestCancleTips requestCancleTips = new RequestCancleTips(this
                , params.carBean
                , params.cityBean.cityId + ""
                , ""
                , params.carBean.carType + ""
                , params.carBean.seatCategory + ""
                , serverDate
                , "0"
                , ""
                , ""
                , params.orderType + "");
        requestData(requestCancleTips);
    }


    /*
    * 提交订单
    * */
    private void requestSubmitOrder() {
        switch (params.orderType) {
            case 1:
                RequestSubmitPick requestSubmitPick = new RequestSubmitPick(activity, getPickOrderByInput());
                requestData(requestSubmitPick);
                break;
        }
    }

    private OrderBean getPickOrderByInput() {
        SkuOrderTravelerInfoView.TravelerInfoBean travelerInfoBean = travelerInfoView.getTravelerInfoBean();
        ManLuggageBean manLuggageBean = countView.getManLuggageBean();
        StatisticClickEvent.pickClick(StatisticConstant.SUBMITORDER_J, getIntentSource(), params.carBean.carDesc + "", travelerInfoBean.isCheckin, countView.getTotalPeople());
        return new OrderUtils().getPickOrderByInput(params.flightBean
                , params.poiBean
                , params.carBean
                , travelerInfoBean.pickName
                , params.carListBean
                , travelerInfoBean.pickName
                , manLuggageBean.mans + ""
                , manLuggageBean.childs + ""
                , params.carListBean.distance + ""
                , ""
                , ""
                , travelerInfoBean.travelerName
                , ""
                , travelerInfoBean.mark
                , params.flightBean.arrivalTime
                , manLuggageBean.childSeats + ""
                , manLuggageBean.luggages + ""
                , travelerInfoBean.getContactUsersBean()
                , discountView.isCheckedTravelFund()
                , deductionBean != null ? CommonUtils.getCountInteger(deductionBean.deduction) + "" : "0"
                , couponBean
                , mostFitBean
                , "" //TODO 指定司导下单
                , manLuggageBean
                , travelerInfoBean.isCheckin);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        travelerInfoView.onRequestPermissionsResult(requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (SkuOrderTravelerInfoView.REQUEST_CODE_PICK_CONTACTS == requestCode || SkuOrderTravelerInfoView.REQUEST_CODE_PICK_OTHER_CONTACTS == requestCode) {
            Uri result = data.getData();
            String[] contact = PhoneInfo.getPhoneContacts(this, result);
            if (contact == null || contact.length < 2) {
                return;
            }
            if (!TextUtils.isEmpty(contact[0])) {
                if (SkuOrderTravelerInfoView.REQUEST_CODE_PICK_CONTACTS == requestCode) {
                    travelerInfoView.setTravelerName(contact[0]);
                } else {
                    travelerInfoView.setOtherTravelerName(contact[0]);
                }
            }
            if (!TextUtils.isEmpty(contact[1])){
                String phone = contact[1];
                if (!TextUtils.isEmpty(phone)) {
                    phone = phone.replace("+86", "");//此处拷贝自以前代码。。。
                }
                if (SkuOrderTravelerInfoView.REQUEST_CODE_PICK_CONTACTS == requestCode) {
                    travelerInfoView.setTravelerPhone(phone);
                } else {
                    travelerInfoView.setOtherTravelerPhone(phone);
                }
            }
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        super.onDataRequestSucceed(_request);
        if (_request instanceof RequestMostFit) {
            mostFitBean = ((RequestMostFit) _request).getData();
            discountView.setMostFitBean(mostFitBean);
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

    /*
     * 后续页面需要的统计参数
     * */
    private EventPayBean getChoosePaymentStatisticParams() {
        EventPayBean eventPayBean = new EventPayBean();
        eventPayBean.guideCollectId = "";
        eventPayBean.paysource = "下单过程中";
        eventPayBean.orderType = params.orderType;

        if (params.carBean != null) {
            eventPayBean.carType = params.carBean.carDesc;
            eventPayBean.seatCategory = params.carBean.seatCategory;
            eventPayBean.guestcount = params.carBean.capOfPerson+"";
            eventPayBean.shouldPay = params.carBean.vehiclePrice + params.carBean.servicePrice;
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

}
