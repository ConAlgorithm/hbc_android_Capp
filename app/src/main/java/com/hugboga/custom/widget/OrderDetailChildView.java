package com.hugboga.custom.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.OrderDetailActivity;
import com.hugboga.custom.data.bean.DeliverInfoBean;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.request.RequestDeliverInfo;
import com.hugboga.custom.utils.ApiReportHelper;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/3/14.
 */

public class OrderDetailChildView extends LinearLayout implements HbcViewBehavior, HttpRequestListener {

    @Bind(R.id.order_detail_child_guideinfo)
    OrderDetailGuideInfo orderDetailGuideInfo;
    @Bind(R.id.order_detail_child_travel_view)
    OrderDetailTravelView orderDetailTravelView;
    @Bind(R.id.order_detail_child_cancel)
    TextView childCancelTV;
    @Bind(R.id.order_detail_child_deliver_view)
    OrderDetailDeliverItemView orderDetailDeliverItemView;

    private OrderDetailActivity orderDetailActivity;
    private OrderBean orderBean;

    public OrderDetailChildView(Context context) {
        this(context, null);
    }

    public OrderDetailChildView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_order_detail_child, this);
        ButterKnife.bind(view);
        orderDetailActivity = (OrderDetailActivity) getContext();
    }

    @Override
    public void update(Object _data) {
        OrderBean parentOrderBean = orderDetailActivity.getOrderBean();

        orderBean = (OrderBean) _data;
        if (orderBean.orderStatus.code == 2 && parentOrderBean.isSeparateOrder()) {
            orderDetailGuideInfo.setVisibility(View.GONE);
            childCancelTV.setVisibility(View.GONE);
            orderDetailDeliverItemView.setVisibility(View.VISIBLE);
            DeliverInfoBean deliverInfoBean = ((OrderDetailActivity)getContext()).getDeliverInfoBean();
            if (deliverInfoBean != null) {
                orderDetailDeliverItemView.loadingLayout(deliverInfoBean);
            } else {
                RequestDeliverInfo request = new RequestDeliverInfo(getContext(), orderBean.orderNo);
                HttpRequestUtils.request(getContext(), request, this, false);
            }
        } else if (orderBean.orderStatus.code > 2) {
            orderDetailDeliverItemView.setVisibility(View.GONE);
            if (orderBean.orderGuideInfo == null) {
                childCancelTV.setVisibility(View.VISIBLE);
                orderDetailGuideInfo.setVisibility(View.GONE);
                switch (orderBean.orderStatus) {
                    case CANCELLED://已取消
                        childCancelTV.setText(R.string.order_detail_child_state_cancel);
                        break;
                    case REFUNDED://已退款
                        childCancelTV.setText(R.string.order_detail_child_state_refund);
                        break;
                    case COMPLAINT://客诉处理中
                        childCancelTV.setText(R.string.order_detail_child_state_complaint);
                        break;
                }
            } else {//显示司导信息
                childCancelTV.setVisibility(View.GONE);
                orderDetailGuideInfo.setVisibility(View.VISIBLE);
                orderDetailGuideInfo.update(orderBean);
            }
        } else {
            orderDetailDeliverItemView.setVisibility(View.GONE);
            orderDetailGuideInfo.setVisibility(View.GONE);
            childCancelTV.setVisibility(View.GONE);
        }
        orderDetailTravelView.update(orderBean);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        ApiReportHelper.getInstance().addReport(_request);
        if (_request instanceof RequestDeliverInfo) {
            RequestDeliverInfo request = (RequestDeliverInfo) _request;
            DeliverInfoBean deliverInfoBean = request.getData();
            if (deliverInfoBean == null) {
                orderDetailDeliverItemView.setVisibility(View.GONE);
                return;
            }
            orderDetailActivity.setDeliverInfoBean(deliverInfoBean);
            orderDetailDeliverItemView.loadingLayout(deliverInfoBean);
        }
    }

    @Override
    public void onDataRequestCancel(BaseRequest request) {

    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {

    }
}
