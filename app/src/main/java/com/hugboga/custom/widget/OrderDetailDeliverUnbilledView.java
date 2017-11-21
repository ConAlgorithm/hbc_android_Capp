package com.hugboga.custom.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.DeliverInfoBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.iwgang.countdownview.CountdownView;

/**
 * Created by qingcha on 16/9/8.
 */
public class OrderDetailDeliverUnbilledView extends LinearLayout implements HbcViewBehavior{

    @BindView(R.id.order_detail_deliver_unbilled_title_tv)
    TextView titleTV;
    @BindView(R.id.order_detail_deliver_unbilled_subtitle_tv)
    TextView subtitleTV;
    @BindView(R.id.order_detail_deliver_unbilled_countdown_view)
    CountdownView countdownView;

    public OrderDetailDeliverUnbilledView(Context context) {
        this(context, null);
    }

    public OrderDetailDeliverUnbilledView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.HORIZONTAL);
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
        subtitleTV.setVisibility(TextUtils.isEmpty(deliverInfoBean.deliverDetail) ? View.GONE : View.VISIBLE);
        subtitleTV.setText(deliverInfoBean.deliverDetail);
        countdownView.start(deliverInfoBean.span);
    }

    public void setOnCountdownEndListener(CountdownView.OnCountdownEndListener onCountdownEndListener) {
        countdownView.setOnCountdownEndListener(onCountdownEndListener);
    }

    public void stop() {
        if (countdownView != null) {
            try {
                countdownView.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
