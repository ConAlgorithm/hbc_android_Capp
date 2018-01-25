package com.hugboga.custom.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.AirPort;
import com.hugboga.custom.data.bean.AreaCodeBean;
import com.hugboga.custom.data.bean.CarAdditionalServicePrice;
import com.hugboga.custom.data.bean.CarBean;
import com.hugboga.custom.data.bean.CarListBean;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.CouponBean;
import com.hugboga.custom.data.bean.DeductionBean;
import com.hugboga.custom.data.bean.FlightBean;
import com.hugboga.custom.data.bean.GuidesDetailData;
import com.hugboga.custom.data.bean.ManLuggageBean;
import com.hugboga.custom.data.bean.MostFitAvailableBean;
import com.hugboga.custom.data.bean.MostFitBean;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.OrderInfoBean;
import com.hugboga.custom.data.bean.PayResultExtarParamsBean;
import com.hugboga.custom.data.bean.PoiBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestCancleTips;
import com.hugboga.custom.data.request.RequestDeduction;
import com.hugboga.custom.data.request.RequestMostFit;
import com.hugboga.custom.data.request.RequestPayNo;
import com.hugboga.custom.data.request.RequestSubmitBase;
import com.hugboga.custom.data.request.RequestSubmitPickOrder;
import com.hugboga.custom.data.request.RequestSubmitPickSeckills;
import com.hugboga.custom.data.request.RequestSubmitRent;
import com.hugboga.custom.data.request.RequestSubmitSend;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.bean.EventPayBean;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.AlertDialogUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.OrderUtils;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.CsDialog;
import com.hugboga.custom.widget.OrderDescriptionView;
import com.hugboga.custom.widget.OrderExplainView;
import com.hugboga.custom.widget.OrderInsuranceView;
import com.hugboga.custom.widget.SkuOrderBottomView;
import com.hugboga.custom.widget.SkuOrderCountView;
import com.hugboga.custom.widget.SkuOrderDiscountView;
import com.hugboga.custom.widget.SkuOrderTravelerInfoView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by qingcha on 17/5/19.
 */
