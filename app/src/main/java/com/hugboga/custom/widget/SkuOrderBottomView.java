package com.hugboga.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
    @Bind(R.id.sku_order_bottom_discount_tv)
    TextView discountTV;
    @Bind(R.id.sku_order_bottom_pay_tv)
    TextView payTV;

    public OnSubmitOrderListener listener;

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
        priceTV.setText(getContext().getString(R.string.sign_rmb) + shouldPrice);
        discountTV.setText("已优惠" + getContext().getString(R.string.sign_rmb) + discountPrice);

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

    private PopupWindow popup;
    private LinearLayout popupLayout;

    public void showPopupWindow(int orderPrice, int discountPrice, int actualPay) {
        popupLayout = new LinearLayout(getContext());
        popupLayout.setOrientation(LinearLayout.VERTICAL);
        popupLayout.setPadding(0, UIUtils.dip2px(10) , 0, UIUtils.dip2px(10));
        addItemView("订单金额", "" + orderPrice, 0xFF333333);
        addItemView("优惠金额", "" + discountPrice, 0xFF333333);
        addItemView("还需支付", "" + actualPay, 0xFFF63308);
    }

    private void addItemView(String title, String price, int priceColor) {
        LinearLayout itemView = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.view_order_price_bottom_popup_item, null, false);
        TextView titleTV = (TextView) itemView.findViewById(R.id.item_order_price_title_tv);
        titleTV.setText(title);
        TextView priceTV = (TextView) itemView.findViewById(R.id.item_order_price_tv);
        priceTV.setText(price);
        priceTV.setTextColor(priceColor);
        popupLayout.addView(itemView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }



}
