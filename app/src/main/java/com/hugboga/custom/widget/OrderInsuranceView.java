package com.hugboga.custom.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 17/7/5.
 */

public class OrderInsuranceView extends LinearLayout {

    @Bind(R.id.order_discount_insurance_choose_iv)
    ImageView insuranceChooseIV;
    @Bind(R.id.order_discount_insurance_count_tv)
    TextView insuranceCountTV;

    public OrderInsuranceView(Context context) {
        this(context, null);
    }

    public OrderInsuranceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        View view = inflate(context, R.layout.view_order_insurance, this);
        ButterKnife.bind(view);
        insuranceChooseIV.setSelected(true);
    }

    public void setInsuranceCount(int count) {
        insuranceCountTV.setText(String.format(" × %1$s份", "" + count));
    }

    @OnClick({R.id.order_discount_insurance_layout})
    public void onClick(View view) {
        insuranceChooseIV.setSelected(!insuranceChooseIV.isSelected());
    }
}
