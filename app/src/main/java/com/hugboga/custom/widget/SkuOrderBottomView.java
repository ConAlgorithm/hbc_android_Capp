package com.hugboga.custom.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 16/12/17.
 */
public class SkuOrderBottomView extends LinearLayout {

    @Bind(R.id.sku_order_bottom_price_tv)
    TextView priceTV;
    @Bind(R.id.sku_order_bottom_pay_tv)
    TextView payTV;
    @Bind(R.id.sku_order_bottom_price_layout)
    RelativeLayout priceLayout;

    @Bind(R.id.sku_order_bottom_price_detail_arrow_iv)
    ImageView arrowIV;

    public OnSubmitOrderListener listener;

    private int shouldPrice;
    private int discountPrice;

    private OrderPricePopupLayout orderPricePopupLayout;

    public SkuOrderBottomView(Context context) {
        this(context, null);
    }

    public SkuOrderBottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        View view = inflate(context, R.layout.view_sku_order_bottom, this);
        ButterKnife.bind(view);
    }

    public void setOrderPricePopupLayout(OrderPricePopupLayout orderPricePopupLayout) {
        this.orderPricePopupLayout = orderPricePopupLayout;
    }

    public void updatePrice(int shouldPrice, int discountPrice) {
        this.shouldPrice = shouldPrice;
        this.discountPrice = discountPrice;
        priceTV.setText(getContext().getString(R.string.sign_rmb) + shouldPrice);
        orderPricePopupLayout.updatePriceItemView((shouldPrice + discountPrice), discountPrice, shouldPrice);
    }

    @OnClick({R.id.sku_order_bottom_pay_tv})
    public void submitOrder(View view) {
        dismissPopupWindow();
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

    public void dismissPopupWindow() {
        if (orderPricePopupLayout != null) {
            orderPricePopupLayout.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.sku_order_bottom_price_detail_layout})
    public void onShowPricePopup() {
        showPopupWindow((shouldPrice + discountPrice), discountPrice, shouldPrice);
    }

    public void showPopupWindow(int orderPrice, int discountPrice, int actualPay) {
        if (orderPricePopupLayout.getVisibility() == View.VISIBLE) {
            arrowIV.setBackgroundResource(R.mipmap.share_withdraw);
            orderPricePopupLayout.setVisibility(View.GONE);
            return;
        } else {
            arrowIV.setBackgroundResource(R.mipmap.share_unfold);
        }
        orderPricePopupLayout.showPopupWindow(orderPrice, discountPrice, actualPay);
    }

}
