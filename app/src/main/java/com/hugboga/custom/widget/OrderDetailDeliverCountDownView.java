package com.hugboga.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
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
public class OrderDetailDeliverCountDownView extends LinearLayout implements HbcViewBehavior, CountdownView.OnCountdownEndListener{

    @Bind(R.id.deliver_countdown_view)
    CountdownView countdownView;
    @Bind(R.id.deliver_countdown_progress_view)
    DeliverCircleProgressView progressView;

    private final int oneMinute = 60 * 1000;
    private final int tenMinute = oneMinute * 10;

    private OnUpdateListener onUpdateListener;

    public OrderDetailDeliverCountDownView(Context context) {
        this(context, null);
    }

    public OrderDetailDeliverCountDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_deliver_countdown, this);
        ButterKnife.bind(view);
    }

    @Override
    public void update(Object _data) {
        final DeliverInfoBean deliverInfoBean = (DeliverInfoBean) _data;
        if (deliverInfoBean == null) {
            return;
        }

        countdownView.start(deliverInfoBean.span);
        countdownView.setOnCountdownEndListener(this);
        int progress = (int)(360 * (deliverInfoBean.span /(float) deliverInfoBean.stayTime));
        progressView.setProgress(progress);

        if (deliverInfoBean.span > tenMinute) {//10分钟刷新一次
            setOnCountdownIntervalListener(tenMinute, deliverInfoBean);
        } else {//1分钟刷新一次
            setOnCountdownIntervalListener(oneMinute, deliverInfoBean);
        }
    }

    private void setOnCountdownIntervalListener(long interval, final DeliverInfoBean deliverInfoBean) {
        countdownView.setOnCountdownIntervalListener(interval, new CountdownView.OnCountdownIntervalListener() {
            @Override
            public void onInterval(CountdownView cv, long remainTime) {
                if (deliverInfoBean.stayTime > remainTime) {
                    int progress = 360 - (int)(360 * (remainTime /(float) deliverInfoBean.stayTime));
                    if (progress > 0) {
                        progressView.setProgress(progress);
                    }
                    if (onUpdateListener != null) {
                        onUpdateListener.onUpdate(false);
                    }
                }
            }
        });
    }

    @Override
    public void onEnd(CountdownView cv) {
        if (onUpdateListener != null) {
            onUpdateListener.onUpdate(true);
        }
    }

    public interface OnUpdateListener {
        public void onUpdate(boolean isEnd);
    }

    public void setOnUpdateListener(OnUpdateListener onUpdateListener) {
        this.onUpdateListener = onUpdateListener;
    }
}
