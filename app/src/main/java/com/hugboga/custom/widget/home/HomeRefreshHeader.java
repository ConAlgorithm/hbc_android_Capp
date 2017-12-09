package com.hugboga.custom.widget.home;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.HomeTopBean;
import com.yan.pullrefreshlayout.PullRefreshLayout;
import com.yan.pullrefreshlayout.ViscousInterpolator;

import java.util.List;

/**
 * Created by qingcha on 17/11/20.
 */

public class HomeRefreshHeader extends HomeHeaderOrFooter{

//    private final String TWO_REFRESH_TEXT = CommonUtils.getString(R.string.home_top_up_detail);
    private final int REFRESH_FIRST_DURING = 180;
    private final int TWO_REFRESH_DURING = 400;

    private int twoRefreshDistance;
    private int firstRefreshTriggerDistance;
    private boolean isTwoRefresh;

    private ObjectAnimator alphaInAnimation;
    private ObjectAnimator alphaOutAnimation;
    private ValueAnimator translateYAnimation;

    private PullRefreshLayout pullRefreshLayout;
    private HomeHeaderView headerView;

    public HomeRefreshHeader(Context context, PullRefreshLayout pullRefreshLayout) {
        super(context);
        this.pullRefreshLayout = pullRefreshLayout;
        twoRefreshInit();
    }

    private void twoRefreshInit() {
        setLayoutParams(new ViewGroup.LayoutParams(-1, -1));

        firstRefreshTriggerDistance = HomeRefreshHeader.this.pullRefreshLayout.getRefreshTriggerDistance();
        twoRefreshDistance = firstRefreshTriggerDistance * 2;

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) rlContainer.getLayoutParams();
        layoutParams.gravity = Gravity.BOTTOM;
        rlContainer.setLayoutParams(layoutParams);

        headerView = new HomeHeaderView(getContext());
        headerView.setAlpha(0F);
        addView(headerView);
        animationInit();
        headerView.findViewById(R.id.home_header_close_iv).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onPullFinish();
                pullRefreshLayout.refreshComplete();
            }
        });
    }

    private void animationInit() {
        alphaInAnimation = ObjectAnimator.ofFloat(headerView, "alpha", 0, 1);
        alphaInAnimation.setDuration(TWO_REFRESH_DURING);
        alphaOutAnimation = ObjectAnimator.ofFloat(headerView, "alpha", 1, 0);
        alphaOutAnimation.setDuration(TWO_REFRESH_DURING);

        translateYAnimation = ValueAnimator.ofFloat(0, 0);
        translateYAnimation.setDuration(TWO_REFRESH_DURING);
        translateYAnimation.setInterpolator(new ViscousInterpolator());
        translateYAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float m = (float) animation.getAnimatedValue();
                pullRefreshLayout.moveChildren((int) m);
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!isTwoRefresh) {
            return super.dispatchTouchEvent(ev);
        }

        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_MOVE:
                if (translateYAnimation.isRunning()) {
                    translateYAnimation.cancel();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (pullRefreshLayout.getMoveDistance() <= getHeight() - firstRefreshTriggerDistance) {
                    pullRefreshLayout.refreshComplete();
                } else if (pullRefreshLayout.getMoveDistance() < getHeight()) {
                    translateYAnimation.setFloatValues(pullRefreshLayout.getMoveDistance(), getHeight());
                    translateYAnimation.start();
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onPullChange(float percent) {
        super.onPullChange(percent);
        String topUpDetailText = getContext().getResources().getString(R.string.home_top_up_detail);
        if (!pullRefreshLayout.isHoldingTrigger()) {
            if (pullRefreshLayout.getMoveDistance() >= twoRefreshDistance) {
                if (!tv.getText().toString().equals(topUpDetailText)) {
                    tv.setText(topUpDetailText);
                }
            } else if (tv.getText().toString().equals(topUpDetailText)) {
                tv.setText(getContext().getResources().getString(R.string.home_top_pull));//release loading
            }
        } else if (pullRefreshLayout.getMoveDistance() >= getHeight() && percent <= 1.0) {
            pullRefreshLayout.setDispatchPullTouchAble(true);
            isTwoRefresh = true;
        }
    }

    @Override
    public void onPullHolding() {
        super.onPullHolding();
        if (pullRefreshLayout.getMoveDistance() >= twoRefreshDistance) {
            pullRefreshLayout.setPullDownMaxDistance(getHeight() * 2);
            pullRefreshLayout.setRefreshTriggerDistance(getHeight());
            pullRefreshLayout.setRefreshAnimationDuring(TWO_REFRESH_DURING);
            pullRefreshLayout.setDispatchPullTouchAble(false);
            alphaInAnimation.start();
        }
    }

    @Override
    public void onPullFinish() {
        super.onPullFinish();
        if (isTwoRefresh) {
            isTwoRefresh = false;
            if (headerView.getAlpha() > 0) {
                alphaOutAnimation.start();
            }

            pullRefreshLayout.setPullDownMaxDistance(getHeight());
            pullRefreshLayout.setRefreshTriggerDistance(firstRefreshTriggerDistance);
            pullRefreshLayout.setRefreshAnimationDuring(REFRESH_FIRST_DURING);
            pullRefreshLayout.setDispatchPullTouchAble(true);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (alphaInAnimation.isRunning()) {
            alphaInAnimation.cancel();
        }
        if (alphaOutAnimation.isRunning()) {
            alphaOutAnimation.cancel();
        }
        if (translateYAnimation.isRunning()) {
            translateYAnimation.cancel();
        }
    }

    public boolean isTwoRefresh() {
        return isTwoRefresh;
    }

    public void update(List<HomeTopBean> homeTopBeanList) {
        if (headerView == null) {
            return;
        }
        headerView.update(homeTopBeanList);
    }

}
