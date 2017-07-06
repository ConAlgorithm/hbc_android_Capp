package com.hugboga.custom.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/2/22.
 */
public class SliderLayout extends LinearLayout {

    @Bind(R.id.slider_layout_min_tv)
    TextView minTV;
    @Bind(R.id.slider_layout_max_tv)
    TextView maxTV;
    @Bind(R.id.slider_layout_slider)
    SliderView slider;

    public SliderLayout(Context context) {
        this(context, null);
    }

    public SliderLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.slider_layout, this);
        ButterKnife.bind(view);
    }

    public void setValue(int value) {
        slider.setValue(value);
    }

    public int getValue() {
        return slider.getValue();
    }

    public int getMax() {
        return slider.getMax();
    }

    public void setMax(int max) {
        slider.setMax(max);
        maxTV.setText("" + max);
    }

    public int getMin() {
        return slider.getMin();
    }

    public void setMin(int min) {
        slider.setMin(min);
        minTV.setText("" + min);
    }

    public void setSliderEnabled(boolean isEnabled) {
        minTV.setVisibility(isEnabled ? VISIBLE : INVISIBLE);
        maxTV.setVisibility(isEnabled ? VISIBLE : INVISIBLE);
        slider.setEnabled(isEnabled);
    }

    public void setType(int type) {
        slider.setType(type);
    }

    public void setOnValueChangedListener(SliderView.OnValueChangedListener onValueChangedListener) {
        slider.setOnValueChangedListener(onValueChangedListener);
    }

    public void setShowNumberIndicator(boolean showNumberIndicator) {
        slider.setShowNumberIndicator(showNumberIndicator);
    }
}
