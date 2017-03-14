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
 * Created by qingcha on 17/3/14.
 */

public class OrderDetailNoView extends RelativeLayout {

    @Bind(R.id.order_detail_travel_no_tv)
    TextView noTV;

    public OrderDetailNoView(Context context) {
        this(context, null);
    }

    public OrderDetailNoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_order_detail_no, this);
        ButterKnife.bind(view);
    }

    @OnClick({R.id.order_detail_travel_no_copy_tv})
    public void copyOrderNo() {

    }
}
