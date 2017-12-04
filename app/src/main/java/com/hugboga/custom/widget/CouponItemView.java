package com.hugboga.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 16/8/22.
 */

public class CouponItemView extends LinearLayout {
    View view;
    @BindView(R.id.title_view1)
    TextView titleView1;
    @BindView(R.id.title_view2)
    TextView titleView2;
    @BindView(R.id.content_view)
    TextView contentView;


    public CouponItemView(Context context) {
        this(context, null);
    }

    public CouponItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context,R.layout.coupon_item_layout, this);
        ButterKnife.bind(this, view);
    }

    public void setTitleType1(String txt) {
        titleView1.setVisibility(VISIBLE);
        titleView1.setText(txt);
    }
    public void setTitleType2(String txt) {
        titleView2.setVisibility(VISIBLE);
        titleView2.setText(txt);
    }
    public void setContent(String txt) {
        contentView.setText(txt);
    }

}
