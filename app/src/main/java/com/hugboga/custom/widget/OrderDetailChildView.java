package com.hugboga.custom.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.hugboga.custom.R;

import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/3/14.
 */

public class OrderDetailChildView extends LinearLayout{

    public OrderDetailChildView(Context context) {
        this(context, null);
    }

    public OrderDetailChildView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_order_detail_child, this);
        ButterKnife.bind(view);
    }
}