public class OrderActivity extends BaseActivity implements SkuOrderDiscountView.DiscountOnClickListener,
        SkuOrderCountView.OnCountChangeListener, SkuOrderBottomView.OnSubmitOrderListener, SkuOrderTravelerInfoView.OnSwitchPickOrSendListener {

    @BindView(R.id.order_scrollview)
    ScrollView scrollView;
    @BindView(R.id.order_seckills_layout)
    FrameLayout seckillsLayout;
    @BindView(R.id.order_bottom_view)
    SkuOrderBottomView bottomView;
    @BindView(R.id.order_desc_view)
    OrderDescriptionView descriptionView;
    @BindView(R.id.order_count_view)
    SkuOrderCountView countView;
    @BindView(R.id.order_traveler_info_view)
    SkuOrderTravelerInfoView travelerInfoView;
    @BindView(R.id.order_discount_view)
    SkuOrderDiscountView discountView;
    @BindView(R.id.order_insurance_view)
    OrderInsuranceView insuranceView;
    @BindView(R.id.order_explain_view)
    OrderExplainView explainView;

    private OrderActivity.Params params;
    private String serverDate;
    private OrderInfoBean orderInfoBean;
    private OrderBean orderBean;

    private DeductionBean deductionBean;

    private MostFitBean mostFitBean;
    private CouponBean couponBean;
    private String couponId;

    private int requestCouponCount = 0;

    private boolean requestedSubmit = false;
    CsDialog csDialog;

    public static class Params implements Serializable {
        public FlightBean flightBean; // 接机航班信息
        public AirPort airPortBean;   // 送机机场信息
        public PoiBean startPoiBean;  // 送机出发地, 单次出发地
        public PoiBean endPoiBean;    // 接机送达地, 单次送达地
        public int orderType;
        public CarListBean carListBean;
        public CarBean carBean;
        public CityBean cityBean;
        public String serverDate;
        public String serverTime;
        public GuidesDetailData guidesDetailData;
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

        MobClickUtils.onEvent(getEventId(), getEventMap());

        params.carBean.expectedCompTime = params.carListBean.estTime;//历史遗留
        serverDate = params.serverDate + " " + params.serverTime + ":00";

        descriptionView.setData(params);
        countView.setOnCountChangeListener(this);
        countView.update(params.carBean, params.carListBean, params.serverDate);
        bottomView.setOnSubmitOrderListener(this);
        bottomView.setHintData(params.orderType, params.guidesDetailData != null, params.carListBean.isSeckills,
                params.carBean.reconfirmFlag, params.carBean.reconfirmTip);
        explainView.setTermsTextViewVisibility("去支付", View.VISIBLE);
        travelerInfoView.setOrderType(params.orderType, params.carListBean);
        travelerInfoView.setOnSwitchPickOrSendListener(this);
        discountView.setDiscountOnClickListener(this);
        double additionalPrice = countView.getAdditionalPrice() + travelerInfoView.getAdditionalPrice();
        if (params.carListBean.isSeckills) {
            discountView.setVisibility(View.GONE);
            seckillsLayout.setVisibility(View.VISIBLE);
            bottomView.updatePrice(params.carBean.seckillingPrice, params.carBean.price + additionalPrice - params.carBean.seckillingPrice);
        } else {
            requestCouponCount = 2;
            requestMostFit(additionalPrice);
            requestTravelFund(additionalPrice);
        }
        requestCancleTips();

        scrollToTop();
    }

    public void initTitleBar() {
        initDefaultTitleBar();
        fgTitle.setText(R.string.order_title);
        fgRightTV.setVisibility(View.GONE);

        RelativeLayout.LayoutParams headerRightImageParams = new RelativeLayout.LayoutParams(UIUtils.dip2px(38), UIUtils.dip2px(38));
        headerRightImageParams.rightMargin = UIUtils.dip2px(18);
        headerRightImageParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        headerRightImageParams.addRule(RelativeLayout.CENTER_VERTICAL);
        fgRightBtn.setLayoutParams(headerRightImageParams);
        fgRightBtn.setPadding(0, 0, 0, 0);
        fgRightBtn.setImageResource(R.mipmap.topbar_cs);
        fgRightBtn.setVisibility(View.VISIBLE);
        fgRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SensorsUtils.onAppClick(getEventSource(), "客服", getIntentSource());
                //DialogUtil.getInstance(OrderActivity.this).showServiceDialog(OrderActivity.this, null, UnicornServiceActivity.SourceType.TYPE_CHARTERED, null, null, getEventSource());
                csDialog = CommonUtils.csDialog(OrderActivity.this, null, null, null, UnicornServiceActivity.SourceType.TYPE_CHARTERED, getEventSource(), new CsDialog.OnCsListener() {
                    @Override
                    public void onCs() {
                        if (csDialog != null && csDialog.isShowing()) {
                            csDialog.dismiss();
                        }
                    }
                });
            }
        });
    }

    @Subscribe
    public void onEventMainThread(final EventAction action) {
        switch (action.getType()) {
            case CHOOSE_COUNTRY_BACK:
                AreaCodeBean areaCodeBean = (AreaCodeBean) action.getData();
                travelerInfoView.setAreaCode(areaCodeBean.getCode(), areaCodeBean.viewId);
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
                if (couponBean == null) {
                    couponId = null;
                    couponBean = null;
                } else {
                    if (couponBean.couponID.equalsIgnoreCase(couponId)) {
                        break;
                    } else {
                        couponId = couponBean.couponID;
                    }
                }
                mostFitBean = null;
                discountView.setCouponBean(couponBean);
                break;
            case ORDER_REFRESH:
                finish();
                break;
            case ORDER_SECKILLS_ERROR:
                String errorMessage = (String) action.getData();
                showCheckSeckillsDialog(errorMessage);
                break;
        }
    }

    public String getCouponId() {
        if (null != mostFitBean) {
            couponId = mostFitBean.couponId;
        } else if (null != couponBean) {
            couponId = couponBean.couponID;
        }
        return couponId;
    }

    private void showCheckSeckillsDialog(String content) {
        AlertDialogUtils.showAlertDialog(this, content, CommonUtils.getString(R.string.order_empty_continue), CommonUtils.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                EventBus.getDefault().post(new EventAction(EventType.ORDER_SECKILLS_REFRESH));
                OrderActivity.this.finish();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onSubmitOrder() {
        hideSoftInput();
        if (!travelerInfoView.checkTravelerInfo()) {
            return;
        }
        if (!CommonUtils.isLogin(this, getEventSource())) {
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
        insuranceView.setInsuranceCount(bean.mans + bean.childs);
    }

    /* 儿童座椅+酒店价格发生改变 */
    @Override
    public void onAdditionalPriceChange(double price) {
        double additionalPrice = price + travelerInfoView.getAdditionalPrice();
        if (params.carListBean.isSeckills) {
            bottomView.updatePrice(params.carBean.seckillingPrice, params.carBean.price + additionalPrice - params.carBean.seckillingPrice);
        } else {
            requestCouponCount = 2;
            requestMostFit(additionalPrice);
            requestTravelFund(additionalPrice);
        }
    }

    @Override
    public void onSwitchPickOrSend(boolean isSelect, int _additionalPrice) {
        if (params.carListBean.additionalServicePrice != null) {
            if (params.carListBean.isSeckills) {
                double additionalPrice = _additionalPrice + countView.getAdditionalPrice();
                bottomView.updatePrice(params.carBean.seckillingPrice, params.carBean.price + additionalPrice - params.carBean.seckillingPrice);
            } else {
                CarAdditionalServicePrice additionalServicePrice = params.carListBean.additionalServicePrice;
                boolean isPickup = params.orderType == 1 && CommonUtils.getCountInteger(additionalServicePrice.pickupSignPrice) > 0;
                boolean isSend = params.orderType == 2 && CommonUtils.getCountInteger(additionalServicePrice.checkInPrice) > 0;
                if (isPickup || isSend) {
                    double additionalPrice = _additionalPrice + countView.getAdditionalPrice();
                    requestCouponCount = 2;
                    requestMostFit(additionalPrice);
                    requestTravelFund(additionalPrice);
                }
            }
        }
    }

    /* 选择优惠方式 */
    @Override
    public void chooseDiscount(int type) {
        if (params.carBean == null) {
            return;
        }
        final double additionalPrice = countView.getAdditionalPrice() + travelerInfoView.getAdditionalPrice();
        double totalPrice = params.carBean.price + additionalPrice;
        double actualPrice = totalPrice;
        double deductionPrice = 0;

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
        if (requestCouponCount == 0) {
            bottomView.setHintTV();
        }
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
        bundle.putString("idStr", getCouponId());
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
    private void requestMostFit(double additionalPrice) {
        RequestMostFit requestMostFit = new RequestMostFit(this
                , params.carBean.price + additionalPrice + ""
                , params.carBean.price + additionalPrice + ""
                , serverDate
                , params.carBean.carType + ""
                , params.carBean.seatCategory + ""
                , params.cityBean != null ? params.cityBean.cityId + "" : ""
                , params.cityBean != null ? params.cityBean.areaCode + "" : ""
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
    private void requestTravelFund(double additionalPrice) {
        RequestDeduction requestDeduction = new RequestDeduction(this, params.carBean.price + additionalPrice + "");
        requestData(requestDeduction);
    }

    /*
     * 金额为零，直接请求支付接口（支付宝）
     * */
    private void requestPayNo(String orderNo) {
        RequestPayNo pequestPayNo = new RequestPayNo(this, orderNo, 0, Constants.PAY_STATE_ALIPAY, discountView.isCheckedTravelFund() ? "" : getCouponId());
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
        if (requestedSubmit) {
            return;
        } else {
            requestedSubmit = true;
        }
        RequestSubmitBase requestSubmitBase = null;
        switch (params.orderType) {
            case 1:
                orderBean = getPickOrderByInput();
                if (params.carListBean.isSeckills) {
                    requestSubmitBase = new RequestSubmitPickSeckills(this, orderBean);
                } else {
                    requestSubmitBase = new RequestSubmitPickOrder(this, orderBean);
                }
                break;
            case 2:
                orderBean = getSendOrderByInput();
                requestSubmitBase = new RequestSubmitSend(this, orderBean);
                break;
            case 4:
                orderBean = getSingleOrderByInput();
                requestSubmitBase = new RequestSubmitRent(this, orderBean);
                break;
        }
        requestData(requestSubmitBase);
        SensorsUtils.onAppClick(getEventSource(), "去支付", getIntentSource());
    }

    private OrderBean getPickOrderByInput() {
        SkuOrderTravelerInfoView.TravelerInfoBean travelerInfoBean = travelerInfoView.getTravelerInfoBean();
        ManLuggageBean manLuggageBean = countView.getManLuggageBean();
        String eventId = params.carListBean.isSeckills ? StatisticConstant.SUBMITORDER_J_MS : StatisticConstant.SUBMITORDER_J;
        StatisticClickEvent.pickClick(eventId, getIntentSource(), params.carBean.carDesc + "", travelerInfoBean.isPickup, countView.getTotalPeople());
        return new OrderUtils().getPickOrderByInput(params.flightBean
                , params.endPoiBean
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
                , params.serverTime
                , manLuggageBean.childSeats + ""
                , manLuggageBean.luggages + ""
                , travelerInfoBean.getContactUsersBean()
                , discountView.isCheckedTravelFund()
                , deductionBean != null ? CommonUtils.getCountInteger(deductionBean.deduction) + "" : "0"
                , couponBean
                , mostFitBean
                , params.guidesDetailData != null ? params.guidesDetailData.guideId : ""
                , manLuggageBean
                , travelerInfoBean.isPickup
                , travelerInfoBean.wechatNo);
    }

    private OrderBean getSendOrderByInput() {
        SkuOrderTravelerInfoView.TravelerInfoBean travelerInfoBean = travelerInfoView.getTravelerInfoBean();
        ManLuggageBean manLuggageBean = countView.getManLuggageBean();
        StatisticClickEvent.sendClick(StatisticConstant.SUBMITORDER_S, getIntentSource(), params.carBean.carDesc + "", travelerInfoBean.isCheckin, countView.getTotalPeople());
        return new OrderUtils().getSendOrderByInput(params.startPoiBean
                , params.carBean
                , travelerInfoBean.travelerName
                , travelerInfoBean.isCheckin
                , travelerInfoBean.sendFlight
                , params.airPortBean
                , params.carListBean
                , params.serverDate
                , discountView.isCheckedTravelFund()
                , manLuggageBean.mans + ""
                , manLuggageBean.childs + ""
                , ""
                , ""
                , travelerInfoBean.mark
                , params.serverTime
                , manLuggageBean.childSeats + ""
                , manLuggageBean.luggages + ""
                , travelerInfoBean.getContactUsersBean()
                , deductionBean != null ? CommonUtils.getCountInteger(deductionBean.deduction) + "" : "0"
                , couponBean
                , mostFitBean
                , params.guidesDetailData != null ? params.guidesDetailData.guideId : ""
                , manLuggageBean
                , travelerInfoBean.wechatNo);
    }

    private OrderBean getSingleOrderByInput() {
        SkuOrderTravelerInfoView.TravelerInfoBean travelerInfoBean = travelerInfoView.getTravelerInfoBean();
        ManLuggageBean manLuggageBean = countView.getManLuggageBean();
        StatisticClickEvent.commitClick(StatisticConstant.SUBMITORDER_C, getIntentSource(), params.carBean.carDesc + "", countView.getTotalPeople(), travelerInfoBean.isOther);

        return new OrderUtils().getSingleOrderByInput(manLuggageBean.mans + ""
                , params.carBean
                , manLuggageBean.childs + ""
                , params.cityBean.cityId + ""
                , params.cityBean.cityId + ""
                , null
                , params.serverTime
                , params.cityBean.name
                , params.serverDate
                , params.startPoiBean
                , params.endPoiBean
                , params.carListBean.distance + ""
                , ""
                , ""
                , params.carListBean
                , manLuggageBean
                , travelerInfoBean.travelerName
                , ""
                , travelerInfoBean.mark
                , manLuggageBean.childSeats + ""
                , manLuggageBean.luggages + ""
                , travelerInfoBean.getContactUsersBean()
                , discountView.isCheckedTravelFund()
                , deductionBean != null ? CommonUtils.getCountInteger(deductionBean.deduction) + "" : "0"
                , couponBean
                , mostFitBean
                , params.guidesDetailData != null ? params.guidesDetailData.guideId : ""
                , travelerInfoBean.wechatNo);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        travelerInfoView.onRequestPermissionsResult(requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        travelerInfoView.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        super.onDataRequestSucceed(_request);
        if (_request instanceof RequestMostFit) {
            mostFitBean = ((RequestMostFit) _request).getData();
            requestCouponCount--;
            discountView.setMostFitBean(mostFitBean);
        } else if (_request instanceof RequestDeduction) {
            deductionBean = ((RequestDeduction) _request).getData();
            requestCouponCount--;
            discountView.setDeductionBean(deductionBean);
        } else if (_request instanceof RequestSubmitBase) {
            requestedSubmit = false;
            orderInfoBean = ((RequestSubmitBase) _request).getData();
            if (orderInfoBean.getPriceActual() == 0) {
                requestPayNo(orderInfoBean.getOrderno());
            } else {
                ChoosePaymentActivity.RequestParams requestParams = new ChoosePaymentActivity.RequestParams();
                requestParams.couponId = discountView.isCheckedTravelFund() ? "" : getCouponId();
                requestParams.orderId = orderInfoBean.getOrderno();
                requestParams.shouldPay = orderInfoBean.getPriceActual();
                requestParams.payDeadTime = orderInfoBean.getPayDeadTime();
                requestParams.source = source;
                requestParams.needShowAlert = true;
                requestParams.eventPayBean = getChoosePaymentStatisticParams();
                requestParams.orderType = params.orderType;
                requestParams.isOrder = true;
                requestParams.extarParamsBean = getPayResultExtarParamsBean();
                Intent intent = new Intent(activity, ChoosePaymentActivity.class);
                intent.putExtra(Constants.PARAMS_DATA, requestParams);
                intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                startActivity(intent);
            }
        } else if (_request instanceof RequestPayNo) {
            RequestPayNo mParser = (RequestPayNo) _request;
            if (mParser.payType == Constants.PAY_STATE_ALIPAY) {
                if ("travelFundPay".equals(mParser.getData()) || "couppay".equals(mParser.getData())) {
                    PayResultActivity.Params payParams = new PayResultActivity.Params();
                    payParams.payResult = true;
                    payParams.orderId = orderInfoBean.getOrderno();
                    payParams.orderType = params.orderType;
                    payParams.extarParamsBean = getPayResultExtarParamsBean();
                    Intent intent = new Intent(this, PayResultActivity.class);
                    intent.putExtra(Constants.PARAMS_DATA, payParams);
                    intent.putExtra(Constants.PARAMS_SOURCE, "收银台");
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
        if (request instanceof RequestSubmitBase) {
            requestedSubmit = false;
            CommonUtils.apiErrorShowService(OrderActivity.this, errorInfo, request, OrderActivity.this.getEventSource());
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
        if (params.carListBean != null) {
            eventPayBean.isSeckills = params.carListBean.isSeckills;
        }
        if (params.carBean != null) {
            eventPayBean.carType = params.carBean.carDesc;
            eventPayBean.seatCategory = params.carBean.seatCategory;
            eventPayBean.guestcount = params.carBean.capOfPerson + "";
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

    @Override
    public String getEventSource() {
        return "确认订单";
    }

    @Override
    public String getEventId() {
        if (params != null) {
            switch (params.orderType) {
                case 1:
                    if (params.carListBean != null && params.carListBean.isSeckills) {
                        return StatisticConstant.LAUNCH_J2_MS;
                    } else {
                        return StatisticConstant.LAUNCH_J2;
                    }
                case 2:
                    return StatisticConstant.LAUNCH_S2;
                case 3:
                    return StatisticConstant.LAUNCH_C2;
            }
        }
        return super.getEventId();
    }

    @Override
    public Map getEventMap() {
        HashMap map = new HashMap();
        if (!TextUtils.isEmpty(getIntentSource())) {
            map.put(Constants.PARAMS_SOURCE, getIntentSource());
        }
        if (params != null && params.carBean != null) {
            map.put("carstyle", params.carBean.carDesc);
        }
        return map;
    }

    public PayResultExtarParamsBean getPayResultExtarParamsBean() {
        PayResultExtarParamsBean paramsBean = null;
        if (params.orderType == 4) {
            paramsBean = new PayResultExtarParamsBean();
            paramsBean.startPoiBean = params.endPoiBean;
            paramsBean.destPoiBean = params.startPoiBean;
            paramsBean.cityId = params.cityBean.cityId;
            paramsBean.guidesDetailData = params.guidesDetailData;
        } else if (params.orderType == 1) {
            paramsBean = new PayResultExtarParamsBean();
            paramsBean.startPoiBean = params.endPoiBean;
            FlightBean flightBean = params.flightBean;
            AirPort airPort = new AirPort();
            airPort.airportName = flightBean.arrAirportName;
            airPort.airportCode = flightBean.arrivalAirportCode;
            airPort.cityName = flightBean.arrCityName;
            airPort.cityId = flightBean.arrCityId;
            airPort.location = flightBean.arrLocation;
            paramsBean.airPortBean = airPort;
            paramsBean.guidesDetailData = params.guidesDetailData;
        }
        return paramsBean;
    }
}
