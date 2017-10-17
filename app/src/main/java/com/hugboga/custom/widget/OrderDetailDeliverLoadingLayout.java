package com.hugboga.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.hugboga.custom.R;

/**
 * Created by qingcha on 16/9/8.
 */
public class OrderDetailDeliverLoadingLayout extends LinearLayout{

    public OrderDetailDeliverLoadingLayout(Context context) {
        this(context, null);
    }

    public OrderDetailDeliverLoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setGravity(Gravity.CENTER_VERTICAL);
        setOrientation(LinearLayout.HORIZONTAL);
        inflate(context, R.layout.view_order_detail_deliver_loading, this);
    }
}
