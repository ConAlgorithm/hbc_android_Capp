package com.hugboga.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.DeliverInfoBean;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.iwgang.countdownview.CountdownView;

/**
 * Created by qingcha on 16/9/8.
 */
public class OrderDetailDeliverUnbilledView extends LinearLayout implements HbcViewBehavior{

    @Bind(R.id.order_detail_deliver_unbilled_title_tv)
    TextView titleTV;
    @Bind(R.id.order_detail_deliver_unbilled_countdown_view)
    CountdownView countdownView;

    public OrderDetailDeliverUnbilledView(Context context) {
        this(context, null);
    }

    public OrderDetailDeliverUnbilledView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER_VERTICAL);
        final View view = inflate(context, R.layout.view_order_detail_deliver_unbilled, this);
        ButterKnife.bind(view);
    }

    @Override
    public void update(Object _data) {
        DeliverInfoBean deliverInfoBean = (DeliverInfoBean) _data;
        if (deliverInfoBean == null) {
            return;
        }
        titleTV.setText(deliverInfoBean.deliverMessage);
        countdownView.start(deliverInfoBean.deliverTimeSpan);
    }

    public void setOnCountdownEndListener(CountdownView.OnCountdownEndListener onCountdownEndListener) {
        countdownView.setOnCountdownEndListener(onCountdownEndListener);
    }
}
