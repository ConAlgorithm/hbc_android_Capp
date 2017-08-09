package com.hugboga.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.utils.CommonUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 16/12/17.
 */
public class SkuOrderBottomView extends LinearLayout {

    @Bind(R.id.sku_order_bottom_pay_tv)
    TextView payTV;

    @Bind(R.id.sku_order_bottom_should_price_tv)
    TextView shouldPriceTV;
    @Bind(R.id.sku_order_bottom_total_price_tv)
    TextView totalPriceTV;
    @Bind(R.id.sku_order_bottom_discount_price_tv)
    TextView discountPriceTV;

    @Bind(R.id.sku_order_bottom_selected_guide_hint_tv)
    TextView selectedGuideHintTV;

    @Bind(R.id.sku_order_bottom_progress_layout)
    FrameLayout progressLayout;

    private OnSubmitOrderListener listener;

    private int orderType;
    private boolean isGuides;
    private boolean isSeckills;
    private double shouldPrice;

    public SkuOrderBottomView(Context context) {
        this(context, null);
    }

    public SkuOrderBottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        View view = inflate(context, R.layout.view_sku_order_bottom, this);
        ButterKnife.bind(view);
    }

    public void updatePrice(double shouldPrice, double discountPrice) {
        this.shouldPrice = shouldPrice;
        shouldPriceTV.setText(getContext().getString(R.string.sign_rmb) + CommonUtils.doubleTrans(shouldPrice));
        totalPriceTV.setText(String.format("总额: ¥%1$s","" + CommonUtils.doubleTrans(shouldPrice + discountPrice)));
        if (discountPrice <= 0) {
            discountPriceTV.setVisibility(View.GONE);
        } else {
            discountPriceTV.setVisibility(View.VISIBLE);
            discountPriceTV.setText(String.format("已减: ¥%1$s","" + CommonUtils.doubleTrans(discountPrice)));
        }

        if (orderType != 0) {
            setHintTV(orderType, isGuides, isSeckills);
        }
    }

    public void onLoading() {
        payTV.setEnabled(false);
        progressLayout.setVisibility(View.VISIBLE);
    }

    public void onSucceed() {
        payTV.setEnabled(true);
        progressLayout.setVisibility(View.GONE);
    }

    @OnClick({R.id.sku_order_bottom_pay_tv})
    public void submitOrder(View view) {
        if (listener != null) {
            listener.onSubmitOrder();
        }
    }

    public interface OnSubmitOrderListener {
        public void onSubmitOrder();
    }

    public void setOnSubmitOrderListener(OnSubmitOrderListener listener) {
        this.listener = listener;
    }

    public TextView getSelectedGuideHintTV() {
        return selectedGuideHintTV;
    }

    public void setHintTV(int orderType, boolean isGuides, boolean isSeckills) {
        this.orderType = orderType;
        this.isGuides = isGuides;
        this.isSeckills = isSeckills;

        String hint1 = "支付后参与砍价活动，最多可减200元哦！";
        String hint2 = "支付后还可以挑选司导哦~";
        String showText = "";
        boolean isShowHint1 = shouldPrice > 200;

        boolean isDaily = orderType == 3 || orderType == 888 || orderType == 5 || orderType == 6;

        if (isSeckills) {
            if (isDaily) {
                showText = hint2;
            } else {
                showText = null;
            }
        } else if (isGuides) {
            showText = isShowHint1 ? hint1 : null;
        } else if (isDaily) {
            showText = hint2;
        } else {
            showText = isShowHint1 ? hint1 : null;
        }
        selectedGuideHintTV.setText(showText);
        selectedGuideHintTV.setVisibility(showText == null ? GONE : VISIBLE);
    }

}
