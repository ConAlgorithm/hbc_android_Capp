package com.hugboga.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.hugboga.custom.R;

/**
 * Created by qingcha on 16/9/8.
 */

public class OrderDetailDeliverSwitcherView extends ViewSwitcher implements ViewSwitcher.ViewFactory{

    public OrderDetailDeliverSwitcherView(Context context) {
        this(context, null);
    }

    public OrderDetailDeliverSwitcherView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFactory(this);
        setInAnimation(AnimationUtils.loadAnimation(context, R.anim.home_banner_fade_in));
        setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.home_banner_fade_out));

        ImageView img = (ImageView) getNextView();
    }

    @Override
    public View makeView() {
        ImageView imageView  = new ImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);
        return imageView;
    }
}
