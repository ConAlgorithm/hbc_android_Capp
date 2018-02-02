package com.hugboga.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.DeliverInfoBean;
import com.hugboga.custom.utils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.iwgang.countdownview.CountdownView;
import cn.iwgang.countdownview.DynamicConfig;

/**
 * Created by qingcha on 16/9/8.
 */
public class OrderDetailDeliverCountDownView extends LinearLayout implements HbcViewBehavior, CountdownView.OnCountdownEndListener{

    @BindView(R.id.deliver_countdown_view)
    CountdownView countdownView;
    @BindView(R.id.deliver_countdown_progress_view)
    DeliverCircleProgressView progressView;

    private long lastRemainTime = 0;
    private OnUpdateListener onUpdateListener;
    private boolean isResetCountdownView = true;

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
        float progress = 0;
        long countdownMillisecond = 0;
        if (deliverInfoBean.isTwiceConfirm()) {//二次确认订单，xx时xx分
            if (isResetCountdownView) {
                DynamicConfig.BackgroundInfo backgroundInfo = new DynamicConfig.BackgroundInfo();
                backgroundInfo.setShowTimeBgDivisionLine(false);
                DynamicConfig dynamicConfig = new DynamicConfig.Builder()
                        .setShowDay(true).setShowMinute(true).setShowHour(true).setShowSecond(false)
                        .setSuffixDay("天").setSuffixHour("时").setSuffixMinute("分")
                        .setSuffixLRMargin(UIUtils.dip2px(0)).setSuffixGravity(1)
                        .setSuffixTextColor(0xFF191919).setSuffixTextSize(11).setSuffixTextBold(true)
                        .setTimeTextColor(0xFF191919).setTimeTextSize(11).setTimeTextBold(true)
                        .setBackgroundInfo(backgroundInfo).build();
                countdownView.dynamicShow(dynamicConfig);
                isResetCountdownView = false;
            }

            progress =  360 - 360 * (deliverInfoBean.twiceCancelSpan / (float)deliverInfoBean.twiceCancelTotalSpan);
            countdownMillisecond = deliverInfoBean.twiceCancelSpan;
        } else {
            progress =  360 - 360 * (deliverInfoBean.deliverTimeSpan / (float)deliverInfoBean.stayTime);
            countdownMillisecond = deliverInfoBean.deliverTimeSpan;
        }

        progressView.setProgress(progress);
        lastRemainTime = countdownMillisecond;
        countdownView.start(countdownMillisecond);
        countdownView.setOnCountdownEndListener(this);
        setOnCountdownIntervalListener(1000, deliverInfoBean);
        countdownView.setVisibility(View.VISIBLE);
    }

    private void setOnCountdownIntervalListener(long interval, final DeliverInfoBean deliverInfoBean) {
        countdownView.setOnCountdownIntervalListener(interval, new CountdownView.OnCountdownIntervalListener() {
            @Override
            public void onInterval(CountdownView cv, long remainTime) {
                long stayTime = deliverInfoBean.isTwiceConfirm() ? deliverInfoBean.twiceCancelTotalSpan : deliverInfoBean.stayTime;

                if (stayTime > remainTime) {
                    float progress = 360 - 360 * (remainTime / (float)stayTime);
                    if (progress > 0) {
                        progressView.setProgress(progress);
                    }

                    long mInterval = 0;//控制刷新间隔时间
                    if (deliverInfoBean.isTwiceConfirm()) {
                        mInterval = OrderDetailDeliverItemView.LATE_INTERVAL;//1分刷新一次
                    } else if (deliverInfoBean.refreshCount >= 0) {//先每5秒请求一次并更新数据，连续请求6次后，恢复到1分钟请求一次
                        mInterval = OrderDetailDeliverItemView.FRE_INTERVAL;
                    } else {
                        mInterval = OrderDetailDeliverItemView.LATE_INTERVAL;//1分刷新一次
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
