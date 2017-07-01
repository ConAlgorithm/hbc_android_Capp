package com.hugboga.custom.widget;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.BaseActivity;
import com.hugboga.custom.activity.UnicornServiceActivity;
import com.hugboga.custom.utils.OrderUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/2/22.
 */
public class CharterFirstCountView extends LinearLayout implements SliderView.OnValueChangedListener{

    public static final int SLIDER_TYPE_ADULT = 1;
    public static final int SLIDER_TYPE_CHILD = 2;

    @Bind(R.id.charter_first_count_adult_slider_layout)
    SliderLayout adultSlider;
    @Bind(R.id.charter_first_count_child_slider_layout)
    SliderLayout childSlider;
    @Bind(R.id.charter_first_count_hint_tv)
    TextView hintTV;

    private Context context;

    private OnOutRangeListener listener;

    /**
     * 最大乘车人数
     * */
    private int maxPassengers = 10;

    public CharterFirstCountView(Context context) {
        this(context, null);
    }

    public CharterFirstCountView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        View view = inflate(context, R.layout.view_charter_first_count, this);
        ButterKnife.bind(view);

        adultSlider.setMax(11);
        adultSlider.setValue(0);
        adultSlider.setType(SLIDER_TYPE_ADULT);
        adultSlider.setOnValueChangedListener(this);
        adultSlider.setShowNumberIndicator(false);

        childSlider.setMin(0);
        childSlider.setMax(11);
        childSlider.setType(SLIDER_TYPE_CHILD);
        childSlider.setOnValueChangedListener(this);
        childSlider.setShowNumberIndicator(false);

        setSliderEnabled(false);
    }

    public void setSliderEnabled(boolean isEnabled) {
        adultSlider.setSliderEnabled(isEnabled);
        childSlider.setSliderEnabled(isEnabled);
    }

    public void setMaxPassengers(int maxPassengers, boolean isGuide, boolean isSeckills) {
        if (maxPassengers <= 0) {
            setSliderEnabled(false);
            adultSlider.setValue(0);
            childSlider.setValue(0);
            return;
        }
        if (adultSlider.getValue() == 0) {
            adultSlider.setMin(1);
            adultSlider.setValue(2);
        }
        adultSlider.setMax(maxPassengers);
        childSlider.setMax(maxPassengers);
        this.maxPassengers = maxPassengers;
        setHintViewVisibility();
        if (context instanceof BaseActivity) {
            if (isSeckills) {
                hintTV.setText(context.getResources().getString(R.string.charter_first_max_passengers_hint3, "" + maxPassengers));
            } else {
                int hintResId = isGuide ? R.string.charter_first_max_passengers_hint2 : R.string.charter_first_max_passengers_hint;
                OrderUtils.genCLickSpan((Activity) context, hintTV, context.getResources().getString(hintResId, "" + maxPassengers),
                        context.getResources().getString(R.string.charter_first_max_passengers_service),
                        null,
                        0xFFFFFFFF,
                        new OrderUtils.MyCLickSpan.OnSpanClickListener() {
                            @Override
                            public void onSpanClick(View view) {
                                DialogUtil.showServiceDialog(context, null, UnicornServiceActivity.SourceType.TYPE_CHARTERED, null, null, ((BaseActivity)context).getEventSource());
                            }
                        });
            }

        }
    }

    @Override
    public void onSliderScrolled(int value, int type) {
        setHintViewVisibility();
    }

    @Override
    public void onValueChanged(int value, int type) {
    }

    private void setHintViewVisibility() {
        if (maxPassengers <= 0) {
            hintTV.setVisibility(View.INVISIBLE);
            return;
        }
        final boolean isOutRange = childSlider.getValue() + adultSlider.getValue() > maxPassengers;
        if (isOutRange) {
            hintTV.setVisibility(View.VISIBLE);
        } else {
            hintTV.setVisibility(View.INVISIBLE);
        }

        if (listener != null) {
            listener.onOutRangeChange(isOutRange);
        }
    }

    public void setHintViewVisibility(int visibility) {
        hintTV.setVisibility(visibility);
    }

    public int getAdultValue() {
        return adultSlider.getValue();
    }

    public int getChildValue() {
        return childSlider.getValue();
    }

    public void setAdultValue(int value) {
        adultSlider.setValue(value);
    }

    public void setChildValue(int value) {
        childSlider.setValue(value);
    }

    public int getPassengers() {
        return getAdultValue() + getChildValue();
    }

    public interface OnOutRangeListener {
        public void onOutRangeChange(boolean isOut);
    }

    public void setOnOutRangeListener(OnOutRangeListener listener) {
        this.listener = listener;
    }

}
