package com.hugboga.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.hugboga.custom.R;

import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/3/14.
 */

public class OrderDetailTravelView extends LinearLayout {

    public OrderDetailTravelView(Context context) {
        this(context, null);
    }

    public OrderDetailTravelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_order_detail_travel, this);
        ButterKnife.bind(view);
    }
}
