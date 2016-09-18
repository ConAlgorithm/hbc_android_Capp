package com.hugboga.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created on 16/8/22.
 */

public class CouponItemView extends LinearLayout {
    View view;
    @Bind(R.id.title_view)
    TextView titleView;
    @Bind(R.id.content_view)
    TextView contentView;


    public CouponItemView(Context context) {
        this(context, null);
    }

    public CouponItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context,R.layout.coupon_item_layout, this);
        ButterKnife.bind(this, view);
    }

    public void setTitle(String txt) {
        titleView.setText(txt);
    }

    public void setContent(String txt) {
        contentView.setText(txt);
    }

}
