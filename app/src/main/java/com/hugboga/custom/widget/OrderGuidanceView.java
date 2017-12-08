package com.hugboga.custom.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.hugboga.custom.R;

/**
 * Created by qingcha on 17/12/7.
 */

public class OrderGuidanceView extends LinearLayout{

    public OrderGuidanceView(Context context) {
        this(context, null);
    }

    public OrderGuidanceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_order_guidance,this);
    }
}
