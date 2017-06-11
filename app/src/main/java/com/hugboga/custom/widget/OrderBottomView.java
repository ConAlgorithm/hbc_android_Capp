package com.hugboga.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.CarBean;
import com.hugboga.custom.data.bean.CarListBean;
import com.hugboga.custom.utils.CommonUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 17/5/18.
 */

public class OrderBottomView extends RelativeLayout {

    @Bind(R.id.order_bottom_should_price_tv)
    TextView shouldPriceTV;
    @Bind(R.id.order_bottom_distance_tv)
    TextView distanceTV;

    private OnConfirmListener listener;

    public OrderBottomView(Context context) {
        this(context, null);
    }

    public OrderBottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_order_bottom, this);
        ButterKnife.bind(view);
        setBackgroundColor(0xFFFFFFFF);
    }

    public void setData(CarListBean carListBean, CarBean carBean) {
        int price = carListBean.isSeckills ? carBean.seckillingPrice : carBean.price;
        shouldPriceTV.setText(getContext().getResources().getString(R.string.sign_rmb) + price);
        distanceTV.setVisibility(View.VISIBLE);
        distanceTV.setText("全程预计: " + CommonUtils.doubleTrans(carListBean.distance) + "公里/" + carListBean.interval + "分钟");
    }

    @OnClick(R.id.order_bottom_confirm_tv)
    public void onConfirm() {
        if (listener != null) {
            listener.onConfirm();
        }
    }

    public interface OnConfirmListener {
        public void onConfirm();
    }

    public void setOnConfirmListener(OnConfirmListener listener) {
        this.listener = listener;
    }
}
