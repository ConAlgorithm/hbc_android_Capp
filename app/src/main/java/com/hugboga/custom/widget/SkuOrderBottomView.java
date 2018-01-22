package com.hugboga.custom.widget;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 16/12/17.
 */
public class SkuOrderBottomView extends LinearLayout {

    @BindView(R.id.sku_order_bottom_pay_tv)
    TextView payTV;

    @BindView(R.id.sku_order_bottom_price_detail_tv)
    TextView priceDetailTV;

    @BindView(R.id.sku_order_bottom_should_price_tv)
    TextView shouldPriceTV;
    @BindView(R.id.sku_order_bottom_total_price_tv)
    TextView totalPriceTV;
    @BindView(R.id.sku_order_bottom_discount_price_tv)
    TextView discountPriceTV;

    @BindView(R.id.sku_order_bottom_selected_guide_hint_tv)
    TextView selectedGuideHintTV;

    @BindView(R.id.sku_order_bottom_progress_layout)
    FrameLayout progressLayout;

    private OnSubmitOrderListener listener;
    private OnIntentPriceInfoListener onIntentPriceInfoListener;

    private int orderType;
    private boolean isGuides;
    private boolean isSeckills;
    private double shouldPrice;
    public int reconfirmFlag;    // 二次确认标示 0：否 1：是
    public String reconfirmTip;  // 二次确认提示

    public SkuOrderBottomView(Context context) {
        this(context, null);
    }

    public SkuOrderBottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        View view = inflate(context, R.layout.view_sku_order_bottom, this);
        ButterKnife.bind(view);

        priceDetailTV.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        priceDetailTV.getPaint().setAntiAlias(true);
    }

    public void updatePrice(double shouldPrice, double discountPrice) {
        this.shouldPrice = shouldPrice;
        shouldPriceTV.setText(getContext().getString(R.string.sign_rmb) + CommonUtils.doubleTrans(shouldPrice));
        totalPriceTV.setText(CommonUtils.getString(R.string.order_bottom_total_price, "" + CommonUtils.doubleTrans(shouldPrice + discountPrice)));
        if (discountPrice <= 0) {
            discountPriceTV.setVisibility(View.GONE);
        } else {
            discountPriceTV.setVisibility(View.VISIBLE);
            discountPriceTV.setText(CommonUtils.getString(R.string.order_bottom_discount_price, "" + CommonUtils.doubleTrans(discountPrice)));
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

    @OnClick({R.id.sku_order_bottom_price_detail_tv})
    public void intentPriceInfo() {
        if (onIntentPriceInfoListener != null) {
            onIntentPriceInfoListener.intentPriceInfo();
        }
    }


    public void setHintData(int orderType, boolean isGuides, boolean isSeckills, int reconfirmFlag, String reconfirmTip) {
        this.orderType = orderType;
        this.isGuides = isGuides;
        this.isSeckills = isSeckills;
        this.reconfirmFlag = reconfirmFlag;
        this.reconfirmTip = reconfirmTip;
        if (isSeckills) {
            setHintTV();
        }
        if (orderType == 3 || orderType == 888) {
            priceDetailTV.setVisibility(View.VISIBLE);
        } else {
            priceDetailTV.setVisibility(View.GONE);
        }
    }

    public void setHintTV() {
        if (orderType == 0) {
            selectedGuideHintTV.setVisibility(GONE);
        }
        String hint1 = CommonUtils.getString(R.string.order_bottom_hint1);
        String hint2 = CommonUtils.getString(R.string.order_bottom_hint2);
        String showText = "";
        boolean isShowHint1 = shouldPrice > 200;
        int bgColor = 0xFFB2C0D6;

        boolean isDaily = orderType == 3 || orderType == 888 || orderType == 5 || orderType == 6;

        if (isSeckills) {
            if (isDaily) {
                showText = hint2;
            } else {
                showText = null;
            }
        } else {
            if (reconfirmFlag == 1 && !TextUtils.isEmpty(reconfirmTip)) {
                showText = reconfirmTip;
                bgColor = 0xFFF56363;
            } else if (isGuides) {
                showText = isShowHint1 ? hint1 : null;
            } else if (isDaily) {
                showText = hint2;
            } else {
                showText = isShowHint1 ? hint1 : null;
            }
        }

        selectedGuideHintTV.setBackgroundColor(bgColor);
        selectedGuideHintTV.setText(showText);
        selectedGuideHintTV.setVisibility(showText == null ? GONE : VISIBLE);
    }

    public interface OnIntentPriceInfoListener {
        public void intentPriceInfo();
    }

    public void setOnIntentPriceInfoListener(OnIntentPriceInfoListener listener) {
        this.onIntentPriceInfoListener = listener;
    }

}
