package com.hugboga.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.OrderDetailActivity;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.OrderStatus;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.statistic.sensors.SensorsUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 16/6/2.
 */
public class OrderDetailFloatView extends LinearLayout implements HbcViewBehavior {

    @BindView(R.id.order_detail_pay_price_tv)
    public TextView needPayTV;

    public OrderDetailFloatView(Context context) {
        this(context, null);
    }

    public OrderDetailFloatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        setVisibility(View.GONE);
        View view = inflate(getContext(), R.layout.view_order_detail_pay, this);
        ButterKnife.bind(view);
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
                    if (getContext() != null && (getContext() instanceof OrderDetailActivity)) {
                        SensorsUtils.onAppClick(((OrderDetailActivity) getContext()).getEventSource(), "去支付",
                                ((OrderDetailActivity) getContext()).getIntentSource());
                    }
                }
            });
        } else {
            setVisibility(View.GONE);
        }
    }
}
