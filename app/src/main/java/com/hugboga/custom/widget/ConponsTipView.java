package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.LoginActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CouponsOrderTipBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.request.RequestCouponsOrderTip;
import com.hugboga.custom.utils.ApiReportHelper;
import com.hugboga.custom.utils.UIUtils;

/**
 * Created by qingcha on 17/6/29.
 */
public class ConponsTipView extends TextView{

    private OnCouponsTipRequestSucceedListener listener;
    private CouponsOrderTipBean couponsOrderTipBean;

    public ConponsTipView(Context context) {
        this(context, null);
    }

    public ConponsTipView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setTextSize(14);
        int padding = UIUtils.dip2px(10);
        setPadding(padding, padding, padding, padding);
    }

    public void update(final int orderType) {
        if (couponsOrderTipBean != null) {
            return;
        }
        if (UserEntity.getUser().isLogin(getContext())) {
            setText("");
            RequestCouponsOrderTip requestCouponsOrderTip = new RequestCouponsOrderTip(getContext(), "" + orderType);
            HttpRequestUtils.request(getContext(), requestCouponsOrderTip, new HttpRequestListener() {
                @Override
                public void onDataRequestSucceed(BaseRequest request) {
                    ApiReportHelper.getInstance().addReport(request);
                    couponsOrderTipBean = ((RequestCouponsOrderTip)request).getData();
                    if (couponsOrderTipBean == null || TextUtils.isEmpty(couponsOrderTipBean.tips)) {
                        setVisibility(GONE);
                    } else {
                        setVisibility(VISIBLE);
                        setTextColor(getContext().getResources().getColor(R.color.default_price_red));
                        setText(couponsOrderTipBean.tips);
                    }
                    if (listener != null) {
                        listener.onCouponsTipRequestSucceed(couponsOrderTipBean);
                    }
                }

                @Override
                public void onDataRequestCancel(BaseRequest request) {

                }

                @Override
                public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
                      setVisibility(GONE);
                }
            }, false);
        } else {
            String hintText = "登录 可享我的优惠权益";
            SpannableString spannableString = new SpannableString(hintText);
            spannableString.setSpan(new MyClickSpan(getContext()),  0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            setText(spannableString);
            setMovementMethod(LinkMovementMethod.getInstance());
            setVisibility(VISIBLE);
            setTextColor(0xFF7F7F7F);
        }
    }

    public static class MyClickSpan extends ClickableSpan {

        private Context context;

        public MyClickSpan(Context context)  {
            this.context = context;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(context.getResources().getColor(R.color.default_yellow));
            ds.setUnderlineText(true);
        }

        @Override
        public void onClick(View widget) {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.putExtra(Constants.PARAMS_SOURCE, widget.getContext().getResources().getString(R.string.custom_chartered));
            context.startActivity(intent);
        }
    }

    public void showView() {
        if (couponsOrderTipBean != null && !TextUtils.isEmpty(couponsOrderTipBean.tips)) {
            setVisibility(VISIBLE);
        }
    }

    public interface OnCouponsTipRequestSucceedListener {
        public void onCouponsTipRequestSucceed(CouponsOrderTipBean couponsOrderTipBean);
    }

    public void setOnCouponsTipRequestSucceedListener(OnCouponsTipRequestSucceedListener listener) {
        this.listener = listener;
    }

}
