package com.hugboga.custom.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.hugboga.custom.data.bean.OrderBean;
/**
 * Created by qingcha on 17/3/14.
 */

public class OrderDetailTravelInfoView extends FrameLayout implements HbcViewBehavior{

    private OrderDetailTravelGroup orderDetailTravelGroup;
    private OrderDetailChildView orderDetailChildView;

    public OrderDetailTravelInfoView(@NonNull Context context) {
        this(context, null);
    }

    public OrderDetailTravelInfoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void update(Object _data) {
        OrderBean orderBean = (OrderBean) _data;
        if (orderBean.orderType == 888) {
            setVisibility(View.VISIBLE);
            if (orderDetailTravelGroup == null) {
                removeAllViews();
                orderDetailTravelGroup = new OrderDetailTravelGroup(getContext());
                addView(orderDetailTravelGroup, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            }
            orderDetailTravelGroup.update(_data);
        } else if (orderBean.orderType == 3) {
            setVisibility(View.GONE);
            if (orderDetailChildView == null) {
                removeAllViews();
                orderDetailChildView = new OrderDetailChildView(getContext());
                addView(orderDetailChildView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            }
            orderDetailChildView.update(_data);
        } else {
            removeAllViews();
            setVisibility(View.GONE);
        }

    }
}
