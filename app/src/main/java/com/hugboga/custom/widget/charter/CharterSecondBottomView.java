package com.hugboga.custom.widget.charter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.utils.CharterDataUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 17/2/25.
 */

public class CharterSecondBottomView extends LinearLayout{

    @Bind(R.id.sku_order_bottom_confirm_tv)
    TextView confirmTV;
    @Bind(R.id.sku_order_bottom_day_tv)
    TextView dayTV;
    @Bind(R.id.sku_order_bottom_day_layout)
    LinearLayout dayLayout;

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
            confirmTV.setText("查看报价");
            dayLayout.setVisibility(View.GONE);
        } else {
            confirmTV.setText("确定  ");
            dayLayout.setVisibility(View.VISIBLE);
            dayTV.setText(String.format("Day%1$s", "" + charterDataUtils.currentDay));
        }
    }

    @OnClick({R.id.charter_bottom_travel_list_layout})
    public void intentTravelList(View view) {
        if (listener != null) {
            listener.intentTravelList();
        }
    }

    @OnClick({R.id.sku_order_bottom_confirm_tv})
    public void confirm(View view) {
        if (listener != null) {
            listener.confirm();
        }
    }

    public interface OnBottomClickListener{
        public void confirm();
        public void intentTravelList();
    }

    public void setOnBottomClickListener(OnBottomClickListener listener) {
        this.listener = listener;
    }
}
