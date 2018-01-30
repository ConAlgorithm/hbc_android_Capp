package com.hugboga.custom.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.DefaultSSLSocketFactory;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.AirPort;
import com.hugboga.custom.data.bean.DeliverInfoBean;
import com.hugboga.custom.data.bean.GuidesDetailData;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.OrderGuideInfo;
import com.hugboga.custom.data.bean.OrderPriceInfo;
import com.hugboga.custom.data.bean.OrderStatus;
import com.hugboga.custom.data.bean.PayResultExtarParamsBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.request.RequestOrderDetail;
import com.hugboga.custom.data.request.RequestPayNo;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.bean.EventPayBean;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.statistic.event.EventBase;
import com.hugboga.custom.statistic.event.EventCancelOrder;
import com.hugboga.custom.statistic.event.EventPay;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.widget.CompatPopupWindow;
import com.hugboga.custom.widget.CsDialog;
import com.hugboga.custom.widget.DialogUtil;
import com.hugboga.custom.widget.HbcViewBehavior;
import com.hugboga.custom.widget.OrderDetailBargainEntr;
import com.hugboga.custom.widget.OrderDetailDeliverView;
import com.hugboga.custom.widget.OrderDetailFloatView;
import com.hugboga.custom.widget.OrderDetailItineraryView;
import com.hugboga.custom.widget.OrderDetailTitleBar;
import com.hugboga.custom.widget.OrderDetailTravelGroup;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by qingcha on 16/8/2.
 */
public class OrderDetailActivity extends BaseActivity implements View.OnClickListener {

    public static final int EVALUATE_TYPE = 1000;
    public static final String SOURCE_CLASS = "source_class";

    @BindView(R.id.order_detail_title_layout)
    OrderDetailTitleBar titleBar;

    @BindView(R.id.order_detail_deliver_view)
    OrderDetailDeliverView deliverView;

    @BindView(R.id.order_detail_itinerary_view)
    OrderDetailItineraryView itineraryView;

    @BindView(R.id.order_detail_float_view)
    OrderDetailFloatView floatView;

    @BindView(R.id.order_detail_group_layout)
    LinearLayout groupLayout;

    @BindView(R.id.order_detail_empty_tv)
    TextView emptyTV;

    @BindView(R.id.order_detail_explain_tv)
    TextView explainTV;

    @BindView(R.id.order_detail_bargain_entr_view)
    OrderDetailBargainEntr bargainEntrView;

    private CompatPopupWindow popup;
    private View menuLayout;
    private Params params;
    private OrderBean orderBean;
    private DialogUtil mDialogUtil;

    private boolean isAppointGuideSucceed = false;
    CsDialog csDialog;

    public static class Params implements Serializable {
        public String orderId;
        public String source;
        public int orderType;
        public String subOrderId;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fg_order_detail;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            params = (Params) savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                params = (Params) bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }
        source = getIntentSource();
        EventBus.getDefault().register(this);

        mDialogUtil = DialogUtil.getInstance(this);
        titleBar.setTitle(params.orderType);
        emptyTV.setVisibility(View.VISIBLE);
        requestData();

