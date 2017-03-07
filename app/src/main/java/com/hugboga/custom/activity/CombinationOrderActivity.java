package com.hugboga.custom.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
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
import com.hugboga.custom.data.bean.CouponBean;
import com.hugboga.custom.data.bean.DeductionBean;
import com.hugboga.custom.data.bean.ManLuggageBean;
import com.hugboga.custom.data.bean.MostFitAvailableBean;
import com.hugboga.custom.data.bean.MostFitBean;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.OrderInfoBean;
import com.hugboga.custom.data.bean.PoiBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.request.RequestBatchPrice;
import com.hugboga.custom.data.request.RequestCancleTips;
import com.hugboga.custom.data.request.RequestDeduction;
import com.hugboga.custom.data.request.RequestMostFit;
import com.hugboga.custom.data.request.RequestPayNo;
import com.hugboga.custom.data.request.RequestSubmitBase;
import com.hugboga.custom.utils.AlertDialogUtils;
import com.hugboga.custom.utils.CharterDataUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.PhoneInfo;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.DialogUtil;
import com.hugboga.custom.widget.OrderExplainView;
import com.hugboga.custom.widget.SkuOrderBottomView;
import com.hugboga.custom.widget.SkuOrderCarTypeView;
import com.hugboga.custom.widget.SkuOrderCountView;
import com.hugboga.custom.widget.SkuOrderDiscountView;
import com.hugboga.custom.widget.SkuOrderEmptyView;
import com.hugboga.custom.widget.SkuOrderTravelerInfoView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/3/4.
 */
