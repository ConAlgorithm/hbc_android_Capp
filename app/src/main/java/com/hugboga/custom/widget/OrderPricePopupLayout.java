package com.hugboga.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/3/28.
 */

public class OrderPricePopupLayout extends RelativeLayout {

    @Bind(R.id.order_price_bottom_container_layout)
    LinearLayout containerLayout;

    public OrderPricePopupLayout(Context context) {
        this(context, null);
    }

    public OrderPricePopupLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_order_price_bottom_popup, this);
        ButterKnife.bind(view);
    }

    public void showPopupWindow(int orderPrice, int discountPrice, int actualPay) {
        final String signRMB = getContext().getResources().getString(R.string.sign_rmb);
        if (containerLayout.getChildCount() == 0) {
            containerLayout.removeAllViews();
            addItemView("订单金额",  signRMB + orderPrice, 0xFF333333);
            addItemView("优惠金额", "-" + signRMB + discountPrice, 0xFF333333);
            addItemView("还需支付", signRMB + actualPay, 0xFFF63308);
        } else {
            updatePriceItemView(orderPrice, discountPrice, actualPay);
        }
        setVisibility(View.VISIBLE);
    }

    private void addItemView(String title, String price, int priceColor) {
        RelativeLayout itemView = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.view_order_price_bottom_popup_item, null, false);
        TextView titleTV = (TextView) itemView.findViewById(R.id.item_order_price_title_tv);
        titleTV.setText(title);
        TextView priceTV = (TextView) itemView.findViewById(R.id.item_order_price_tv);
        priceTV.setText(price);
        priceTV.setTextColor(priceColor);
        containerLayout.addView(itemView, LinearLayout.LayoutParams.MATCH_PARENT, UIUtils.dip2px(22));
    }

    private void updateItemView(int index, String price) {
        RelativeLayout itemView = (RelativeLayout)containerLayout.getChildAt(index);
        TextView priceTV = (TextView) itemView.findViewById(R.id.item_order_price_tv);
        priceTV.setText(price);
    }

    public void updatePriceItemView(int orderPrice, int discountPrice, int actualPay) {
        if (containerLayout.getChildCount() > 0) {
            final String signRMB = getContext().getResources().getString(R.string.sign_rmb);
            updateItemView(0, signRMB + orderPrice);
            updateItemView(1, "-" + signRMB + discountPrice);
            updateItemView(2, signRMB + actualPay);
        }
    }
}
