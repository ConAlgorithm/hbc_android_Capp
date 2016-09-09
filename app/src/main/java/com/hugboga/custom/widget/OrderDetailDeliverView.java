package com.hugboga.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

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

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.iwgang.countdownview.CountdownView;

/**
 * Created by qingcha on 16/9/7.
 */
public class OrderDetailDeliverView extends LinearLayout implements HbcViewBehavior, HttpRequestListener {

    @Bind(R.id.order_detail_deliver_loading_view)
    OrderDetailDeliverLoadingLayout loadingView;
    @Bind(R.id.order_detail_deliver_grouplayout)
    FrameLayout groupLayout;
    private OrderDetailGuideInfo guideInfoView;

    private OrderBean orderBean;
    private int deliverStatus;

    public OrderDetailDeliverView(Context context) {
        this(context, null);
    }

    public OrderDetailDeliverView(Context context, AttributeSet attrs) {
        super(context, attrs);
        final View view = inflate(context, R.layout.view_order_detail_deliver, this);
        ButterKnife.bind(view);
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
        if (orderBean.orderStatus == OrderStatus.PAYSUCCESS) {//2:预订成功
            sendRequest();
        } else if (orderBean.orderStatus != OrderStatus.INITSTATE && orderBean.orderGuideInfo != null) {
            removeAllViews();
            if (guideInfoView == null) {
                guideInfoView = new OrderDetailGuideInfo(getContext());
            }
            addView(guideInfoView);
            guideInfoView.update(orderBean);
        } else {
            setVisibility(View.GONE);
        }
    }

    private void sendRequest() {
        if (orderBean == null) {
            return;
        }
        loadingView.setVisibility(View.VISIBLE);
        RequestDeliverInfo request = new RequestDeliverInfo(getContext(), orderBean.orderNo);
        HttpRequestUtils.request(getContext(), request, this);
    }

    private void resetItemView(DeliverInfoBean _deliverInfoBean) {
        if (deliverStatus != _deliverInfoBean.deliverStatus) {
            deliverStatus = _deliverInfoBean.deliverStatus;
            groupLayout.removeAllViews();
        } else if (groupLayout.getChildCount() > 0) {
            HbcViewBehavior hbcViewBehavior = (HbcViewBehavior) groupLayout.getChildAt(0);
            hbcViewBehavior.update(_deliverInfoBean);
            return;
        }

        if (_deliverInfoBean.deliverStatus == DeliverInfoBean.DeliverStatus.UNBILLED) { // 未发单
            OrderDetailDeliverUnbilledView unbilledView = new OrderDetailDeliverUnbilledView(getContext());
            unbilledView.update(_deliverInfoBean);
            groupLayout.addView(unbilledView);
            unbilledView.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                @Override
                public void onEnd(CountdownView cv) {
                    sendRequest();
                }
            });
        } else if (_deliverInfoBean.deliverStatus == DeliverInfoBean.DeliverStatus.IDENTIFIED) { // 已确定导游
            EventBus.getDefault().post(new EventAction(EventType.ORDER_DETAIL_UPDATE, orderBean.orderNo));
        } else {
            OrderDetailDeliverItemView itemView = new OrderDetailDeliverItemView(getContext());
            itemView.update(_deliverInfoBean);
            groupLayout.addView(itemView);
            itemView.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                @Override
                public void onEnd(CountdownView cv) {
                    sendRequest();
                }
            });
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        if (_request instanceof RequestDeliverInfo) {
            loadingView.setVisibility(View.GONE);
            RequestDeliverInfo request = (RequestDeliverInfo) _request;
            DeliverInfoBean deliverInfoBean = request.getData();
            resetItemView(deliverInfoBean);
        }
    }

    @Override
    public void onDataRequestCancel(BaseRequest request) {

    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {

    }
}
