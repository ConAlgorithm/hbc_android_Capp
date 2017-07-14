package com.hugboga.custom.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
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
import com.hugboga.custom.data.bean.ManLuggageBean;
import com.hugboga.custom.data.bean.MostFitAvailableBean;
import com.hugboga.custom.data.bean.MostFitBean;
import com.hugboga.custom.data.bean.OrderInfoBean;
import com.hugboga.custom.data.bean.PoiBean;
import com.hugboga.custom.data.bean.SeckillsBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.bean.combination.GroupParamBuilder;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.request.RequestBatchPrice;
import com.hugboga.custom.data.request.RequestCancleTips;
import com.hugboga.custom.data.request.RequestCheckGuide;
import com.hugboga.custom.data.request.RequestDeduction;
import com.hugboga.custom.data.request.RequestMostFit;
import com.hugboga.custom.data.request.RequestNewCars;
import com.hugboga.custom.data.request.RequestOrderGroup;
import com.hugboga.custom.data.request.RequestPayNo;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.bean.EventPayBean;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.AlertDialogUtils;
import com.hugboga.custom.utils.ApiReportHelper;
import com.hugboga.custom.utils.CarUtils;
import com.hugboga.custom.utils.CharterDataUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.PhoneInfo;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.CircularProgress;
import com.hugboga.custom.widget.CombinationOrderDescriptionView;
import com.hugboga.custom.widget.DialogUtil;
import com.hugboga.custom.widget.OrderExplainView;
import com.hugboga.custom.widget.OrderInsuranceView;
import com.hugboga.custom.widget.SkuOrderBottomView;
import com.hugboga.custom.widget.SkuOrderCarTypeView;
import com.hugboga.custom.widget.SkuOrderCountView;
import com.hugboga.custom.widget.SkuOrderDiscountView;
import com.hugboga.custom.widget.SkuOrderEmptyView;
import com.hugboga.custom.widget.SkuOrderTravelerInfoView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by qingcha on 17/3/4.
 */