public class CombinationOrderActivity extends BaseActivity implements SkuOrderCarTypeView.OnSelectedCarListener, SkuOrderDiscountView.DiscountOnClickListener
        , SkuOrderCountView.OnCountChangeListener, SkuOrderBottomView.OnSubmitOrderListener, SkuOrderEmptyView.OnRefreshDataListener{

    public static final String SERVER_TIME = "00:00:00";
    public static final String SERVER_TIME_END = "23:59:59";
    public static final int REQUEST_CODE_PICK_CONTACTS = 101;

    @Bind(R.id.combination_order_scrollview)
    ScrollView scrollView;
    @Bind(R.id.combination_order_car_type_view)
    SkuOrderCarTypeView carTypeView;
    @Bind(R.id.combination_order_count_view)
    SkuOrderCountView countView;
    @Bind(R.id.combination_order_traveler_info_view)
    SkuOrderTravelerInfoView travelerInfoView;
    @Bind(R.id.combination_order_discount_view)
    SkuOrderDiscountView discountView;
    @Bind(R.id.combination_order_bottom_view)
    SkuOrderBottomView bottomView;
    @Bind(R.id.combination_order_explain_view)
    OrderExplainView explainView;
    @Bind(R.id.combination_order_empty_layout)
    SkuOrderEmptyView emptyLayout;

    private CarListBean carListBean;
    private CarBean carBean;
    private int orderType = Constants.BUSINESS_TYPE_COMBINATION;
    private OrderInfoBean orderInfoBean;

    private DeductionBean deductionBean;

    private MostFitBean mostFitBean;
    private CouponBean couponBean;
    private String couponId;

    private CharterDataUtils charterDataUtils;
    private CityBean startCityBean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combination_order);
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
    }

    private void initView() {
        initTitleBar();

        charterDataUtils = CharterDataUtils.getInstance();
        startCityBean = charterDataUtils.getStartCityBean(1);

        carTypeView.setOnSelectedCarListener(this);
        discountView.setDiscountOnClickListener(this);
        countView.setOnCountChangeListener(this);
        bottomView.setOnSubmitOrderListener(this);
        emptyLayout.setOnRefreshDataListener(this);
        explainView.setTermsTextViewVisibility("去支付", View.VISIBLE);

        requestBatchPrice();
    }

    public void initTitleBar() {
        initDefaultTitleBar();
        fgTitle.setText("确认订单");
        fgLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftInput();
                AlertDialogUtils.showAlertDialog(CombinationOrderActivity.this, getString(R.string.back_alert_msg), "离开", "取消", new DialogInterface.OnClickListener() {
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
        fgRightTV.setVisibility(View.GONE);

        RelativeLayout.LayoutParams headerRightImageParams = new RelativeLayout.LayoutParams(UIUtils.dip2px(38), UIUtils.dip2px(38));
        headerRightImageParams.rightMargin = UIUtils.dip2px(18);
        headerRightImageParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        headerRightImageParams.addRule(RelativeLayout.CENTER_VERTICAL);
        fgRightBtn.setLayoutParams(headerRightImageParams);
        fgRightBtn.setPadding(0,0,0,0);
        fgRightBtn.setImageResource(R.mipmap.icon_service);
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
                travelerInfoView.setAreaCode(areaCodeBean.getCode());
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
                if (couponBean.couponID.equalsIgnoreCase(couponId)) {
                    couponId = null;
                    couponBean = null;
                }
                mostFitBean = null;
                discountView.setCouponBean(couponBean);
                break;
            case SKU_ORDER_REFRESH://价格或数量变更 刷新
                onRefresh();
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
        if (_request instanceof RequestBatchPrice) {
            carListBean = ((RequestBatchPrice) _request).getData();
            if (!checkDataIsEmpty(carListBean)) {
                carTypeView.update(carListBean);
            }
            scrollToTop();
        } else if (_request instanceof RequestMostFit) {
            mostFitBean = ((RequestMostFit) _request).getData();
            discountView.setMostFitBean(mostFitBean);
        } else if (_request instanceof RequestDeduction) {
            deductionBean = ((RequestDeduction) _request).getData();
            discountView.setDeductionBean(deductionBean);
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
        if (request instanceof RequestSubmitBase || request instanceof RequestPayNo) {
            return;
        }
        emptyLayout.setErrorVisibility(View.VISIBLE);
        setItemVisibility(View.GONE);
    }

    private boolean checkDataIsEmpty(CarListBean carListBean) {
        boolean isEmpty = false;
        if (carListBean == null || carListBean.carList == null || carListBean.carList.size() <= 0) {
            isEmpty = true;
        } else {
            isEmpty = false;
        }
        emptyLayout.setEmptyVisibility(isEmpty ? View.VISIBLE : View.GONE);

        int itemVisibility = !isEmpty ? View.VISIBLE : View.GONE;
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
    public void onRefresh() {
        requestBatchPrice();
    }

    @Override
    public void onSelectedCar(CarBean carBean) {
        this.carBean = carBean;
        countView.update(carBean, carListBean, charterDataUtils.chooseDateBean.start_date, null);
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
        mostFitAvailableBean.expectedCompTime = (null == carBean.expectedCompTime) ? "" : carBean.expectedCompTime + "";
        mostFitAvailableBean.limit = 20 + "";
        mostFitAvailableBean.offset = 0 + "";
        String channelPrice = "" + (carBean.price + countView.getAdditionalPrice());
        mostFitAvailableBean.priceChannel = channelPrice;
        mostFitAvailableBean.useOrderPrice = channelPrice;
        mostFitAvailableBean.serviceCityId = startCityBean.cityId + "";
        mostFitAvailableBean.serviceCountryId = startCityBean.areaCode + "";
        mostFitAvailableBean.serviceLocalDays = "0";
        mostFitAvailableBean.serviceNonlocalDays = charterDataUtils.chooseDateBean.dayNums + "";
        mostFitAvailableBean.serviceTime = charterDataUtils.chooseDateBean.start_date + " " + SERVER_TIME;
        mostFitAvailableBean.userId = UserEntity.getUser().getUserId(this);
        mostFitAvailableBean.totalDays = charterDataUtils.chooseDateBean.dayNums + "";
        mostFitAvailableBean.orderType = orderType + "";
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

    @Override
    public void intentTravelFund() {
        Intent intent = new Intent(this, TravelFundActivity.class);
        intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
        startActivity(intent);
    }

    @Override
    public void onSubmitOrder() {

    }

    /*
    * 获取可服务车辆列表
    * */
    private void requestBatchPrice() {
        RequestBatchPrice request = new RequestBatchPrice(this, charterDataUtils);
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
                , charterDataUtils.chooseDateBean.start_date + " " + SERVER_TIME
                , carBean.carType + ""
                , carBean.seatCategory + ""
                , startCityBean.cityId + ""
                , startCityBean.areaCode + ""
                , charterDataUtils.chooseDateBean.dayNums + ""
                , carListBean.distance + ""
                , charterDataUtils.chooseDateBean.dayNums + ""
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
                , carBean
                , startCityBean.cityId + ""
                , ""
                , carBean.carType + ""
                , carBean.seatCategory + ""
                , charterDataUtils.chooseDateBean.start_date + " " + SERVER_TIME
                , "0"
                , ""
                , ""
                , orderType + "");
        requestData(requestCancleTips);
    }

    @Override
    public String getEventSource() {
        return "组合单下单页";
    }


}
