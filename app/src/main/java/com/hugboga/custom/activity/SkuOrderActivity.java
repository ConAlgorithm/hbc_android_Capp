package com.hugboga.custom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.huangbaoche.hbcframe.data.net.ErrorHandler;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.AreaCodeBean;
import com.hugboga.custom.data.bean.CarBean;
import com.hugboga.custom.data.bean.CarListBean;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.CouponBean;
import com.hugboga.custom.data.bean.DeductionBean;
import com.hugboga.custom.data.bean.GuideCarBean;
import com.hugboga.custom.data.bean.GuidesDetailData;
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
import com.hugboga.custom.data.request.RequestCheckGuide;
import com.hugboga.custom.data.request.RequestDeduction;
import com.hugboga.custom.data.request.RequestMostFit;
import com.hugboga.custom.data.request.RequestNewCars;
import com.hugboga.custom.data.request.RequestPayNo;
import com.hugboga.custom.data.request.RequestPriceSku;
import com.hugboga.custom.data.request.RequestSubmitBase;
import com.hugboga.custom.data.request.RequestSubmitDaily;
import com.hugboga.custom.data.request.RequestSubmitLine;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.bean.EventPayBean;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.ApiReportHelper;
import com.hugboga.custom.utils.CarUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.utils.OrderUtils;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.CircularProgress;
import com.hugboga.custom.widget.CsDialog;
import com.hugboga.custom.widget.OrderExplainView;
import com.hugboga.custom.widget.OrderInsuranceView;
import com.hugboga.custom.widget.SkuOrderBottomView;
import com.hugboga.custom.widget.SkuOrderCarTypeView;
import com.hugboga.custom.widget.SkuOrderCountView;
import com.hugboga.custom.widget.SkuOrderDescriptionView;
import com.hugboga.custom.widget.SkuOrderDiscountView;
import com.hugboga.custom.widget.SkuOrderEmptyView;
import com.hugboga.custom.widget.SkuOrderTravelerInfoView;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by qingcha on 16/12/16.
 */
