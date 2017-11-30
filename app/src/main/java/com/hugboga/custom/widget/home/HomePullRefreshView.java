package com.hugboga.custom.widget.home;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.yan.pullrefreshlayout.PullRefreshLayout;

/**
 * Created by qingcha on 17/11/25.
 */

public class HomePullRefreshView extends NestedFrameLayout implements PullRefreshLayout.OnPullListener {

    public HomePullRefreshView(Context context) {
        super(context);
        if (contentView() == -1) {
            throw new RuntimeException("must override method contentView");
        }
        LayoutInflater.from(getContext()).inflate(contentView(), this, true);
        initView();
    }

    public HomePullRefreshView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HomePullRefreshView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected int contentView() {
        return -1;
    }

    protected void initView() {
    }

    @Override
    public void onPullChange(float percent) {
    }

    @Override
    public void onPullHoldTrigger() {
    }

    @Override
    public void onPullHoldUnTrigger() {
    }

    @Override
    public void onPullHolding() {
    }

    @Override
    public void onPullFinish() {
    }

    @Override
    public void onPullReset() {
    }

}
