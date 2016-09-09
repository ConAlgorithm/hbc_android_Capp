package com.hugboga.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
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
public class OrderDetailDeliverItemView extends LinearLayout implements HbcViewBehavior{

    @Bind(R.id.eliver_item_loading_view)
    DeliverLoadingView loadingView;
    @Bind(R.id.eliver_item_countdown_view)
    OrderDetailDeliverCountDownView countdownLayout;

    @Bind(R.id.deliver_item_title_tv)
    TextView titleTV;

    @Bind(R.id.deliver_item_subtitle_tv)
    TextView subtitleIV;
    @Bind(R.id.deliver_item_guide_avatar_layout)
    LinearLayout avatarLayout;


    public OrderDetailDeliverItemView(Context context) {
        this(context, null);
    }

    public OrderDetailDeliverItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        final View view = inflate(context, R.layout.view_deliver_item, this);
        ButterKnife.bind(view);
        loadingView.setVisibility(View.GONE);
    }

    @Override
    public void update(Object _data) {
        DeliverInfoBean deliverInfoBean = (DeliverInfoBean) _data;
        if (deliverInfoBean == null) {
            return;
        }

        //3种UI样式
        switch (deliverInfoBean.deliverStatus) {
            case DeliverInfoBean.DeliverStatus.BILLED:          // 已发单
            case DeliverInfoBean.DeliverStatus.INFORMED:        // 已通知司导 count
            case DeliverInfoBean.DeliverStatus.INFORMED_GUIDE:  // 已通知该司导 count
                countdownLayout(deliverInfoBean);
                break;
            case DeliverInfoBean.DeliverStatus.BEING_ARRANGED:  // 正在安排司导
            case DeliverInfoBean.DeliverStatus.COORDINATION:    // 为您协调司导
                loadingLayout(deliverInfoBean);
                break;
            case DeliverInfoBean.DeliverStatus.COMMITTED:       // 有司导表态  司导
                guideAvatarListLayout(deliverInfoBean);
                break;
        }
    }

    private void loadingLayout(DeliverInfoBean deliverInfoBean) {
        loadingView.setVisibility(View.VISIBLE);
        countdownLayout.setVisibility(View.GONE);
        avatarLayout.removeAllViews();
        avatarLayout.setVisibility(View.GONE);
        subtitleIV.setVisibility(View.VISIBLE);

        titleTV.setText(deliverInfoBean.deliverMessage);
        subtitleIV.setText(deliverInfoBean.deliverDetail);

    }

    private void countdownLayout(DeliverInfoBean deliverInfoBean) {
        loadingView.setVisibility(View.GONE);
        countdownLayout.setVisibility(View.VISIBLE);
        avatarLayout.removeAllViews();
        avatarLayout.setVisibility(View.GONE);
        subtitleIV.setVisibility(View.VISIBLE);

        countdownLayout.update(deliverInfoBean);
        titleTV.setText(deliverInfoBean.deliverMessage);
        subtitleIV.setText(deliverInfoBean.deliverDetail);
    }

    private void guideAvatarListLayout(DeliverInfoBean deliverInfoBean) {
        loadingView.setVisibility(View.GONE);
        countdownLayout.setVisibility(View.VISIBLE);
        avatarLayout.setVisibility(View.VISIBLE);
        subtitleIV.setVisibility(View.GONE);

        titleTV.setText(deliverInfoBean.deliverMessage);
//      avatarLayout.removeAllViews();
//      avatarLayout.
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (loadingView != null) {
            loadingView.onStop();
        }
    }

    public void setOnCountdownEndListener(CountdownView.OnCountdownEndListener onCountdownEndListener) {
        countdownLayout.setOnCountdownEndListener(onCountdownEndListener);
    }
}