public class SkuOrderActivity extends BaseActivity implements SkuOrderCarTypeView.OnSelectedCarListener
        , SkuOrderDiscountView.DiscountOnClickListener
        , SkuOrderCountView.OnCountChangeListener, SkuOrderBottomView.OnSubmitOrderListener
        , SkuOrderEmptyView.OnRefreshDataListener, SkuOrderEmptyView.OnClickServicesListener{

    public static final String TAG = SkuOrderActivity.class.getSimpleName();

    public static final String SERVER_TIME = "09:00";

    @BindView(R.id.sku_order_scrollview)
    ScrollView scrollView;
    @BindView(R.id.sku_order_description_view)
    SkuOrderDescriptionView descriptionView;
    @BindView(R.id.sku_order_car_type_view)
    SkuOrderCarTypeView carTypeView;
    @BindView(R.id.sku_order_count_view)
    SkuOrderCountView countView;
    @BindView(R.id.sku_order_traveler_info_view)
    SkuOrderTravelerInfoView travelerInfoView;
    @BindView(R.id.sku_order_discount_view)
    SkuOrderDiscountView discountView;
    @BindView(R.id.sku_order_insurance_view)
    OrderInsuranceView insuranceView;
    @BindView(R.id.sku_order_bottom_view)
    SkuOrderBottomView bottomView;
    @BindView(R.id.sku_order_explain_view)
    OrderExplainView explainView;
    @BindView(R.id.sku_order_empty_layout)
    SkuOrderEmptyView emptyLayout;
    @BindView(R.id.sku_order_progress_view)
    CircularProgress progressView;

    private CsDialog csDialog;

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

    private GuidesDetailData guidesDetailData;
    private ArrayList<GuideCarBean> guideCarBeanList;

    private double sensorsActualPrice = 0;

    private int requestCouponTag = 0;
    private int requestCancleTipsTag = 0;
    private int requestSucceedCount = 0;
    private int requestCouponCount = 0;

    private boolean requestedSubmit = false;
    private boolean requestPayNo = false;

    public static class Params implements Serializable {
        public SkuItemBean skuItemBean;
        public CityBean cityBean;
        public String serverDate;
        public GuidesDetailData guidesDetailData;
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_sku_order;
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
        serverDate = params.serverDate;

        initTitleBar();
        descriptionView.update(params.skuItemBean, serverDate, params.guidesDetailData);
        carTypeView.setOnSelectedCarListener(this);
        carTypeView.setOrderType(orderType);
        discountView.setDiscountOnClickListener(this);
        countView.setOnCountChangeListener(this);
        bottomView.setOnSubmitOrderListener(this);
        emptyLayout.setOnRefreshDataListener(this);
        emptyLayout.setOnClickServicesListener(this);
        explainView.setTermsTextViewVisibility("去支付", View.VISIBLE);
        travelerInfoView.setOrderType(orderType);

        if (params.guidesDetailData != null) {
            guidesDetailData = params.guidesDetailData;
            carTypeView.setGuidesDetailData(guidesDetailData);
            getGuideCars();
        } else {
            requestPriceSku(serverDate);
        }

        MobClickUtils.onEvent(StatisticConstant.LAUNCH_SKU);
    }

    public void initTitleBar() {
        initDefaultTitleBar();
        fgTitle.setText(R.string.order_title);
        fgLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSaveDialog();
            }
        });
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
                showServiceDialog();
            }
        });
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            showSaveDialog();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    private void showSaveDialog() {
        hideSoftInput();
        OrderUtils.showSaveDialog(this);
    }

    public void showServiceDialog() {
        SensorsUtils.onAppClick(getEventSource(), "客服", getIntentSource());
        //DialogUtil.getInstance(SkuOrderActivity.this).showServiceDialog(SkuOrderActivity.this, null, UnicornServiceActivity.SourceType.TYPE_LINE, null, params.skuItemBean, getEventSource());
        csDialog = CommonUtils.csDialog(SkuOrderActivity.this, null, null, params.skuItemBean, UnicornServiceActivity.SourceType.TYPE_LINE, "线路包车", new CsDialog.OnCsListener() {
            @Override
            public void onCs() {
                if (csDialog != null && csDialog.isShowing()) {
                    csDialog.dismiss();
                }
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
                intent.putExtra("mBusinessType", orderType);
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
            case ORDER_REFRESH://价格或数量变更 刷新
                onRefresh();
                break;
        }
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
        if (_request instanceof RequestPriceSku) {
            carListBean = ((RequestPriceSku) _request).getData();
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
            scrollToTop();
        } else if (_request instanceof RequestMostFit) {
            RequestMostFit requestMostFit = (RequestMostFit) _request;
            if (!TextUtils.equals(requestMostFit.tag, "" + requestCouponTag)) {
                return;
            }
            onLoadSucceed();
            requestCouponCount--;
            mostFitBean = requestMostFit.getData();
            discountView.setMostFitBean(mostFitBean);
        } else if (_request instanceof RequestDeduction) {
            RequestDeduction requestDeduction = (RequestDeduction) _request;
            if (!TextUtils.equals(requestDeduction.tag, "" + requestCouponTag)) {
                return;
            }
            onLoadSucceed();
            requestCouponCount--;
            deductionBean = requestDeduction.getData();
            discountView.setDeductionBean(deductionBean);
        } else if (_request instanceof RequestSubmitBase) {
            orderInfoBean = ((RequestSubmitBase) _request).getData();
            if (orderInfoBean.getPriceActual() == 0) {
                requestPayNo(orderInfoBean.getOrderno());
            } else {
                requestedSubmit = false;
                ChoosePaymentActivity.RequestParams requestParams = new ChoosePaymentActivity.RequestParams();
                requestParams.couponId = discountView.isCheckedTravelFund() ? "" : getCouponId();
                requestParams.orderId = orderInfoBean.getOrderno();
                requestParams.shouldPay = orderInfoBean.getPriceActual();
                requestParams.payDeadTime = orderInfoBean.getPayDeadTime();
                requestParams.source = source;
                requestParams.needShowAlert = true;
                requestParams.eventPayBean = getChoosePaymentStatisticParams();
                requestParams.orderType = orderType;
                requestParams.isOrder = true;
                Intent intent = new Intent(activity, ChoosePaymentActivity.class);
                intent.putExtra(Constants.PARAMS_DATA, requestParams);
                intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                startActivity(intent);
            }
        } else if (_request instanceof RequestPayNo) {
            requestPayNo = false;
            requestedSubmit = false;
            RequestPayNo mParser = (RequestPayNo) _request;
            if (mParser.payType == Constants.PAY_STATE_ALIPAY) {
                if ("travelFundPay".equals(mParser.getData()) || "couppay".equals(mParser.getData())) {
                    PayResultActivity.Params params = new PayResultActivity.Params();
                    params.payResult = true;
                    params.orderId =  orderInfoBean.getOrderno();
                    params.orderType = orderType;
                    Intent intent = new Intent(this, PayResultActivity.class);
                    intent.putExtra(Constants.PARAMS_DATA, params);
                    intent.putExtra(Constants.PARAMS_SOURCE, "收银台");
                    startActivity(intent);
                    SensorsUtils.setSensorsPayResultEvent(getChoosePaymentStatisticParams(), "支付宝", true);
                }
            }
        } else if (_request instanceof RequestCancleTips) {
            RequestCancleTips requestCancleTips = (RequestCancleTips) _request;
            if (!TextUtils.equals(requestCancleTips.tag, "" + requestCancleTipsTag)) {
                return;
            }
            onLoadSucceed();
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
        if (request.errorType != BaseRequest.ERROR_TYPE_PROCESSED && request instanceof RequestPriceSku) {
            String errorCode = ErrorHandler.getErrorCode(errorInfo, request);
            String errorMessage = CommonUtils.getString(R.string.single_errormessage);
            checkDataIsEmpty(null, 0, String.format(errorMessage, errorCode));
            return;
        }
        if (request instanceof RequestPayNo) {
            requestPayNo = false;
            return;
        } else if (request instanceof RequestSubmitBase) {
            requestedSubmit = false;
            CommonUtils.apiErrorShowService(SkuOrderActivity.this, errorInfo, request, SkuOrderActivity.this.getEventSource());
        } else {
            if (emptyLayout != null) {
                emptyLayout.setErrorVisibility(View.VISIBLE);
                progressView.setVisibility(View.GONE);
                setItemVisibility(View.GONE);
            }
        }
    }

    private boolean checkDataIsEmpty(ArrayList<CarBean> _carList) {
        return checkDataIsEmpty(_carList, 0, null);
    }

    private boolean checkDataIsEmpty(ArrayList<CarBean> _carList, int noneCarsState, String noneCarsReason) {
        boolean isEmpty = emptyLayout.setEmptyVisibility(_carList, noneCarsState, noneCarsReason, false, orderType);
        int itemVisibility = !isEmpty ? View.VISIBLE : View.GONE;
        if (isEmpty) {
            progressView.setVisibility(View.GONE);
            setSensorsPriceEvent(false);
        }
        setItemVisibility(itemVisibility);
        return isEmpty;
    }

    private void setItemVisibility(int visibility) {
        carTypeView.setVisibility(visibility);
        countView.setVisibility(visibility);
        travelerInfoView.setVisibility(visibility);
        discountView.setVisibility(visibility);
        bottomView.setVisibility(visibility);
        explainView.setVisibility(visibility);
        insuranceView.setVisibility(visibility);
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

    /* empty重试 */
    @Override
    public void onRefresh() {
        requestPriceSku(serverDate);
    }

    public void onBottomLoading(boolean isLoading) {
        if (isLoading) {
            bottomView.onLoading();
            progressView.setVisibility(View.VISIBLE);
            discountView.setVisibility(View.GONE);
            insuranceView.setVisibility(View.GONE);
            explainView.setVisibility(View.GONE);
        } else {
            bottomView.onSucceed();
            progressView.setVisibility(View.GONE);
            discountView.setVisibility(View.VISIBLE);
            insuranceView.setVisibility(View.VISIBLE);
            explainView.setVisibility(View.VISIBLE);
        }
    }

    public void onLoadSucceed() {
        requestSucceedCount--;
        if (requestSucceedCount == 0) {
            onBottomLoading(false);
        }
    }

    @Override
    public void onClickServices() {
        showServiceDialog();
    }

    /* 选择车辆 */
    @Override
    public void onSelectedCar(CarBean carBean) {
        this.carBean = carBean;
        bottomView.setHintData(carBean.price, orderType, params.guidesDetailData != null, false,
                carBean.reconfirmFlag, carBean.reconfirmTip);
        countView.update(carBean, carListBean, serverDate, params.skuItemBean);
        double additionalPrice = countView.getAdditionalPrice();
        requestSucceedCount = 3;
        requestCouponCount = 2;
        onBottomLoading(!carBean.isCallOnClick);
        requestCouponTag++;
        requestCancleTipsTag ++;
        requestMostFit(additionalPrice, requestCouponTag);
        requestTravelFund(additionalPrice, requestCouponTag);
        requestCancleTips(requestCancleTipsTag);
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
        insuranceView.setInsuranceCount(bean.mans + bean.childs);
    }

    /* 儿童座椅+酒店价格发生改变 */
    @Override
    public void onAdditionalPriceChange(double price) {
        requestSucceedCount = 2;
        requestCouponCount = 2;
        onBottomLoading(true);
        requestCouponTag++;
        requestMostFit(price, requestCouponTag);
        requestTravelFund(price, requestCouponTag);
    }

    /* 选择优惠方式 */
    @Override
    public void chooseDiscount(int type) {
        if (carBean == null) {
            return;
        }
        final double additionalPrice = countView.getAdditionalPrice();
        double totalPrice = carBean.price + additionalPrice;
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
                    actualPrice = carBean.price - deductionPrice + additionalPrice;
                }
                break;
            case SkuOrderDiscountView.TYPE_INVALID:
                break;
        }
        bottomView.updatePrice(actualPrice, deductionPrice);
        sensorsActualPrice = actualPrice;
        if (requestCouponCount == 0) {
            bottomView.setHintTV(actualPrice);
        }
    }

    /* 进入优惠券列表 */
    @Override
    public void intentCouponList() {
        Bundle bundle = new Bundle();
        MostFitAvailableBean mostFitAvailableBean = new MostFitAvailableBean();
        mostFitAvailableBean.carSeatNum = carBean.seatCategory + "";
        mostFitAvailableBean.carTypeId = carBean.carType + "";
        mostFitAvailableBean.distance = carListBean.distance + "";
        mostFitAvailableBean.expectedCompTime = carBean.expectedCompTime == null ? "" : carBean.expectedCompTime;
        mostFitAvailableBean.limit = 20 + "";
        mostFitAvailableBean.offset = 0 + "";
        String channelPrice = "" + (carBean.price + countView.getAdditionalPrice());
        mostFitAvailableBean.priceChannel = channelPrice;
        mostFitAvailableBean.useOrderPrice = channelPrice;
        mostFitAvailableBean.serviceCityId = params.cityBean.cityId + "";
        mostFitAvailableBean.serviceCountryId = params.cityBean.areaCode + "";
        mostFitAvailableBean.serviceTime = serverDate + " " + "00:00:00";
        mostFitAvailableBean.userId = UserEntity.getUser().getUserId(this);
        mostFitAvailableBean.totalDays = params.skuItemBean.daysCount + "";
        mostFitAvailableBean.orderType = params.skuItemBean.goodsClass == 1 ? "5" : "6";
        mostFitAvailableBean.carModelId = carBean.carId + "";
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

    /* 去支付 */
    @Override
    public void onSubmitOrder() {
        hideSoftInput();
        if (!travelerInfoView.checkTravelerInfo()) {
            return;
        }
        if (!CommonUtils.isLogin(this,getEventSource())) {
            return;
        }
        if (guidesDetailData != null) {
            checkGuideCoflict();
        } else {
            requestSubmitOrder();
        }
        StatisticClickEvent.click(StatisticConstant.SUBMITORDER_SKU);
        SensorsUtils.onAppClick(getEventSource(), "去支付", getIntentSource());
    }

    public String getCouponId() {
        if (null != mostFitBean) {
            couponId = mostFitBean.couponId;
        } else if (null != couponBean) {
            couponId = couponBean.couponID;
        }
        return couponId;
    }

    /*
    * 获取车辆列表
    * @params serverDate 出发日期
    * */
    private void requestPriceSku(String serverDate) {
        this.serverDate = serverDate;
        String serverDayTime = serverDate + " " + SERVER_TIME + ":00";
        RequestPriceSku request = new RequestPriceSku(this
                , params.skuItemBean.goodsNo
                , serverDayTime
                , "" + params.cityBean.cityId
                , guidesDetailData == null ? "" : guidesDetailData.getCarIds()
                , guidesDetailData == null ? 0 : guidesDetailData.isQuality);
        requestData(request);
    }

    /*
    * 获取优惠券
    * @params additionalPrice 儿童座椅 + 酒店价格
    * */
    private void requestMostFit(double additionalPrice, int requestTag) {
        RequestMostFit requestMostFit = new RequestMostFit(this
                , carBean.price + additionalPrice + ""
                , carBean.price + additionalPrice + ""
                , serverDate + " " + "00:00:00"
                , carBean.carType + ""
                , carBean.seatCategory + ""
                , params.cityBean.cityId + ""
                , params.cityBean.areaCode + ""
                , params.skuItemBean.daysCount + ""
                , carListBean.distance + ""
                , carBean.expectedCompTime == null ? "" : carBean.expectedCompTime
                , orderType + ""
                , carBean.carId + ""
                , null);
        requestMostFit.tag = "" + requestTag;
        requestData(requestMostFit, false);
    }

    /*
    * 获取旅游基金
    * @params additionalPrice 儿童座椅 + 酒店价格
    * */
    private void requestTravelFund(double additionalPrice, int requestTag) {
        RequestDeduction requestDeduction = new RequestDeduction(this, carBean.price + additionalPrice + "");
        requestDeduction.tag = "" + requestTag;
        requestData(requestDeduction, false);
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
        orderBean = getSKUOrderByInput();
        switch (orderType) {
            case 5:
                RequestSubmitDaily requestSubmitBase = new RequestSubmitDaily(this, orderBean, false);
                requestData(requestSubmitBase);
                break;
            case 6:
                RequestSubmitLine requestSubmitLine = new RequestSubmitLine(this, orderBean);
                requestData(requestSubmitLine);
                break;
        }
        setSensorsEvent();
    }

    /*
    * 金额为零，直接请求支付接口（支付宝）
    * */
    private void requestPayNo(String orderNo) {
        if (requestPayNo) {
            return;
        } else {
            requestPayNo = true;
        }
        RequestPayNo pequestPayNo = new RequestPayNo(this, orderNo, 0, Constants.PAY_STATE_ALIPAY, discountView.isCheckedTravelFund() ? "" : getCouponId());
        requestData(pequestPayNo);
    }

    private void getGuideCars() {
        RequestNewCars requestCars = new RequestNewCars(this, 5, guidesDetailData.guideId, null);
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
                requestPriceSku(serverDate);
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
        checkGuideBean.startTime = serverDate + " " + "00:00:00";
        checkGuideBean.endTime = DateUtils.getDay(serverDate, params.skuItemBean.daysCount - 1) + " " + "23:59:59";
        checkGuideBean.cityId = params.cityBean != null ? params.cityBean.cityId : params.skuItemBean.arrCityId;
        checkGuideBean.guideId = guidesDetailData.guideId;
        checkGuideBean.orderType = orderType;

        RequestCheckGuide requestCheckGuide = new RequestCheckGuide(this, checkGuideBean, params.skuItemBean.goodsNo);
        HttpRequestUtils.request(this, requestCheckGuide, new HttpRequestListener() {
            @Override
            public void onDataRequestSucceed(BaseRequest request) {
                ApiReportHelper.getInstance().addReport(request);
                requestSubmitOrder();
            }

            @Override
            public void onDataRequestCancel(BaseRequest request) {

            }

            @Override
            public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
                if (isFinishing()) {
                    return;
                }
                CommonUtils.apiErrorShowService(SkuOrderActivity.this, errorInfo, request, SkuOrderActivity.this.getEventSource(), false);
            }
        }, true);
    }

    /*
    * 获取退改规则
    * */
    private void requestCancleTips(int requestTag) {
        RequestCancleTips requestCancleTips = new RequestCancleTips(this
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
        requestCancleTips.tag = "" + requestTag;
        requestData(requestCancleTips, false);
    }

    /*
    * 下单参数，参考OrderNewActivity
    * */
    private OrderBean getSKUOrderByInput() {
        ManLuggageBean manLuggageBean = countView.getManLuggageBean();
        SkuOrderTravelerInfoView.TravelerInfoBean travelerInfoBean = travelerInfoView.getTravelerInfoBean();

        return new OrderUtils().getSKUOrderByInput(params.guidesDetailData != null ? params.guidesDetailData.guideId : ""
                , params.skuItemBean
                , serverDate
                , TextUtils.isEmpty(travelerInfoBean.serverTime) ? SERVER_TIME : travelerInfoBean.serverTime
                , carListBean.distance + ""
                , carBean
                , manLuggageBean.mans + ""
                , manLuggageBean.childs + ""
                , params.cityBean
                , OrderUtils.getPassCityStr(params.skuItemBean)
                , travelerInfoBean.getContactUsersBean()
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
                , manLuggageBean.luggages + ""
                , travelerInfoBean.wechatNo
                , countView.getExtrasPrice()
                , countView.getTotalExtrasPrice());
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
        return "确认订单";
    }

    //神策统计_确认行程
    private void setSensorsEvent() {
        try {
            JSONObject properties = new JSONObject();
            String skuType = "";
            switch (orderBean.orderType) {
                case 1:
                    skuType = "接机";
                    break;
                case 2:
                    skuType = "送机";
                    break;
                case 3:
                    skuType = "按天包车游";
                    properties.put("hbc_start_time", orderBean.serviceTime);
                    break;
                case 4:
                    skuType = "单次接送";
                    break;
                case 5:
                    skuType = "固定线路";
                    properties.put("hbc_adultNum", orderBean.adult);
                    properties.put("hbc_childNum", orderBean.child);
                    properties.put("hbc_childseatNum", orderBean.childSeatNum);
                    properties.put("hbc_car_type", orderBean.carType + "");
                    properties.put("hbc_start_time", orderBean.serviceTime);
                    properties.put("hbc_sku_id", orderBean.goodsNo);
                    properties.put("hbc_sku_name", orderBean.lineSubject);
                    if (null != orderBean.orderPriceInfo && orderBean.orderPriceInfo.priceHotel != 0.0) {
                        properties.put("hbc_room_average", orderBean.orderPriceInfo.priceHotel);
                        properties.put("hbc_room_num", orderBean.hotelRoom);
                        properties.put("hbc_room_totalprice", orderBean.hotelRoom * orderBean.orderPriceInfo.priceHotel);
                    }
                    break;
                case 6:
                    skuType = "推荐线路";
                    properties.put("hbc_adultNum", orderBean.adult);
                    properties.put("hbc_childNum", orderBean.child);
                    properties.put("hbc_childseatNum", orderBean.childSeatNum);
                    properties.put("hbc_car_type", orderBean.carType + "");
                    properties.put("hbc_start_time", orderBean.serviceTime);
                    properties.put("hbc_sku_id", orderBean.goodsNo);
                    properties.put("hbc_sku_name", orderBean.lineSubject);
                    if (null != orderBean.orderPriceInfo && orderBean.orderPriceInfo.priceHotel != 0.0) {
                        properties.put("hbc_room_average", orderBean.orderPriceInfo.priceHotel);
                        properties.put("hbc_room_num", orderBean.hotelRoom);
                        properties.put("hbc_room_totalprice", orderBean.hotelRoom * orderBean.orderPriceInfo.priceHotel);
                    }
                    break;
            }
            properties.put("hbc_sku_type", skuType);
            properties.put("hbc_price_total", Double.valueOf(orderBean.priceChannel));//费用总计
            if (TextUtils.isEmpty(orderBean.coupPriceInfo)) {
                properties.put("hbc_price_coupon", "0");//使用优惠券
            } else {
                properties.put("hbc_price_coupon", orderBean.coupPriceInfo);//使用优惠券
            }
            if (TextUtils.isEmpty(orderBean.travelFund)) {
                properties.put("hbc_price_tra_fund", 0);//使用旅游基金
            } else {
                properties.put("hbc_price_tra_fund", orderBean.travelFund);//使用旅游基金
            }
//            int priceActual = CommonUtils.getCountInteger(orderBean.priceChannel) - CommonUtils.getCountInteger(orderBean.coupPriceInfo) - CommonUtils.getCountInteger(orderBean.travelFund);
//            if (priceActual < 0) {
//                priceActual = 0;
//            }
            properties.put("hbc_price_actually", sensorsActualPrice);//实际支付金额
            properties.put("hbc_is_appoint_guide", orderBean.guideCollectId == null ? false : true);//指定司导下单
            SensorsDataAPI.sharedInstance(this).track("buy_submitorder", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //神策统计_展示报价
    private void setSensorsPriceEvent(boolean isHavePrice) {
        String guideId = guidesDetailData != null ? guidesDetailData.guideId : "";
        SensorsUtils.setSensorsPriceEvent("" + orderType, guideId, isHavePrice);
    }
}
