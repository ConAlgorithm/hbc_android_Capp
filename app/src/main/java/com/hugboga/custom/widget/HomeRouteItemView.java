package com.hugboga.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.hugboga.custom.R;

/**
 * Created by qingcha on 16/6/19.
 */
public class HomeRouteItemView extends LinearLayout {

    public HomeRouteItemView(Context context) {
        this(context, null);
    }

    public HomeRouteItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(0xFFFFFFFF);
        setOrientation(LinearLayout.VERTICAL);
        inflate(getContext(), R.layout.view_home_route_item, this);
    }
}
