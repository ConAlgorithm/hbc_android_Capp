package com.hugboga.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 16/9/8.
 */
public class OrderDetailDeliverLoadingLayout extends LinearLayout{

    @Bind(R.id.oeder_detail_deliver_loading_view)
    DeliverLoadingView loadingView;
    @Bind(R.id.oeder_detail_deliver_loading_title_tv)
    TextView titleTV;

    public OrderDetailDeliverLoadingLayout(Context context) {
        this(context, null);
    }

    public OrderDetailDeliverLoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setGravity(Gravity.CENTER_VERTICAL);
        setOrientation(LinearLayout.HORIZONTAL);
        final View view = inflate(context, R.layout.view_order_detail_deliver_loading, this);
        ButterKnife.bind(view);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == View.GONE) {
            onStop();
        } else {
            onStart();
        }
    }

    public void onStop() {
        loadingView.onStop();
    }

    public void onStart() {
        loadingView.onStart();
    }
}
