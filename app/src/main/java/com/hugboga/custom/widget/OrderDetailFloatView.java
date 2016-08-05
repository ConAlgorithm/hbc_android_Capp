package com.hugboga.custom.widget;

import android.content.Context;
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

/**
 * Created by qingcha on 16/6/2.
 */
public class OrderDetailFloatView extends LinearLayout implements HbcViewBehavior {

    public OrderDetailFloatView(Context context) {
        this(context, null);
    }

    public OrderDetailFloatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        setVisibility(View.GONE);
    }

    @Override
    public void update(Object _data) {
        if (_data == null) {
            return;
        }
        final OrderBean orderBean = (OrderBean) _data;
        if (orderBean.orderStatus == OrderStatus.INITSTATE) {//待支付
            setVisibility(View.VISIBLE);
            removeAllViews();
            inflate(getContext(), R.layout.view_order_detail_pay, this);
            TextView needPayTV = (TextView) findViewById(R.id.order_detail_pay_price_tv);
            needPayTV.setText(String.valueOf(orderBean.orderPriceInfo.actualPay));
            //立即支付
            findViewById(R.id.order_detail_pay_tv).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new EventAction(EventType.ORDER_DETAIL_PAY, orderBean.orderNo));
                }
            });
        } else if (orderBean.insuranceEnable) { //是否添加保险
            setVisibility(View.VISIBLE);
            removeAllViews();
            inflate(getContext(), R.layout.view_order_detail_insurance, this);
            TextView timeTV = (TextView) findViewById(R.id.order_detail_insurance_time_tv);
            timeTV.setText(orderBean.insuranceTips);
            //皇包车免费赠送保险H5
            findViewById(R.id.order_detail_insurance_explain_iv).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new EventAction(EventType.ORDER_DETAIL_INSURANCE_H5, orderBean.orderNo));
                }
            });
            //添加投保人
            findViewById(R.id.order_detail_insurance_add_tv).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new EventAction(EventType.ORDER_DETAIL_ADD_INSURER, orderBean.orderNo));
                }
            });
        } else {
            setVisibility(View.GONE);
        }
    }
}
