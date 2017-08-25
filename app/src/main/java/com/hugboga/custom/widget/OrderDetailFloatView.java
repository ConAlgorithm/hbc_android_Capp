package com.hugboga.custom.widget;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.OrderStatus;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 16/6/2.
 */
public class OrderDetailFloatView extends LinearLayout implements HbcViewBehavior {

    @Bind(R.id.order_detail_pay_price_tv)
    public TextView needPayTV;
    @Bind(R.id.order_detail_price_detail_tv)
    public TextView priceDetailTV;

    public OrderDetailFloatView(Context context) {
        this(context, null);
    }

    public OrderDetailFloatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        setVisibility(View.GONE);
        View view = inflate(getContext(), R.layout.view_order_detail_pay, this);
        ButterKnife.bind(view);

        priceDetailTV.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        priceDetailTV.getPaint().setAntiAlias(true);
    }

    @Override
    public void update(Object _data) {
        if (_data == null) {
            return;
        }
        final OrderBean orderBean = (OrderBean) _data;
        if (orderBean.orderStatus == OrderStatus.INITSTATE) {//待支付
            setVisibility(View.VISIBLE);
            needPayTV.setText(getContext().getResources().getString(R.string.sign_rmb) + String.valueOf(Math.round(orderBean.orderPriceInfo.actualPay)));
            //立即支付
            findViewById(R.id.order_detail_pay_tv).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new EventAction(EventType.ORDER_DETAIL_PAY, orderBean.orderNo));
                }
            });
            if (orderBean.orderType == 3 || orderBean.orderType == 888) {
                priceDetailTV.setVisibility(View.VISIBLE);
            } else {
                priceDetailTV.setVisibility(View.GONE);
            }

        } else {
            setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.order_detail_price_detail_tv})
    public void intentPriceInfo() {
    }
}
