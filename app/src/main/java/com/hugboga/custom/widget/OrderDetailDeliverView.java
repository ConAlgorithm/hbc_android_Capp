package com.hugboga.custom.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.huangbaoche.hbcframe.data.net.ErrorHandler;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.DeliverInfoBean;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.OrderStatus;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestDeliverInfo;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.utils.ApiReportHelper;
import com.hugboga.custom.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.iwgang.countdownview.CountdownView;

/**
 * Created by qingcha on 16/9/7.
 */
    public class OrderDetailDeliverView extends LinearLayout implements HbcViewBehavior, HttpRequestListener {

    @BindView(R.id.order_detail_deliver_loading_view)
    OrderDetailDeliverLoadingLayout loadingView;
    @BindView(R.id.order_detail_deliver_grouplayout)
    FrameLayout groupLayout;
    private OrderDetailGuideInfo guideInfoView;

    private OrderBean orderBean;
    private int deliverStatus;
    private ErrorHandler errorHandler;
    private int refreshCount = DeliverInfoBean.MAX_REFRESH_COUNT;

    public OrderDetailDeliverView(Context context) {
        this(context, null);
    }

    public OrderDetailDeliverView(Context context, AttributeSet attrs) {
        super(context, attrs);
        final View view = inflate(context, R.layout.view_order_detail_deliver, this);
        ButterKnife.bind(view);
        setOrientation(LinearLayout.VERTICAL);
    }

    public OrderDetailGuideInfo getGuideInfoView() {
        return guideInfoView;
    }

    @Override
    public void update(Object _data) {
        if (_data == null) {
            return;
        }
        orderBean = (OrderBean) _data;
        if (orderBean.isSeparateOrder()) {// 是否拆单
            setVisibility(View.GONE);
            return;
        }
        if (orderBean.orderSource == 12 && orderBean.orderGuideInfo != null) {// 分销订单
            if (orderBean.orderStatus == OrderStatus.PAYSUCCESS) {
                addDetailGuideInfoView(false);
                sendRequest(true);
            } else {
                addDetailGuideInfoView(true);
            }
        } else {// 其它订单
            if (orderBean.orderStatus == OrderStatus.PAYSUCCESS || orderBean.isTwiceConfirm) { // 预订成功 || 二次确认
                sendRequest(true);
            } else if (orderBean.orderType != 888 && orderBean.orderStatus != OrderStatus.INITSTATE && orderBean.orderGuideInfo != null) {
                addDetailGuideInfoView(true);
            } else {
                stop();
                setVisibility(View.GONE);
            }
        }
    }

    private void addDetailGuideInfoView(boolean isRemoveAllViews) {
        if (guideInfoView == null) {
            guideInfoView = new OrderDetailGuideInfo(getContext());
            if (isRemoveAllViews) {
                removeAllViews();
                addView(guideInfoView);
            } else {
                View spaceView = new View(getContext());
                spaceView.setBackgroundColor(getContext().getResources().getColor(R.color.default_bg));
                addView(spaceView, LayoutParams.MATCH_PARENT, UIUtils.dip2px(10));
                addView(guideInfoView);
            }
        }
        guideInfoView.update(orderBean);
    }

    public void refreshData(boolean isShowLoadingView) {
        if (orderBean == null || orderBean.isSeparateOrder()) {
            return;
        }
        if (orderBean != null && orderBean.orderStatus == OrderStatus.PAYSUCCESS) {
            sendRequest(isShowLoadingView);
        }
    }

    private void sendRequest(boolean isShowLoadingView) {
        if (getContext() instanceof Activity) {
            if (((Activity) getContext()).isFinishing()) {
                return;
            }
        }
        if (orderBean == null) {
            return;
        }
        if (isShowLoadingView) {
            loadingView.setVisibility(View.VISIBLE);
            groupLayout.setVisibility(View.GONE);
        }
        RequestDeliverInfo request = new RequestDeliverInfo(getContext(), orderBean.orderNo);
        HttpRequestUtils.request(getContext(), request, this, isShowLoadingView);
    }

    private void resetItemView(DeliverInfoBean _deliverInfoBean) {
        --refreshCount;

        loadingView.setVisibility(View.GONE);
        groupLayout.setVisibility(View.VISIBLE);

        if (deliverStatus != _deliverInfoBean.deliverStatus) {
            deliverStatus = _deliverInfoBean.deliverStatus;
            groupLayout.removeAllViews();
        } else if (groupLayout.getChildCount() > 0) {
            HbcViewBehavior hbcViewBehavior = (HbcViewBehavior) groupLayout.getChildAt(0);
            hbcViewBehavior.update(_deliverInfoBean);
            return;
        }

        if (orderBean.isTwiceConfirm) {// 二次确认订单
            setDeliverItemView(_deliverInfoBean);
            return;
        }

        if (_deliverInfoBean.deliverStatus == DeliverInfoBean.DeliverStatus.UNBILLED) { // 1.未发单
            OrderDetailDeliverUnbilledView unbilledView = new OrderDetailDeliverUnbilledView(getContext());
            unbilledView.update(_deliverInfoBean);
            groupLayout.addView(unbilledView);
            unbilledView.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                @Override
                public void onEnd(CountdownView cv) {
                    sendRequest(true);
                }
            });
        } else if (_deliverInfoBean.deliverStatus == DeliverInfoBean.DeliverStatus.IDENTIFIED) { // 8.已确定导游
            EventBus.getDefault().post(new EventAction(EventType.ORDER_DETAIL_UPDATE, orderBean.orderNo));
        } else {
            setDeliverItemView(_deliverInfoBean);
        }
    }

    private void setDeliverItemView(DeliverInfoBean _deliverInfoBean) {
        OrderDetailDeliverItemView itemView = new OrderDetailDeliverItemView(getContext());
        itemView.setOrderNo(orderBean.orderNo, orderBean.orderType);
        setEvent(orderBean.orderType);
        _deliverInfoBean.refreshCount = refreshCount;
        itemView.update(_deliverInfoBean);
        groupLayout.addView(itemView);
        itemView.setOnCountdownEndListener(new OrderDetailDeliverCountDownView.OnUpdateListener() {
            @Override
            public void onUpdate(boolean isEnd) {
                sendRequest(isEnd);
            }
        });
    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        ApiReportHelper.getInstance().addReport(_request);
        if (_request instanceof RequestDeliverInfo) {
            RequestDeliverInfo request = (RequestDeliverInfo) _request;
            DeliverInfoBean deliverInfoBean = request.getData();
            if (deliverInfoBean == null) {
                setVisibility(View.GONE);
                return;
            }
            if (deliverInfoBean.isOrderStatusChanged()) {//订单状态改变
                EventBus.getDefault().post(new EventAction(EventType.ORDER_DETAIL_UPDATE, orderBean.orderNo));
            } else {
                resetItemView(deliverInfoBean);
            }
        }
    }

    @Override
    public void onDataRequestCancel(BaseRequest request) {

    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        if (errorHandler == null) {
            errorHandler = new ErrorHandler((Activity)getContext(), this);
        }
        errorHandler.onDataRequestError(errorInfo, request);
    }

    public void setEvent(int orderType) {
        if (orderType == 0) {
            return;
        }
        Map<String,String> map = new HashMap<>();
        String orderTypeStr = "";
        switch(orderType) {
            case 3:
                orderTypeStr = "自定义包车游";
                break;
            case 5:
                orderTypeStr = "固定线路";
                break;
            case 6:
                orderTypeStr = "推荐线路";
                break;
        }
        map.put("ordertype", orderTypeStr);
        MobClickUtils.onEvent(StatisticConstant.CLICK_WAIT_G, map);
    }

    public void stop() {
        if (groupLayout == null || groupLayout.getChildCount() <= 0) {
            return;
        }
        if (groupLayout.getChildAt(0) instanceof OrderDetailDeliverUnbilledView) {
            ((OrderDetailDeliverUnbilledView) groupLayout.getChildAt(0)).stop();
        } else if (groupLayout.getChildAt(0) instanceof OrderDetailDeliverItemView) {
            ((OrderDetailDeliverItemView) groupLayout.getChildAt(0)).stop();
        }
        groupLayout.removeAllViews();
    }
}
