package com.hugboga.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.DeliverInfoBean;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.iwgang.countdownview.CountdownView;

/**
 * Created by qingcha on 16/9/8.
 */

public class OrderDetailDeliverCountDownView extends LinearLayout implements HbcViewBehavior{

    @Bind(R.id.deliver_countdown_view)
    CountdownView countdownView;
    @Bind(R.id.deliver_countdown_progress_view)
    DeliverCircleProgressView progressView;

    public OrderDetailDeliverCountDownView(Context context) {
        this(context, null);
    }

    public OrderDetailDeliverCountDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_deliver_countdown, this);
        ButterKnife.bind(view);
        countdownView.setOnCountdownIntervalListener(10000, new CountdownView.OnCountdownIntervalListener() {
            @Override
            public void onInterval(CountdownView cv, long remainTime) {
//                progressView.setProgress();
            }
        });
    }

    @Override
    public void update(Object _data) {
        DeliverInfoBean deliverInfoBean = (DeliverInfoBean) _data;
        if (deliverInfoBean == null) {
            return;
        }
        countdownView.start(deliverInfoBean.span);
    }

    public void setOnCountdownEndListener(CountdownView.OnCountdownEndListener onCountdownEndListener) {
        countdownView.setOnCountdownEndListener(onCountdownEndListener);
    }
}
