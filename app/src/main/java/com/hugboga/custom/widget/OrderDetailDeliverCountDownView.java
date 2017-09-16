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

    private static final int FRE_INTERVAL = 5 * 1000;
    private static final int LATE_INTERVAL = 60 * 1000;

    private long lastRemainTime = 0;
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

        countdownView.start(deliverInfoBean.deliverTimeSpan);
        countdownView.setOnCountdownEndListener(this);
        float progress =  360 - 360 * (deliverInfoBean.deliverTimeSpan / (float)deliverInfoBean.stayTime);
        progressView.setProgress(progress);

        setOnCountdownIntervalListener(1000, deliverInfoBean);
    }

    private void setOnCountdownIntervalListener(long interval, final DeliverInfoBean deliverInfoBean) {
        countdownView.setOnCountdownIntervalListener(interval, new CountdownView.OnCountdownIntervalListener() {
            @Override
            public void onInterval(CountdownView cv, long remainTime) {
                if (deliverInfoBean.stayTime > remainTime) {
                    float progress = 360 - 360 * (remainTime / (float)deliverInfoBean.stayTime);
                    if (progress > 0) {
                        progressView.setProgress(progress);
                    }

                    long mInterval = 0;//控制刷新间隔时间
                    if (deliverInfoBean.refreshCount >= 0) {//先每5秒请求一次并更新数据，连续请求6次后，恢复到1分钟请求一次
                        mInterval = FRE_INTERVAL;
                    } else {
                        mInterval = LATE_INTERVAL;//1分刷新一次
                    }

                    if (lastRemainTime == 0 || lastRemainTime - remainTime > mInterval) {
                        lastRemainTime = remainTime;
                        if (onUpdateListener != null) {
                            onUpdateListener.onUpdate(false);
                        }
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
        void onUpdate(boolean isEnd);
    }

    public void setOnUpdateListener(OnUpdateListener onUpdateListener) {
        this.onUpdateListener = onUpdateListener;
    }
}
