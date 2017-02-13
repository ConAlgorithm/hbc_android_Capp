package com.hugboga.custom.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.DefaultSSLSocketFactory;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.ChatInfo;
import com.hugboga.custom.data.bean.CouponBean;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.OrderGuideInfo;
import com.hugboga.custom.data.bean.OrderPriceInfo;
import com.hugboga.custom.data.bean.OrderStatus;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParserChatInfo;
import com.hugboga.custom.data.request.RequestCollectGuidesId;
import com.hugboga.custom.data.request.RequestOrderDetail;
import com.hugboga.custom.data.request.RequestPayNo;
import com.hugboga.custom.data.request.RequestUncollectGuidesId;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.bean.EventPayBean;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.statistic.event.EventBase;
import com.hugboga.custom.statistic.event.EventCancelOrder;
import com.hugboga.custom.statistic.event.EventPay;
import com.hugboga.custom.statistic.event.EventUtil;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.IMUtil;
import com.hugboga.custom.utils.PhoneInfo;
import com.hugboga.custom.widget.DialogUtil;
import com.hugboga.custom.widget.HbcViewBehavior;
import com.hugboga.custom.widget.OrderDetailBargainEntr;
import com.hugboga.custom.widget.OrderDetailDeliverView;
import com.hugboga.custom.widget.OrderDetailFloatView;
import com.hugboga.custom.widget.OrderDetailGuideInfo;
import com.hugboga.custom.widget.OrderDetailItineraryView;
import com.hugboga.custom.widget.OrderDetailTitleBar;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 16/8/2.
 */