        Map map = new HashMap();
        map.put(Constants.PARAMS_SOURCE, source);
        MobClickUtils.onEvent(getEventId(), map);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mDialogUtil != null) {
            mDialogUtil.dismissDialog();
        }
        try {
            if (popup != null && popup.isShowing()) {
                popup.dismiss();
            }
        } catch (Exception e) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        deliverView.refreshData(false);
        DefaultSSLSocketFactory.resetSSLSocketFactory(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (params != null) {
            outState.putSerializable(Constants.PARAMS_DATA, params);
        }
    }

    private void requestData() {
        RequestOrderDetail request = new RequestOrderDetail(this, params.orderId);
        requestData(request);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_detail_empty_tv:
                emptyTV.setOnClickListener(null);
                emptyTV.setText("");
                requestData();
                break;
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        super.onDataRequestSucceed(_request);
        if (_request instanceof RequestOrderDetail) {
            emptyTV.setVisibility(View.GONE);
            RequestOrderDetail mParser = (RequestOrderDetail) _request;
            orderBean = mParser.getData();
            titleBar.update(orderBean);
            floatView.update(orderBean);
            bargainEntrView.update(orderBean);
            final int count = groupLayout.getChildCount();
            for (int i = 0; i < count; i++) {
                View item = groupLayout.getChildAt(i);
                if (item instanceof HbcViewBehavior) {
                    ((HbcViewBehavior) item).update(orderBean);
                }
                if (item instanceof OrderDetailTravelGroup) {
                    ((OrderDetailTravelGroup) item).onChangeSubOrder(params.subOrderId);
                    params.subOrderId = null;
                }
            }

            if (orderBean.cancelRules != null && orderBean.cancelRules.size() > 0) {
                String explainStr = "";
                for (int i = 0; i < orderBean.cancelRules.size(); i++) {
                    explainStr += orderBean.cancelRules.get(i);
                }
                explainTV.setText(explainStr);
            }

            if (isAppointGuideSucceed && orderBean.orderGuideInfo != null) {
                isAppointGuideSucceed = false;
            }
        } else if (_request instanceof RequestPayNo) {
            RequestPayNo mParser = (RequestPayNo) _request;
            if (mParser.payType == Constants.PAY_STATE_ALIPAY) {
                if ("travelFundPay".equals(mParser.getData()) || "couppay".equals(mParser.getData())) {//支付宝使用旅游基金和优惠券0元支付
                    PayResultActivity.Params params = new PayResultActivity.Params();
                    params.payResult = true;
                    params.orderId = orderBean.orderNo;
                    params.orderType = orderBean.orderType;
                    params.extarParamsBean = getPayResultExtarParamsBean();
                    Intent intent = new Intent(OrderDetailActivity.this, PayResultActivity.class);
                    intent.putExtra(Constants.PARAMS_DATA, params);
                    intent.putExtra(Constants.PARAMS_SOURCE, "收银台");
                    startActivity(intent);
                    EventPayBean eventPayBean = new EventPayBean();
                    eventPayBean.transform(orderBean);
                    SensorsUtils.setSensorsPayResultEvent(eventPayBean, "支付宝", true);
                }
            }
        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest _request) {
        super.onDataRequestError(errorInfo, _request);
        if (_request instanceof RequestOrderDetail) {
            if (emptyTV != null) {
                emptyTV.setVisibility(View.VISIBLE);
                emptyTV.setText(R.string.data_load_error_retry);
                emptyTV.setOnClickListener(this);
            }
        }
    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        Intent intent = null;
        switch (action.getType()) {
            case ORDER_DETAIL_BACK://返回
                finish();
                break;
            case ORDER_DETAIL_MORE://更多
                showPopupWindow();
                break;
            case ORDER_DETAIL_CALL://联系客服
                csDialog = CommonUtils.csDialog(this, orderBean, null, null, UnicornServiceActivity.SourceType.TYPE_ORDER, getEventSource(), new CsDialog.OnCsListener() {
                    @Override
                    public void onCs() {
                        if (csDialog != null && csDialog.isShowing()) {
                            csDialog.dismiss();
                        }
                    }
                });
                break;
            case ORDER_DETAIL_PAY://立即支付
                if (!eventVerification(action)) {
                    break;
                }
                if (orderBean == null) {
                    return;
                }
                String couponId = orderBean.coupId;
                OrderPriceInfo priceInfo = orderBean.orderPriceInfo;
                EventPayBean eventPayBean = new EventPayBean();
                eventPayBean.transform(orderBean);
                if (priceInfo.actualPay == 0) {
                    RequestPayNo request = new RequestPayNo(this, orderBean.orderNo, 0, Constants.PAY_STATE_ALIPAY, couponId);
                    requestData(request);
                    MobClickUtils.onEvent(new EventPay(eventPayBean));
                } else {
                    ChoosePaymentActivity.RequestParams requestParams = new ChoosePaymentActivity.RequestParams();
                    requestParams.orderId = orderBean.orderNo;
                    requestParams.shouldPay = orderBean.orderPriceInfo.actualPay;
                    requestParams.payDeadTime = orderBean.payDeadTime;
                    requestParams.source = source;
                    requestParams.couponId = couponId;
                    requestParams.eventPayBean = eventPayBean;
                    requestParams.orderType = orderBean.orderType;
                    requestParams.extarParamsBean = getPayResultExtarParamsBean();
                    intent = new Intent(OrderDetailActivity.this, ChoosePaymentActivity.class);
                    intent.putExtra(Constants.PARAMS_DATA, requestParams);
                    intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                    startActivity(intent);
                    setSensorsEvent();
                }
                break;
            case ORDER_DETAIL_ROUTE://路线详情
                if (!eventVerification(action)) {
                    break;
                }
                if (orderBean == null) {
                    return;
                }
                intent = new Intent(this, SkuDetailActivity.class);
                intent.putExtra(WebInfoActivity.WEB_URL, orderBean.skuDetailUrl);
                intent.putExtra(Constants.PARAMS_ID, orderBean.goodsNo);
                intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                startActivity(intent);
                if (orderBean.orderGoodsType == 3) {//固定线路
                    StatisticClickEvent.click(StatisticConstant.CLICK_RG, "订单详情页");
                } else {
                    StatisticClickEvent.click(StatisticConstant.CLICK_RT, "订单详情页");
                }
                break;
            case ORDER_DETAIL_UPDATE_COLLECT://更新收藏UI
                requestData();
                break;
            case ORDER_DETAIL_UPDATE_EVALUATION://更新评价UI
//                if (!eventVerification(action)) {
//                    break;
//                }
                requestData();
                break;
            case ORDER_DETAIL_UPDATE_INFO://更新个人信息UI
                if (!eventVerification(action)) {
                    break;
                }
                requestData();
                break;
            case ADD_INSURE_SUCCESS://更新添加投保人
                if (!eventVerification(action)) {
                    break;
                }
                if (orderBean.orderNo.equals(action.getData())) {
                    requestData();
                }
                break;
            case ORDER_DETAIL_UPDATE://更新数据
                if (!eventVerification(action)) {
                    break;
                }
                requestData();
                break;
            case ORDER_DETAIL_GUIDE_SUCCEED:
                if (!eventVerification(action)) {
                    break;
                }
                isAppointGuideSucceed = true;
            default:
                break;
        }
    }

    private boolean eventVerification(EventAction action) {
        return orderBean != null && orderBean.orderNo.equals(action.getData());
    }

    /**
     * 右上角的菜单，取消订单 联系客服，此部分copy自旧代码
     */
    public void showPopupWindow() {
        if (popup != null && popup.isShowing()) {
            return;
        }
        if (menuLayout == null) {
            menuLayout = LayoutInflater.from(this).inflate(R.layout.popup_top_right_menu, null);
        }
        TextView cancelOrderTV = (TextView) menuLayout.findViewById(R.id.cancel_order);
        TextView commonProblemTV = (TextView) menuLayout.findViewById(R.id.menu_phone);
        commonProblemTV.setText(R.string.order_detail_problem);
        if (orderBean.orderStatus.code <= 5) {
            cancelOrderTV.setVisibility(View.VISIBLE);
        } else {
            cancelOrderTV.setVisibility(View.GONE);
        }

        if (popup != null) {
            popup.showAsDropDown(titleBar);
            return;
        }


        popup = new CompatPopupWindow(menuLayout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popup.setBackgroundDrawable(new BitmapDrawable());
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        popup.showAsDropDown(titleBar);

        menuLayout.findViewById(R.id.bg_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });

        cancelOrderTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SensorsUtils.onAppClick(getEventSource(), "取消订单", getIntentSource());
                mDialogUtil = DialogUtil.getInstance(OrderDetailActivity.this);
                //如果此订单不能取消，直接进行提示
                popup.dismiss();
                if (orderBean.cancelable) {//cancelable是否能取消
                    String tip = "";
                    if (orderBean.orderStatus == OrderStatus.INITSTATE) {
                        tip = getString(R.string.order_cancel_tip);
                    } else if (orderBean.isChangeManual) {//需要人工取消订单
                        //DialogUtil.showDefaultServiceDialog(OrderDetailActivity.this, "如需要取消订单，请联系客服处理", getEventSource());
                        csDialog = CommonUtils.csDialog(OrderDetailActivity.this, null, CommonUtils.getString(R.string.order_detail_cancel_hint), null, UnicornServiceActivity.SourceType.TYPE_DEFAULT, getEventSource(), new CsDialog.OnCsListener() {
                            @Override
                            public void onCs() {
                                if (csDialog != null && csDialog.isShowing()) {
                                    csDialog.dismiss();
                                }
                            }
                        });
                        return;
                    } else {
                        tip = orderBean.cancelTip;
                    }
                    mDialogUtil.showCustomDialog(getString(R.string.app_name), tip, CommonUtils.getString(R.string.hbc_confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EventBase eventBase = new EventCancelOrder(orderBean);
                            MobClickUtils.onEvent(eventBase);
                            Intent intent = new Intent(OrderDetailActivity.this, OrderCancelReasonActivity.class);
                            intent.putExtra(Constants.PARAMS_DATA, orderBean);
                            OrderDetailActivity.this.startActivity(intent);
                        }
                    }, CommonUtils.getString(R.string.hbc_back), null);
                } else {
                    mDialogUtil.showCustomDialog(orderBean.cancelText);
                }
            }
        });

        commonProblemTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SensorsUtils.onAppClick(getEventSource(), "常见问题", getIntentSource());
                Intent intent = new Intent(OrderDetailActivity.this, WebInfoActivity.class);
                intent.putExtra(WebInfoActivity.WEB_URL, UrlLibs.H5_PROBLEM);
                intent.putExtra(WebInfoActivity.CONTACT_SERVICE, true);
                OrderDetailActivity.this.startActivity(intent);
                popup.dismiss();
            }
        });
    }

    @Override
    public String getEventSource() {
        if (getIntent() != null && !TextUtils.isEmpty(getIntent().getStringExtra(SOURCE_CLASS))) {
            if (NIMChatActivity.class.getSimpleName().equals(getIntent().getStringExtra(SOURCE_CLASS))) {
                return "行程单";
            }
        }
        return "订单详情";
    }

    public OrderBean getOrderBean() {
        return orderBean;
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
                    skuType = "单次";
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
                    if (null != orderBean.orderPriceInfo) {
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
                    if (null != orderBean.orderPriceInfo) {
                        properties.put("hbc_room_average", orderBean.orderPriceInfo.priceHotel);
                        properties.put("hbc_room_num", orderBean.hotelRoom);
                        properties.put("hbc_room_totalprice", orderBean.hotelRoom * orderBean.orderPriceInfo.priceHotel);
                    }
                    break;
            }
            properties.put("hbc_sku_type", skuType);
            properties.put("hbc_price_total", orderBean.orderPriceInfo.shouldPay);//费用总计
            properties.put("hbc_price_coupon", String.valueOf(orderBean.orderPriceInfo.couponPrice));//使用优惠券
            properties.put("hbc_price_tra_fund", orderBean.orderPriceInfo.travelFundPrice);//使用旅游基金
//            int priceActual = (carBean.vehiclePrice + carBean.servicePrice) - CommonUtils.getCountInteger(orderBean.coupPriceInfo) - CommonUtils.getCountInteger(orderBean.travelFund);
//            if (priceActual < 0) {
//                priceActual = 0;
//            }
            properties.put("hbc_price_actually", orderBean.orderPriceInfo.actualPay);//实际支付金额
            properties.put("hbc_is_appoint_guide", this.getIntent().getStringExtra("guideCollectId") == null ? false : true);//指定司导下单
            SensorsDataAPI.sharedInstance(this).track("buy_submitorder", properties);
        } catch (Exception e) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EVALUATE_TYPE) {
            if (resultCode == Constants.EVALUATE_OK) {
                finish();
            }
        }
    }

    public PayResultExtarParamsBean getPayResultExtarParamsBean() {
        if (orderBean == null) {
            return null;
        }
        PayResultExtarParamsBean paramsBean = null;
        if (orderBean.orderType == 4) {
            paramsBean = new PayResultExtarParamsBean();
            paramsBean.startPoiBean = orderBean.getDestPoiBean();
            paramsBean.destPoiBean = orderBean.getStartPoiBean();
            paramsBean.cityId = orderBean.serviceCityId;
        } else if (orderBean.orderType == 1) {
            paramsBean = new PayResultExtarParamsBean();
            paramsBean.startPoiBean = orderBean.getDestPoiBean();
            AirPort airPort = new AirPort();
            airPort.airportName = orderBean.flightDestName;
            airPort.airportCode = orderBean.flightDestCode;
            airPort.cityName = orderBean.flightDestCityName;
            airPort.cityId = orderBean.serviceCityId;
            airPort.areaCode = orderBean.serviceAreaCode;
            airPort.location = orderBean.startAddressPoi;
            paramsBean.airPortBean = airPort;
        }
        if (paramsBean != null && !TextUtils.isEmpty(orderBean.guideCollectId) && orderBean.orderGuideInfo != null) {
            OrderGuideInfo guideInfo = orderBean.orderGuideInfo;
            GuidesDetailData guidesDetailData = new GuidesDetailData();
            guidesDetailData.guideId = guideInfo.guideID;
            guidesDetailData.guideName = guideInfo.guideName;
            guidesDetailData.avatar = guideInfo.guideAvatar;
            guidesDetailData.cityId = guideInfo.cityId;
            guidesDetailData.cityName = guideInfo.cityName;
            guidesDetailData.countryId = guideInfo.countryId;
            guidesDetailData.countryName = guideInfo.countryName;
            guidesDetailData.isFavored = guideInfo.storeStatus;
            guidesDetailData.isQuality = 0;
            paramsBean.guidesDetailData = guidesDetailData;
        }
        return paramsBean;
    }
}
