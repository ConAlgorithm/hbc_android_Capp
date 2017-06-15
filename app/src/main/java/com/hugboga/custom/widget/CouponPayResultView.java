package com.hugboga.custom.widget;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.CouponActivity;
import com.hugboga.custom.activity.LoginActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.utils.AlertDialogUtils;
import com.hugboga.custom.utils.CommonUtils;
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
    private String areaCode;
    private String phone;

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
        if (isPaySucceed && CommonUtils.isLogin(getContext())) {
            String userAreaCode = UserEntity.getUser().getAreaCode(getContext());
            String userPhone = UserEntity.getUser().getPhone(getContext());
            if (TextUtils.equals(CommonUtils.removePhoneCodeSign(userAreaCode), CommonUtils.removePhoneCodeSign(areaCode)) && TextUtils.equals(userPhone, phone)) {
                Intent intent = new Intent(getContext(), CouponActivity.class);
                intent.putExtra(Constants.PARAMS_SOURCE, "买卷支付结果页");
                getContext().startActivity(intent);
            } else {
                String content =  String.format("需使用%1$s %2$s登录才能看到已购买的优惠券", CommonUtils.addPhoneCodeSign(areaCode), phone);
                AlertDialogUtils.showAlertDialog(getContext(), content, "切换账号", "不了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CommonUtils.logout(getContext());
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        intent.putExtra(LoginActivity.KEY_AREA_CODE, CommonUtils.removePhoneCodeSign(areaCode));
                        intent.putExtra(LoginActivity.KEY_PHONE, phone);
                        intent.putExtra(Constants.PARAMS_SOURCE, "买卷支付结果页");
                        getContext().startActivity(intent);
                        dialog.dismiss();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            }
        } else {
            ((Activity) getContext()).finish();
        }
    }

    public void initView(boolean _isPaySucceed, String areaCode, String phone) {
        this.isPaySucceed = _isPaySucceed;
        this.areaCode = areaCode;
        this.phone = phone;
        if (isPaySucceed) {
            successedIV.setBackgroundResource(R.mipmap.activity_pay_successful);
            successedTV.setText("支付成功");
            successedTV.setTextColor(getContext().getResources().getColor(R.color.default_btn_yellow));
            descTV.setText(String.format("该优惠券已放入%1$s %2$s的账户\n用该手机号登录皇包车即可查看或使用", CommonUtils.addPhoneCodeSign(areaCode), phone));
            bottomTV.setText("查看优惠券");
            bottomTV.setTextColor(0xFFFFFFFF);
            bottomTV.setBackgroundResource(R.drawable.shape_rounded_yellow_btn);
        } else {
            successedIV.setBackgroundResource(R.mipmap.activity_pay_failure);
            successedTV.setText("支付失败");
            successedTV.setTextColor(0xFFC1C1C1);
            descTV.setText("很抱歉，支付失败\n您可以点击按钮尝试重新支付");
            bottomTV.setText("重新支付");
            bottomTV.setTextColor(0xFF7D7D7D);
            bottomTV.setBackgroundResource(R.drawable.shape_rounded_white_btn);
        }
    }
}
