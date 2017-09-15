package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.utils.CommonUtils;

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
        insuranceCountTV.setText(CommonUtils.getString(R.string.order_insurance_unit, "" + count));
    }

    @OnClick({R.id.order_discount_insurance_layout, R.id.order_discount_insurance_explain_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.order_discount_insurance_layout:
                insuranceChooseIV.setSelected(!insuranceChooseIV.isSelected());
                break;
            case R.id.order_discount_insurance_explain_tv:
                Intent intent = new Intent(getContext(), WebInfoActivity.class);
                intent.putExtra(WebInfoActivity.WEB_URL, UrlLibs.H5_INSURANCE);
                getContext().startActivity(intent);
                break;
        }

    }
}
