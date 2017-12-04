package com.hugboga.custom.widget.charter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.utils.CharterDataUtils;
import com.hugboga.custom.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 17/2/25.
 */

public class CharterSecondBottomView extends LinearLayout{

    @BindView(R.id.sku_order_bottom_confirm_tv)
    TextView confirmTV;
    @BindView(R.id.sku_order_bottom_confirm_arrow_iv)
    ImageView confirmArrowIV;
    @BindView(R.id.sku_order_bottom_confirm_layout)
    LinearLayout confirmLayout;
    @BindView(R.id.charter_bottom_travel_list_layout)
    LinearLayout travelListLayout;
    @BindView(R.id.charter_bottom_previous_layout)
    LinearLayout previousLayout;



    private CharterDataUtils charterDataUtils;
    private OnBottomClickListener listener;

    public CharterSecondBottomView(Context context) {
        this(context, null);
    }

    public CharterSecondBottomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_charter_second_bottom, this);
        ButterKnife.bind(view);
        charterDataUtils = CharterDataUtils.getInstance();
        updateConfirmView();
    }

    public void updateConfirmView() {
        if (charterDataUtils.isLastDay()) {
            confirmTV.setText(CommonUtils.getString(R.string.daily_second_see_price));
            confirmArrowIV.setVisibility(View.GONE);
        } else {
            confirmTV.setText("  " + CommonUtils.getString(R.string.daily_second_retral_day));
            confirmArrowIV.setVisibility(View.VISIBLE);
        }
        previousLayout.setVisibility(charterDataUtils.isFirstDay() ? View.GONE : View.VISIBLE);
    }

    public void queryPriceState() {
        confirmTV.setText(CommonUtils.getString(R.string.daily_second_see_price));
        confirmArrowIV.setVisibility(View.GONE);
        travelListLayout.setVisibility(View.GONE);
        previousLayout.setVisibility(View.GONE);
    }

    @OnClick({R.id.charter_bottom_travel_list_layout})
    public void intentTravelList(View view) {
        if (listener != null) {
            listener.intentTravelList();
        }
    }

    @OnClick({R.id.charter_bottom_previous_layout})
    public void previous(View view) {
        if (listener != null) {
            listener.previous();
        }
    }

    @OnClick({R.id.sku_order_bottom_confirm_layout})
    public void confirm(View view) {
        if (listener != null) {
            listener.confirm();
        }
    }

    public interface OnBottomClickListener {
        public void confirm();
        public void previous();
        public void intentTravelList();
    }

    public void setOnBottomClickListener(OnBottomClickListener listener) {
        this.listener = listener;
    }

    public void setConfirmViewEnabled(boolean isEnabled) {
        confirmLayout.setEnabled(isEnabled);
        confirmLayout.setBackgroundResource(isEnabled ? R.drawable.shape_rounded_yellow_btn : R.drawable.shape_rounded_gray_btn);
    }
}
