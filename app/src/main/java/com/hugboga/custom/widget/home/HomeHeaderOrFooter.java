package com.hugboga.custom.widget.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * Created by qingcha on 17/11/20.
 */

public class HomeHeaderOrFooter extends HomePullRefreshView {
    protected TextView tv;
    private AVLoadingIndicatorView loadingView;
    protected FrameLayout rlContainer;

    private boolean isStateFinish;
    private boolean isHolding;

    public HomeHeaderOrFooter(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(getContext()).inflate(contentView(), this, true);
        initView();
        loadingView.setIndicator("LineScaleIndicator");
        loadingView.setIndicatorColor(0xFFCDCDCD);
        tv.setTextColor(0xFFCDCDCD);
    }

    public HomeHeaderOrFooter(Context context) {
        this(context, "LineScaleIndicator", 0xFFCDCDCD, false);
    }

    public HomeHeaderOrFooter(Context context, String animationName) {
        this(context, animationName, 0xFFCDCDCD, false);
    }

    public HomeHeaderOrFooter(Context context, String animationName, int color) {
        this(context, animationName, color, true);
    }

    public HomeHeaderOrFooter(Context context, String animationName, int color, boolean withBg) {
        super(context);
        loadingView.setIndicator(animationName);
        loadingView.setIndicatorColor(color);
        tv.setTextColor(color);
        if (withBg) {
            setBackgroundColor(0xFFCDCDCD);
        }
    }

    public void setTv(String title) {
        if (tv!=null) {
            tv.setText(title);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        loadingView.smoothToHide();
        loadingView.clearAnimation();
    }

    @Override
    protected int contentView() {
        return R.layout.view_home_refresh;
    }

    @Override
    protected void initView() {
        rlContainer = (FrameLayout) findViewById(R.id.rl_container);
        tv = (TextView) findViewById(R.id.title);
        loadingView = (AVLoadingIndicatorView) findViewById(R.id.loading_view);
    }

    @Override
    public void onPullChange(float percent) {
        super.onPullChange(percent);
        if (isStateFinish || isHolding) return;
        percent = Math.abs(percent);
        if (percent > 0.2 && percent < 1) {
            if (loadingView.getVisibility() != VISIBLE) {
                loadingView.smoothToShow();
                Log.i("aa","onPullChange  smoothToShow  11");
            }
            if (percent < 1) {
                loadingView.setScaleX(percent);
                loadingView.setScaleY(percent);
            }
        } else if (percent <= 0.2 && loadingView.getVisibility() == VISIBLE) {
            loadingView.smoothToHide();
            Log.i("aa","onPullChange  smoothToHide 22");
        } else if (loadingView.getScaleX() != 1) {
            loadingView.setScaleX(1f);
            loadingView.setScaleY(1f);
        }
    }

    @Override
    public void onPullHoldTrigger() {
        super.onPullHoldTrigger();
        tv.setText("继续拖动，新世界");//release loading
    }

    @Override
    public void onPullHoldUnTrigger() {
        super.onPullHoldUnTrigger();
        tv.setText("继续拖动，新世界");//drag
    }

    @Override
    public void onPullHolding() {
        super.onPullHolding();
        isHolding = true;
//        tv.setText("");//loading...
        rlContainer.setVisibility(GONE);
    }

    @Override
    public void onPullFinish() {
        super.onPullFinish();
        tv.setText("继续拖动，新世界");//loading finish
        isStateFinish = true;
        loadingView.smoothToHide();
        Log.i("aa","onPullChange  onPullFinish");
    }

    @Override
    public void onPullReset() {
        super.onPullReset();
        tv.setText("继续拖动，新世界");//drag
        isStateFinish = false;
        isHolding = false;
        Log.i("aa","onPullChange  onPullReset");
        rlContainer.setVisibility(VISIBLE);

    }
}