public class CombinationOrderActivity extends BaseActivity implements SkuOrderCarTypeView.OnSelectedCarListener, SkuOrderDiscountView.DiscountOnClickListener
        , SkuOrderCountView.OnCountChangeListener, SkuOrderBottomView.OnSubmitOrderListener
        , SkuOrderEmptyView.OnRefreshDataListener, SkuOrderEmptyView.OnClickServicesListener{

    public static final String TAG = CombinationOrderActivity.class.getSimpleName();

    public static final String SERVER_TIME = "09:00:00";
    public static final String SERVER_TIME_END = "23:59:59";

    @Bind(R.id.combination_order_scrollview)
    ScrollView scrollView;
    @Bind(R.id.combination_order_description_layout)
    CombinationOrderDescriptionView descriptionLayout;
    @Bind(R.id.combination_order_car_type_view)
    SkuOrderCarTypeView carTypeView;
    @Bind(R.id.combination_order_count_view)
    SkuOrderCountView countView;
    @Bind(R.id.combination_order_traveler_info_view)
    SkuOrderTravelerInfoView travelerInfoView;
    @Bind(R.id.combination_order_discount_view)
    SkuOrderDiscountView discountView;
    @Bind(R.id.combination_order_insurance_view)
    OrderInsuranceView insuranceView;
    @Bind(R.id.combination_order_bottom_view)
    SkuOrderBottomView bottomView;
    @Bind(R.id.combination_order_explain_view)
    OrderExplainView explainView;
    @Bind(R.id.combination_order_empty_layout)
    SkuOrderEmptyView emptyLayout;
    @Bind(R.id.combination_order_progress_view)
    CircularProgress progressView;
    @Bind(R.id.combination_order_seckills_layout)
    RelativeLayout seckillsLayout;

    private CarListBean carListBean;
    private CarBean carBean;
    private int orderType;
    private OrderInfoBean orderInfoBean;

    private DeductionBean deductionBean;

    private MostFitBean mostFitBean;
    private CouponBean couponBean;
    private String couponId;

    private CharterDataUtils charterDataUtils;
    private CityBean startCityBean;
    private ArrayList<GuideCarBean> guideCarBeanList;

    private int requestCouponTag = 0;
    private int requestCancleTipsTag = 0;
    private int requestSucceedCount = 0;

    @Override
    public int getContentViewId() {
        return R.layout.activity_combination_order;
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void initView() {
        initTitleBar();

        charterDataUtils = CharterDataUtils.getInstance();
        startCityBean = charterDataUtils.getStartCityBean(1);

        descriptionLayout.update(charterDataUtils);
        carTypeView.setOnSelectedCarListener(this);
        carTypeView.showLuggageExplain();
        discountView.setDiscountOnClickListener(this);
        countView.setOnCountChangeListener(this);
        bottomView.setOnSubmitOrderListener(this);
        bottomView.setHintTV(3, charterDataUtils.guidesDetailData != null, charterDataUtils.isSeckills());
        emptyLayout.setOnRefreshDataListener(this);
        emptyLayout.setOnClickServicesListener(this);
        explainView.setTermsTextViewVisibility("去支付", View.VISIBLE);
        travelerInfoView.setOrderType(3);

        if (charterDataUtils.isSeckills()) {
            discountView.setVisibility(View.GONE);
            seckillsLayout.setVisibility(View.VISIBLE);
            progressView.setVisibility(View.GONE);
        }

        if (charterDataUtils.guidesDetailData != null) {
            carTypeView.setGuidesDetailData(charterDataUtils.guidesDetailData);
            getGuideCars();
        } else {
            requestBatchPrice();
        }

        StatisticClickEvent.dailyClick(StatisticConstant.LAUNCH_R2, getIntentSource(), charterDataUtils.chooseDateBean.dayNums,
                charterDataUtils.guidesDetailData != null, (charterDataUtils.adultCount + charterDataUtils.childCount) + "");
    }

    public void initTitleBar() {
        initDefaultTitleBar();
        fgTitle.setText("确认订单");
        fgLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftInput();
                finish();
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
                DialogUtil.getInstance(CombinationOrderActivity.this).showServiceDialog(CombinationOrderActivity.this, null, UnicornServiceActivity.SourceType.TYPE_CHARTERED, null, null, getEventSource());
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
                if (startCityBean != null) {
                    bundle.putInt(PoiSearchActivity.KEY_CITY_ID, startCityBean.cityId);
                    bundle.putString(PoiSearchActivity.KEY_LOCATION, startCityBean.location);
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
                } else if (couponBean.couponID.equalsIgnoreCase(couponId)) {
                    break;
                }
                mostFitBean = null;
                discountView.setCouponBean(couponBean);
                break;
            case ORDER_REFRESH://价格或数量变更 刷新
                onRefresh();
                break;
            case ORDER_SECKILLS_ERROR:
                String errorMessage = (String) action.getData();
                showCheckSeckillsDialog(errorMessage);
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
        if (_request instanceof RequestBatchPrice) {
            carListBean = ((RequestBatchPrice) _request).getData();
            if (!checkDataIsEmpty(carListBean == null ? null : carListBean.carList, carListBean.noneCarsState, carListBean.noneCarsReason)) {
                if (charterDataUtils.guidesDetailData != null && guideCarBeanList != null) {
                    ArrayList<CarBean> carList = CarUtils.getCarBeanList(carListBean.carList, guideCarBeanList);
                    if (checkDataIsEmpty(carList)) {
                        return;
                    }
                    carListBean.carList = carList;
                }
                if (charterDataUtils.isSeckills()) {
                    carListBean.isSeckills = true;
                    carListBean.timeLimitedSaleNo = charterDataUtils.seckillsBean.timeLimitedSaleNo;
                    carListBean.timeLimitedSaleScheduleNo = charterDataUtils.seckillsBean.timeLimitedSaleScheduleNo;
                }
                carTypeView.update(carListBean);
            }
            scrollToTop();
        } else if (_request instanceof RequestMostFit) {
            RequestMostFit requestMostFit = (RequestMostFit) _request;
            if (!TextUtils.equals(requestMostFit.tag, "" + requestCouponTag)) {
                return;
            }
            onLoadSucceed();
            mostFitBean = requestMostFit.getData();
            discountView.setMostFitBean(mostFitBean);
        } else if (_request instanceof RequestDeduction) {
            RequestDeduction requestDeduction = (RequestDeduction) _request;
            if (!TextUtils.equals(requestDeduction.tag, "" + requestCouponTag)) {
                return;
            }
            onLoadSucceed();
            deductionBean = requestDeduction.getData();
            discountView.setDeductionBean(deductionBean);
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
        } else if (_request instanceof RequestOrderGroup) {
            orderInfoBean = ((RequestOrderGroup) _request).getData();
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
                requestParams.orderType = orderType;
                requestParams.eventPayBean = getChoosePaymentStatisticParams();
                requestParams.isOrder = true;
                Intent intent = new Intent(this, ChoosePaymentActivity.class);
                intent.putExtra(Constants.PARAMS_DATA, requestParams);
                intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                startActivity(intent);

                charterDataUtils.onDestroy();
                charterDataUtils.cleanGuidesDate();
            }
        } else if (_request instanceof RequestPayNo) {
            RequestPayNo mParser = (RequestPayNo) _request;
            if (mParser.payType == Constants.PAY_STATE_ALIPAY) {
                if ("travelFundPay".equals(mParser.getData()) || "couppay".equals(mParser.getData())) {
                    PayResultActivity.Params params = new PayResultActivity.Params();
                    params.payResult = true;
                    params.orderId =  orderInfoBean.getOrderno();
                    params.orderType = orderType;
                    Intent intent = new Intent(this, PayResultActivity.class);
                    intent.putExtra(Constants.PARAMS_DATA, params);
                    startActivity(intent);
                    SensorsUtils.setSensorsPayResultEvent(getChoosePaymentStatisticParams(), "支付宝", true);
                }
            }
            charterDataUtils.onDestroy();
            charterDataUtils.cleanGuidesDate();
        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        super.onDataRequestError(errorInfo, request);
        if (request.errorType != BaseRequest.ERROR_TYPE_PROCESSED && (request instanceof RequestBatchPrice || request instanceof RequestOrderGroup)) {
            if (request instanceof RequestBatchPrice) {
                RequestBatchPrice requestBatchPrice = (RequestBatchPrice) request;
                if (emptyLayout != null && UrlLibs.API_SECKILLS_BATCH_PRICE.equals(requestBatchPrice.getUrl())) {
                    String errorMessage = ErrorHandler.getErrorMessage(errorInfo, request);
                    emptyLayout.setSeckillsEmpty(errorMessage, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cancelSeckills();
                        }
                    });
                    return;
                }
            }
            String errorCode = ErrorHandler.getErrorCode(errorInfo, request);
            String errorMessage = "很抱歉，该城市暂时无法提供服务(%1$s)";
            checkDataIsEmpty(null, 0, String.format(errorMessage, errorCode));
            return;
        }
        if (request instanceof RequestPayNo) {
            return;
        }
        if (!(request instanceof RequestOrderGroup)) {
            if (emptyLayout != null) {
                emptyLayout.setErrorVisibility(View.VISIBLE);
                progressView.setVisibility(View.GONE);
                setItemVisibility(View.GONE);
            }
        }
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
            eventPayBean.shouldPay = carBean.vehiclePrice + carBean.servicePrice;
        }
        if (orderInfoBean != null) {
            eventPayBean.orderId = orderInfoBean.getOrderno();
            eventPayBean.actualPay = orderInfoBean.getPriceActual();
        }
        if (charterDataUtils != null) {
            eventPayBean.guestcount = (charterDataUtils.adultCount + charterDataUtils.childCount) + "";
            eventPayBean.isSelectedGuide = charterDataUtils.guidesDetailData != null;
            eventPayBean.days = charterDataUtils.chooseDateBean.dayNums;
        }
        return eventPayBean;
    }

    private boolean checkDataIsEmpty(ArrayList<CarBean> _carList) {
        return checkDataIsEmpty(_carList, 0, null);
    }

    private boolean checkDataIsEmpty(ArrayList<CarBean> _carList, int noneCarsState, String noneCarsReason) {
        boolean isEmpty = emptyLayout.setEmptyVisibility(_carList, noneCarsState, noneCarsReason, charterDataUtils.guidesDetailData != null);
        int itemVisibility = !isEmpty ? View.VISIBLE : View.GONE;
        if (isEmpty) {
            progressView.setVisibility(View.GONE);
        }
        setItemVisibility(itemVisibility);
        return isEmpty;
    }

    private void setItemVisibility(int visibility) {
        carTypeView.setVisibility(visibility);
//        countView.setVisibility(visibility);//包车下单不要出行人数
        travelerInfoView.setVisibility(visibility);
        if (charterDataUtils.isSeckills()) {
            discountView.setVisibility(View.GONE);
        } else {
            discountView.setVisibility(visibility);
        }
        insuranceView.setVisibility(visibility);
        bottomView.setVisibility(visibility);
        explainView.setVisibility(visibility);
    }

    private void showCheckSeckillsDialog(String content) {
        AlertDialogUtils.showAlertDialog(this, content, "继续下单", "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                cancelSeckills();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    private void cancelSeckills() {
        SeckillsBean seckillsBean = charterDataUtils.seckillsBean;
        seckillsBean.isSeckills = false;
        finish();
    }

    @Override
    public void onClickServices() {
        DialogUtil.showServiceDialog(CombinationOrderActivity.this, null, UnicornServiceActivity.SourceType.TYPE_CHARTERED, null, null, getEventSource());
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
        if (charterDataUtils.isSeckills()) {
            return;
        }
        requestSucceedCount--;
        if (requestSucceedCount <= 0) {
            onBottomLoading(false);
        }
    }

    @Override
    public void onRefresh() {
        requestBatchPrice();
    }

    @Override
    public void onSelectedCar(CarBean carBean) {
        this.carBean = carBean;
        countView.update(carBean, charterDataUtils, charterDataUtils.chooseDateBean.start_date);
        double additionalPrice = countView.getAdditionalPrice();

        if (charterDataUtils.isSeckills()) {
            bottomView.updatePrice(carBean.seckillingPrice, carBean.price + additionalPrice - carBean.seckillingPrice);
        } else {
            requestSucceedCount = 3;
            onBottomLoading(!carBean.isCallOnClick);
            requestCouponTag++;
            requestCancleTipsTag ++;
            requestMostFit(additionalPrice, requestCouponTag);
            requestTravelFund(additionalPrice, requestCouponTag);
        }

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
        charterDataUtils.adultCount = bean.mans;
        charterDataUtils.childCount = bean.childs;
    }

    /* 儿童座椅价格发生改变 */
    @Override
    public void onAdditionalPriceChange(double price) {
        requestSucceedCount = 2;
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
        mostFitAvailableBean.priceChannel = "" + (carBean.price + countView.getAdditionalPrice());
        mostFitAvailableBean.useOrderPrice = "" + carBean.price;
        mostFitAvailableBean.serviceCityId = startCityBean.cityId + "";
        mostFitAvailableBean.serviceCountryId = startCityBean.areaCode + "";
        mostFitAvailableBean.serviceTime = charterDataUtils.chooseDateBean.start_date + " " + SERVER_TIME;
        mostFitAvailableBean.userId = UserEntity.getUser().getUserId(this);
        mostFitAvailableBean.totalDays = charterDataUtils.chooseDateBean.dayNums + "";
        mostFitAvailableBean.orderType = orderType + "";
        mostFitAvailableBean.carModelId = carBean.carId + "";
        mostFitAvailableBean.isPickupTransfer = charterDataUtils.isPickupTransfer();
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

    @Override
    public void intentTravelFund() {
        Intent intent = new Intent(this, TravelFundActivity.class);
        intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
        startActivity(intent);
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
        if (charterDataUtils.guidesDetailData != null) {
            checkGuideCoflict();
        } else {
            onSubmit();
        }
    }

    public void onSubmit() {
        StatisticClickEvent.selectCarClick(StatisticConstant.SUBMITORDER_R, getIntentSource(), charterDataUtils.chooseDateBean.dayNums,
                charterDataUtils.guidesDetailData != null, carBean.carDesc, countView.getTotalPeople() + "");

        SkuOrderTravelerInfoView.TravelerInfoBean travelerInfoBean = travelerInfoView.getTravelerInfoBean();

        GroupParamBuilder groupParamBuilder = new GroupParamBuilder();
        String requestParams = groupParamBuilder.charterDataUtils(charterDataUtils)
                .carBean(carBean)
                .manLuggageBean(countView.getManLuggageBean())
                .contactUsersBean(travelerInfoBean.getContactUsersBean())
                .isCheckedTravelFund(discountView.isCheckedTravelFund())
                .travelFund(deductionBean != null ? CommonUtils.getCountDouble(deductionBean.deduction) : 0)
                .couponBean(couponBean)
                .mostFitBean(mostFitBean)
                .startPoiBean(travelerInfoBean.poiBean)
                .allChildSeatPrice(countView.getAdditionalPrice())
                .travelerInfoBean(travelerInfoBean)
                .build();
        requestSubmitOrder(requestParams);
    }

    /*
     * 提交订单
    * */
    private void requestSubmitOrder(String requestBody) {
        RequestOrderGroup requestOrderGroup = new RequestOrderGroup(this, requestBody, charterDataUtils.isSeckills());
        requestData(requestOrderGroup);
    }

    /*
    * 获取可服务车辆列表
    * */
    private void requestBatchPrice() {
        RequestBatchPrice request = new RequestBatchPrice(this, charterDataUtils);
        orderType = charterDataUtils.isGroupOrder ? Constants.BUSINESS_TYPE_COMBINATION : 3;
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
                , charterDataUtils.chooseDateBean.start_date + " " + SERVER_TIME
                , carBean.carType + ""
                , carBean.seatCategory + ""
                , startCityBean.cityId + ""
                , startCityBean.areaCode + ""
                , charterDataUtils.chooseDateBean.dayNums + ""
                , carListBean.distance + ""
                , carBean.expectedCompTime == null ? "" : carBean.expectedCompTime
                , orderType + ""
                , carBean.carId + ""
                , charterDataUtils.isPickupTransfer());
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
     * 金额为零，直接请求支付接口（支付宝）
     * */
    private void requestPayNo(String orderNo) {
        RequestPayNo pequestPayNo = new RequestPayNo(this, orderNo, 0, Constants.PAY_STATE_ALIPAY, couponId);
        requestData(pequestPayNo);
    }

    /*
    * 获取退改规则
    * */
    private void requestCancleTips(int requestTag) {
        RequestCancleTips requestCancleTips = new RequestCancleTips(this
                , carBean
                , startCityBean.cityId + ""
                , charterDataUtils.fitstOrderGoodsType + ""
                , carBean.carType + ""
                , carBean.seatCategory + ""
                , charterDataUtils.chooseDateBean.start_date + " " + SERVER_TIME
                , "0"
                , ""
                , ""
                , 3 + "");//orderType + ""
        requestCancleTips.tag = "" + requestTag;
        requestData(requestCancleTips, false);
    }

    @Override
    public String getEventSource() {
        return "组合单下单页";
    }

    private void getGuideCars() {
        RequestNewCars requestCars = new RequestNewCars(this, 1, charterDataUtils.guidesDetailData.guideId, null);
        HttpRequestUtils.request(this, requestCars, new HttpRequestListener() {
            @Override
            public void onDataRequestSucceed(BaseRequest request) {
                ApiReportHelper.getInstance().addReport(request);
                guideCarBeanList = ((RequestNewCars) request).getData();
                if (charterDataUtils.guidesDetailData == null || guideCarBeanList == null || guideCarBeanList.size() <= 0) {
                    checkDataIsEmpty(null);
                    return;
                }
                charterDataUtils.guidesDetailData.guideCars = guideCarBeanList;
                charterDataUtils.guidesDetailData.guideCarCount = guideCarBeanList.size();
                requestBatchPrice();
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
        RequestCheckGuide.CheckGuideBeanList checkGuideBeanList = charterDataUtils.checkGuideBeanList;
        if (checkGuideBeanList == null || checkGuideBeanList.guideSubOrderInfos == null || checkGuideBeanList.guideSubOrderInfos.size() <= 0) {
            onSubmit();
            return;
        }

        String serverTime = travelerInfoView.getTravelerInfoBean() != null ? travelerInfoView.getTravelerInfoBean().serverTime : "";
        serverTime = charterDataUtils.getStartServiceTime(serverTime);

        RequestCheckGuide.CheckGuideBean checkGuideBean = new RequestCheckGuide.CheckGuideBean();
        checkGuideBean.startTime = serverTime;
        checkGuideBean.endTime = charterDataUtils.getEndServiceTime();
        checkGuideBean.cityId = charterDataUtils.getStartCityBean(1).cityId;
        checkGuideBean.guideId = charterDataUtils.guidesDetailData.guideId;
        checkGuideBean.orderType = 3;
        checkGuideBeanList.guideCheckInfos.add(checkGuideBean);

        checkGuideBeanList.updateFirstDayServiceTime(serverTime);

        RequestCheckGuide requestCheckGuide = new RequestCheckGuide(this, checkGuideBeanList);

        HttpRequestUtils.request(this, requestCheckGuide, new HttpRequestListener() {
            @Override
            public void onDataRequestSucceed(BaseRequest request) {
                ApiReportHelper.getInstance().addReport(request);
                onSubmit();
            }

            @Override
            public void onDataRequestCancel(BaseRequest request) {

            }

            @Override
            public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
                CommonUtils.apiErrorShowService(CombinationOrderActivity.this, errorInfo, request, CombinationOrderActivity.this.getEventSource(), false);
            }
        }, true);
    }
}
