package com.hugboga.custom.widget;

import android.content.Context;
import android.text.TextUtils;
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
    @Bind(R.id.order_bottom_conpons_tip_tv)
    TextView conponsTipTV;

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
        double price = carListBean.isSeckills ? carBean.seckillingPrice : carBean.price;
        shouldPriceTV.setText(getContext().getResources().getString(R.string.sign_rmb) + CommonUtils.doubleTrans(price));
        distanceTV.setVisibility(View.VISIBLE);
        distanceTV.setText(CommonUtils.getString(R.string.order_price_estimated, CommonUtils.doubleTrans(carListBean.distance), carListBean.interval));
    }

    public void setConponsTip(String tip) {
        if (TextUtils.isEmpty(tip)) {
            conponsTipTV.setVisibility(View.GONE);
        } else {
            conponsTipTV.setVisibility(View.VISIBLE);
            conponsTipTV.setText(String.format("(%1$s)", tip));
        }
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
