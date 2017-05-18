package com.hugboga.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 17/5/18.
 */

public class PickupOrderBottomView extends RelativeLayout {

    @Bind(R.id.pickup_order_bottom_should_price_tv)
    TextView shouldPriceTV;
    @Bind(R.id.pickup_order_bottom_distance_tv)
    TextView distanceTV;

    public PickupOrderBottomView(Context context) {
        this(context, null);
    }

    public PickupOrderBottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_pickup_order_bottom, this);
        ButterKnife.bind(view);
        setBackgroundColor(0xFFFFFFFF);
    }

    @OnClick(R.id.pickup_order_bottom_confirm_tv)
    public void onConfirm() {

    }
}
