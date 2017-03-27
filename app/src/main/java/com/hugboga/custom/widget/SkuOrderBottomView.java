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

    private PopupWindow popup;
    private RelativeLayout popupLayout;

    public SkuOrderBottomView(Context context) {
        this(context, null);
    }

    public SkuOrderBottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        View view = inflate(context, R.layout.view_sku_order_bottom, this);
        ButterKnife.bind(view);
    }

    public void updatePrice(int shouldPrice, int discountPrice) {
        this.shouldPrice = shouldPrice;
        this.discountPrice = discountPrice;
        priceTV.setText(getContext().getString(R.string.sign_rmb) + shouldPrice);
        updatePriceItemView((shouldPrice + discountPrice), discountPrice, shouldPrice);

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
        if (popup != null && popup.isShowing()) {
            popup.dismiss();
        }
    }

    @OnClick({R.id.sku_order_bottom_price_detail_layout})
    public void onShowPricePopup() {
        showPopupWindow((shouldPrice + discountPrice), discountPrice, shouldPrice);
    }

    public void showPopupWindow(int orderPrice, int discountPrice, int actualPay) {
        if (popup != null && popup.isShowing()) {
            arrowIV.setBackgroundResource(R.mipmap.share_withdraw);
            popup.dismiss();
            return;
        } else {
            arrowIV.setBackgroundResource(R.mipmap.share_unfold);
        }
        final String signRMB = getContext().getResources().getString(R.string.sign_rmb);
        if (popupLayout == null) {
            popupLayout = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.view_order_price_bottom_popup, null, false);
            addItemView("订单金额",  signRMB + orderPrice, 0xFF333333);
            addItemView("优惠金额", "-" + signRMB + discountPrice, 0xFF333333);
            addItemView("还需支付", signRMB + actualPay, 0xFFF63308);
        } else {
            updatePriceItemView(orderPrice, discountPrice, actualPay);
        }
        int[] location = new int[2];
        priceLayout.getLocationOnScreen(location);
        int popupHeight = UIUtils.dip2px(20) + UIUtils.dip2px(22) * 3;
        if (popup == null) {
            popup = new PopupWindow(popupLayout, LinearLayout.LayoutParams.MATCH_PARENT, popupHeight);
            popup.setBackgroundDrawable(new BitmapDrawable());
            popup.setOutsideTouchable(false);
            popup.setFocusable(false);
        }
        popup.showAtLocation(this, Gravity.NO_GRAVITY, 0, location[1] - popupHeight);

    }

    private void addItemView(String title, String price, int priceColor) {
        RelativeLayout itemView = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.view_order_price_bottom_popup_item, null, false);
        TextView titleTV = (TextView) itemView.findViewById(R.id.item_order_price_title_tv);
        titleTV.setText(title);
        TextView priceTV = (TextView) itemView.findViewById(R.id.item_order_price_tv);
        priceTV.setText(price);
        priceTV.setTextColor(priceColor);
        LinearLayout containerLayout = (LinearLayout)popupLayout.findViewById(R.id.order_price_bottom_container_layout);
        containerLayout.addView(itemView, LayoutParams.MATCH_PARENT, UIUtils.dip2px(22));
    }

    private void updateItemView(int index, String price) {
        LinearLayout containerLayout = (LinearLayout)popupLayout.findViewById(R.id.order_price_bottom_container_layout);
        RelativeLayout itemView = (RelativeLayout)containerLayout.getChildAt(index);
        TextView priceTV = (TextView) itemView.findViewById(R.id.item_order_price_tv);
        priceTV.setText(price);
    }

    public void updatePriceItemView(int orderPrice, int discountPrice, int actualPay) {
        if (popupLayout != null) {
            final String signRMB = getContext().getResources().getString(R.string.sign_rmb);
            updateItemView(0, signRMB + orderPrice);
            updateItemView(1, "-" + signRMB + discountPrice);
            updateItemView(2, signRMB + actualPay);
        }
    }

}
