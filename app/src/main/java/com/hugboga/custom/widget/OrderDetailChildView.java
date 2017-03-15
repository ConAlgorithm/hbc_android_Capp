package com.hugboga.custom.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.OrderStatus;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/3/14.
 */

public class OrderDetailChildView extends LinearLayout implements HbcViewBehavior{

    @Bind(R.id.order_detail_child_guideinfo)
    OrderDetailGuideInfo orderDetailGuideInfo;
    @Bind(R.id.order_detail_child_travelView)
    OrderDetailTravelView orderDetailTravelView;
    @Bind(R.id.order_detail_child_cancel)
    TextView childCancelTV;


    public OrderDetailChildView(Context context) {
        this(context, null);
    }

    public OrderDetailChildView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_order_detail_child, this);
        ButterKnife.bind(view);
    }

    @Override
    public void update(Object _data) {
        OrderBean orderBean = (OrderBean) _data;
        if (orderBean.orderStatus.code >= 3) {
            switch (orderBean.orderStatus) {
                case AGREE:
                case ARRIVED:
                case SERVICING:
                case NOT_EVALUATED:
                case COMPLETE:
                    childCancelTV.setVisibility(View.GONE);
                    //显示司导
                    orderDetailGuideInfo.setVisibility(View.VISIBLE);
                    orderDetailGuideInfo.update(orderBean);
                    break;
                case CANCELLED://已取消
                    childCancelTV.setVisibility(View.VISIBLE);
                    orderDetailGuideInfo.setVisibility(View.GONE);
                    childCancelTV.setText("此段行程：已取消");
                    break;
                case REFUNDED://已退款
                    childCancelTV.setVisibility(View.VISIBLE);
                    orderDetailGuideInfo.setVisibility(View.GONE);
                    childCancelTV.setText("此段行程：已退款");
                    break;
                case COMPLAINT://客诉处理中
                    childCancelTV.setVisibility(View.VISIBLE);
                    orderDetailGuideInfo.setVisibility(View.GONE);
                    childCancelTV.setText("此段行程：客诉处理中");
                    break;
            }

        } else {
            orderDetailGuideInfo.setVisibility(View.GONE);
            childCancelTV.setVisibility(View.GONE);
        }
        orderDetailTravelView.update(orderBean);
    }
}
