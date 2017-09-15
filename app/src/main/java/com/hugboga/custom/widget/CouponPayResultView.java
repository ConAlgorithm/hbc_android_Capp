package com.hugboga.custom.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.MainActivity;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.CouponActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.utils.UIUtils;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 17/6/14.
 */
public class CouponPayResultView extends LinearLayout {

    @Bind(R.id.coupon_pay_result_successed_iv)
    ImageView successedIV;
    @Bind(R.id.coupon_pay_result_successed_tv)
    TextView successedTV;
    @Bind(R.id.coupon_pay_result_desc_tv)
    TextView descTV;
    @Bind(R.id.coupon_pay_result_top_layout)
    LinearLayout topLayout;
    @Bind(R.id.coupon_pay_result_bottom_tv)
    TextView bottomTV;

    private boolean isPaySucceed; //支付结果

    public CouponPayResultView(Context context) {
        this(context, null);
    }

    public CouponPayResultView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_coupon_pay_result, this);
        ButterKnife.bind(view);

        int marginLeft = UIUtils.dip2px(15);
        int width = UIUtils.getScreenWidth() - marginLeft * 2;
        int height = (int)(386 / 720f * UIUtils.getScreenWidth());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        params.leftMargin = marginLeft;
        params.rightMargin = marginLeft;
        params.topMargin = UIUtils.dip2px(20);
        topLayout.setLayoutParams(params);
    }

    @OnClick(R.id.coupon_pay_result_bottom_tv)
    public void onClick() {
        if (isPaySucceed) {
            getContext().startActivity(new Intent(getContext(), MainActivity.class));
            Intent intent = new Intent(getContext(), CouponActivity.class);
            intent.putExtra(Constants.PARAMS_SOURCE, "买卷支付结果页");
            getContext().startActivity(intent);
        } else {
            ((Activity) getContext()).finish();
        }
    }

    public void initView(boolean _isPaySucceed) {
        this.isPaySucceed = _isPaySucceed;
        if (isPaySucceed) {
            successedIV.setBackgroundResource(R.mipmap.activity_pay_successful);
            successedTV.setText(R.string.par_result_succeed);
            successedTV.setTextColor(getContext().getResources().getColor(R.color.default_btn_yellow));
            descTV.setText(R.string.par_result_succeed_coupon_hint);
            bottomTV.setText(R.string.par_result_coupon);
            bottomTV.setTextColor(0xFFFFFFFF);
            bottomTV.setBackgroundResource(R.drawable.shape_rounded_yellow_btn);
        } else {
            successedIV.setBackgroundResource(R.mipmap.activity_pay_failure);
            successedTV.setText(R.string.par_result_failure);
            successedTV.setTextColor(0xFFC1C1C1);
            descTV.setText(R.string.par_result_failure_coupon_hint);
            bottomTV.setText(R.string.par_result_repay);
            bottomTV.setTextColor(0xFF7D7D7D);
            bottomTV.setBackgroundResource(R.drawable.shape_rounded_white_btn);
        }
    }
}