public class OrderDetailActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.order_detail_title_layout)
    OrderDetailTitleBar titleBar;

    @Bind(R.id.order_detail_deliver_view)
    OrderDetailDeliverView deliverView;

    @Bind(R.id.order_detail_itinerary_view)
    OrderDetailItineraryView itineraryView;

    @Bind(R.id.order_detail_float_view)
    OrderDetailFloatView floatView;

    @Bind(R.id.order_detail_group_layout)
    LinearLayout groupLayout;

    @Bind(R.id.order_detail_empty_tv)
    TextView emptyTV;

    @Bind(R.id.order_detail_explain_tv)
    TextView explainTV;

    @Bind(R.id.order_detail_bargain_entr_view)
    OrderDetailBargainEntr bargainEntrView;

    private PopupWindow popup;
    private View menuLayout;
    private Params params;
    private OrderBean orderBean;
    private DialogUtil mDialogUtil;

    public static class Params implements Serializable {
        public String orderId;
        public String source;
        public int orderType;
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
        setContentView(R.layout.fg_order_detail);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        mDialogUtil = DialogUtil.getInstance(this);
        titleBar.setTitle(params.orderType);
        emptyTV.setVisibility(View.VISIBLE);
        requestData();

        Map map = new HashMap();
        map.put(Constants.PARAMS_SOURCE,source);
        MobClickUtils.onEvent(getEventId(),map);

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
            }

            if (orderBean.cancelRules != null && orderBean.cancelRules.size() > 0) {
                String explainStr = "";
                for (int i = 0; i < orderBean.cancelRules.size(); i++) {
                    explainStr += orderBean.cancelRules.get(i);
                }
                explainTV.setText(explainStr);
            }
        } else if (_request instanceof RequestUncollectGuidesId) {//取消收藏
            orderBean.orderGuideInfo.storeStatus = 0;
            updateCollectViewText();
        } else if (_request instanceof RequestCollectGuidesId) {//收藏
            orderBean.orderGuideInfo.storeStatus = 1;
            updateCollectViewText();
        } else if (_request instanceof RequestPayNo) {
            RequestPayNo mParser = (RequestPayNo) _request;
            if (mParser.payType == Constants.PAY_STATE_ALIPAY) {
                if ("travelFundPay".equals(mParser.getData()) || "couppay".equals(mParser.getData())) {//支付宝使用旅游基金和优惠券0元支付
                    PayResultActivity.Params params = new PayResultActivity.Params();
                    params.payResult = true;
                    params.orderId = orderBean.orderNo;
                    Intent intent = new Intent(OrderDetailActivity.this, PayResultActivity.class);
                    intent.putExtra(Constants.PARAMS_DATA, params);
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
            emptyTV.setVisibility(View.VISIBLE);
            emptyTV.setText(R.string.data_load_error_retry);
            emptyTV.setOnClickListener(this);
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
                DialogUtil.showServiceDialog(this, null, UnicornServiceActivity.SourceType.TYPE_ORDER, orderBean, null, getEventSource());
                break;
            case ORDER_DETAIL_PAY://立即支付
                if (!eventVerification(action)) {
                    break;
                }
                if (orderBean == null) {
                    return;
                }
                String couponId = null;
                CouponBean orderCoupon = orderBean.orderCoupon;
                if (orderCoupon != null) {
                    couponId = orderCoupon.couponID;
                }

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
                    intent = new Intent(OrderDetailActivity.this, ChoosePaymentActivity.class);
                    intent.putExtra(Constants.PARAMS_DATA, requestParams);
                    intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                    startActivity(intent);
                    setSensorsEvent();
                }
                break;
            case ORDER_DETAIL_INSURANCE_H5://皇包车免费赠送保险
                if (!eventVerification(action)) {
                    break;
                }
                intent = new Intent(OrderDetailActivity.this, WebInfoActivity.class);
                intent.putExtra(WebInfoActivity.WEB_URL, UrlLibs.H5_INSURANCE);
                startActivity(intent);
                break;
            case ORDER_DETAIL_ADD_INSURER://添加投保人 copy FgOrder
                if (!eventVerification(action)) {
                    break;
                }
                if (orderBean == null) {
                    return;
                }
                Bundle insureBundle = new Bundle();
                insureBundle.putSerializable("orderBean", orderBean);
                Intent intent1 = new Intent(activity,InsureActivity.class);
                intent1.putExtras(insureBundle);
                startActivity(intent1);
                break;
            case ORDER_DETAIL_GUIDE_CALL://联系司导
                if (!eventVerification(action)) {
                    break;
                }
                if (orderBean == null || orderBean.orderGuideInfo == null) {
                    return;
                }
                PhoneInfo.CallDial(OrderDetailActivity.this, orderBean.orderGuideInfo.guideTel);
                break;
            case ORDER_DETAIL_GUIDE_CHAT://和司导聊天
                if (!eventVerification(action)) {
                    break;
                }
                final OrderGuideInfo guideInfo = orderBean.orderGuideInfo;
                if (guideInfo == null) {
                    return;
                }
                ChatInfo chatInfo = new ChatInfo();
                chatInfo.isChat = true;
                chatInfo.userId = guideInfo.guideID;
                chatInfo.userAvatar = guideInfo.guideAvatar;
                chatInfo.title = guideInfo.guideName;
                chatInfo.targetType = "1";
                chatInfo.inBlack = guideInfo.inBlack;
                chatInfo.imUserId = guideInfo.guideImId;
                chatInfo.flag = guideInfo.flag;
                chatInfo.timediff = guideInfo.timediff;
                chatInfo.timezone = guideInfo.timezone;
                chatInfo.cityName = guideInfo.cityName;
                chatInfo.countryName = guideInfo.countryName;

                if(!IMUtil.getInstance().isLogined()){
                    return;
                }
                if(TextUtils.isEmpty(chatInfo.imUserId)){
                    return;
                }
                NIMChatActivity.start(OrderDetailActivity.this,chatInfo.imUserId,null/*,new ParserChatInfo().toJsonString(chatInfo)*/);
                break;
            case ORDER_DETAIL_GUIDE_INFO://司导详情
                if (!eventVerification(action)) {
                    break;
                }
                if (orderBean == null || orderBean.orderGuideInfo == null) {
                    return;
                }
                GuideDetailActivity.Params params = new GuideDetailActivity.Params();
                params.guideId = orderBean.orderGuideInfo.guideID;
                params.guideCarId = orderBean.orderGuideInfo.guideCarId;
                params.guideAgencyDriverId = orderBean.guideAgencyDriverId;
                params.isSelectedService = orderBean.guideAgencyType == 3;
                intent = new Intent(this, GuideDetailActivity.class);
                intent.putExtra(Constants.PARAMS_DATA, params);
                intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                startActivity(intent);
                break;
            case ORDER_DETAIL_UPDATE_COLLECT://更新收藏UI
                orderBean.orderGuideInfo.storeStatus = (int) action.getData();
                updateCollectViewText();
                break;
            case ORDER_DETAIL_GUIDE_COLLECT://收藏 只可收藏不可取消
                if (!eventVerification(action)) {
                    break;
                }
                if (orderBean == null || orderBean.orderGuideInfo == null || orderBean.orderGuideInfo.isCollected()) {
                    return;
                }
                EventUtil.onDefaultEvent(StatisticConstant.COLLECTG, getEventSource());
                mDialogUtil.showLoadingDialog();
                requestData(new RequestCollectGuidesId(OrderDetailActivity.this, orderBean.orderGuideInfo.guideID));
                break;
            case ORDER_DETAIL_GUIDE_EVALUATION://评价司导
                if (!eventVerification(action)) {
                    break;
                }
                if (orderBean == null) {
                    return;
                }
                intent = new Intent(this, EvaluateActivity.class);
                intent.putExtra(Constants.PARAMS_DATA, orderBean);
                startActivity(intent);
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
                intent.putExtra(Constants.PARAMS_SOURCE,source);
                intent.putExtra("goodtype",orderBean.orderGoodsType+"");
                intent.putExtra("type",orderBean.orderType==5?"1":"2");
                startActivity(intent);
                if(orderBean.orderGoodsType == 3) {//固定线路
                    StatisticClickEvent.click(StatisticConstant.CLICK_RG, "订单详情页");
                }else {
                    StatisticClickEvent.click(StatisticConstant.CLICK_RT, "订单详情页");
                }
                break;

            case ORDER_DETAIL_UPDATE_EVALUATION://更新评价UI
                if (!eventVerification(action)) {
                    break;
                }
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
            default:
                break;
        }
    }

    //TODO 收藏应该挪到OrderDetailGuideInfo内处理
    private void updateCollectViewText() {
        OrderDetailGuideInfo guideInfoView = deliverView.getGuideInfoView();
        if (guideInfoView != null) {
            TextView collectTV = (TextView) guideInfoView.findViewById(R.id.ogi_collect_tv);
            collectTV.setText(getString(orderBean.orderGuideInfo.isCollected() ? R.string.uncollect : R.string.collect));
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
            menuLayout  = LayoutInflater.from(this).inflate(R.layout.popup_top_right_menu, null);
        }
        TextView cancelOrderTV = (TextView)menuLayout.findViewById(R.id.cancel_order);
        TextView commonProblemTV = (TextView)menuLayout.findViewById(R.id.menu_phone);
        commonProblemTV.setText("常见问题");
        if (orderBean.orderStatus.code <= 5) {
            cancelOrderTV.setVisibility(View.VISIBLE);
        } else {
            cancelOrderTV.setVisibility(View.GONE);
        }

        if (popup != null) {
            popup.showAsDropDown(titleBar.findViewById(R.id.header_detail_right_1_btn));
            return;
        }
        popup = new PopupWindow(menuLayout, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popup.setBackgroundDrawable(new BitmapDrawable());
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        popup.showAsDropDown(titleBar.findViewById(R.id.header_detail_right_1_btn));

        cancelOrderTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogUtil = DialogUtil.getInstance(OrderDetailActivity.this);
                //如果此订单不能取消，直接进行提示
                popup.dismiss();

                if (orderBean.cancelable) {//cancelable是否能取消
                    String tip = "";
                    if (orderBean.orderStatus == OrderStatus.INITSTATE) {
                        tip = getString(R.string.order_cancel_tip);
                    } else if (orderBean.isChangeManual) {//需要人工取消订单
                        DialogUtil.showDefaultServiceDialog(OrderDetailActivity.this, "如需要取消订单，请联系客服处理", getEventSource());
                        return;
                    } else {
                        tip = orderBean.cancelTip;
                    }
                    mDialogUtil.showCustomDialog(getString(R.string.app_name), tip, "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EventBase eventBase = new EventCancelOrder(orderBean);
                            MobClickUtils.onEvent(eventBase);
                            Intent intent = new Intent(OrderDetailActivity.this, OrderCancelReasonActivity.class);
                            intent.putExtra(Constants.PARAMS_DATA, orderBean);
                            OrderDetailActivity.this.startActivity(intent);
                        }
                    }, "返回", null);
                } else {
                    mDialogUtil.showCustomDialog(orderBean.cancelText);
                }
            }
        });

        commonProblemTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        return "订单详情";
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
                    skuType = "定制包车游";
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
                    properties.put("hbc_car_type", orderBean.carType);
                    properties.put("hbc_start_time", orderBean.serviceTime);
                    properties.put("hbc_sku_id", orderBean.goodsNo);
                    properties.put("hbc_sku_name", orderBean.lineSubject);
                    properties.put("hbc_room_average", orderBean.orderPriceInfo.priceHotel);
                    properties.put("hbc_room_num", orderBean.hotelRoom);
                    properties.put("hbc_room_totalprice", orderBean.hotelRoom * orderBean.orderPriceInfo.priceHotel);
                    break;
                case 6:
                    skuType = "推荐线路";
                    properties.put("hbc_adultNum", orderBean.adult);
                    properties.put("hbc_childNum", orderBean.child);
                    properties.put("hbc_childseatNum", orderBean.childSeatNum);
                    properties.put("hbc_car_type", orderBean.carType);
                    properties.put("hbc_start_time", orderBean.serviceTime);
                    properties.put("hbc_sku_id", orderBean.goodsNo);
                    properties.put("hbc_sku_name", orderBean.lineSubject);
                    properties.put("hbc_room_average", orderBean.orderPriceInfo.priceHotel);
                    properties.put("hbc_room_num", orderBean.hotelRoom);
                    properties.put("hbc_room_totalprice", orderBean.hotelRoom * orderBean.orderPriceInfo.priceHotel);
                    break;
            }
            properties.put("hbc_sku_type", skuType);
            properties.put("hbc_price_total", orderBean.orderPrice);//费用总计
            properties.put("hbc_price_coupon", orderBean.coupPriceInfo);//使用优惠券
            properties.put("hbc_price_tra_fund", CommonUtils.getCountInteger(orderBean.travelFund));//使用旅游基金
//            int priceActual = (carBean.vehiclePrice + carBean.servicePrice) - CommonUtils.getCountInteger(orderBean.coupPriceInfo) - CommonUtils.getCountInteger(orderBean.travelFund);
//            if (priceActual < 0) {
//                priceActual = 0;
//            }
            properties.put("hbc_price_actually", orderBean.orderPriceInfo.actualPay);//实际支付金额
            properties.put("hbc_is_appoint_guide", orderBean.guideCollectId == null ? false : true);//指定司导下单
            SensorsDataAPI.sharedInstance(this).track("buy_submitorder", properties);
        } catch (Exception e) {
        }
    }
}
