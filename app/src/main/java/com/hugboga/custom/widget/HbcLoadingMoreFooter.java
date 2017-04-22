package com.hugboga.custom.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hugboga.custom.utils.UIUtils;
import com.jcodecraeer.xrecyclerview.LoadingMoreFooter;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.SimpleViewSwitcher;
import com.jcodecraeer.xrecyclerview.progressindicator.AVLoadingIndicatorView;

/**
 * Created by qingcha on 17/4/22.
 */
public class HbcLoadingMoreFooter extends LoadingMoreFooter {

    private SimpleViewSwitcher progressCon;
    private TextView mText;

    public HbcLoadingMoreFooter(Context context) {
        super(context);
    }

    public HbcLoadingMoreFooter(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    public void initView() {
        setGravity(Gravity.CENTER);
        setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIUtils.dip2px(45)));
        progressCon = new SimpleViewSwitcher(getContext());
        progressCon.setLayoutParams(new ViewGroup.LayoutParams(UIUtils.dip2px(25), UIUtils.dip2px(25)));

        AVLoadingIndicatorView progressView = new AVLoadingIndicatorView(this.getContext());
        progressView.setIndicatorColor(0xffB5B5B5);
        progressView.setIndicatorId(ProgressStyle.BallSpinFadeLoader);
        progressCon.setView(progressView);

        addView(progressCon);
        mText = new TextView(getContext());
        mText.setTextColor(0xffB5B5B5);
        mText.setText("正在加载...");
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(UIUtils.dip2px(8), 0, 0, 0 );

        mText.setLayoutParams(layoutParams);
        addView(mText);
    }

    @Override
    public void setProgressStyle(int style) {
        if (style == ProgressStyle.SysProgress) {
            progressCon.setView(new ProgressBar(getContext(), null, android.R.attr.progressBarStyle));
        } else {
            AVLoadingIndicatorView progressView = new  AVLoadingIndicatorView(this.getContext());
            progressView.setIndicatorColor(0xffB5B5B5);
            progressView.setIndicatorId(style);
            progressCon.setView(progressView);
        }
    }

    @Override
    public void setState(int state) {
        switch(state) {
            case STATE_LOADING:
                progressCon.setVisibility(View.VISIBLE);
                this.setVisibility(View.VISIBLE);
                break;
            case STATE_COMPLETE:
                this.setVisibility(View.GONE);
                break;
            case STATE_NOMORE:
                this.setVisibility(View.GONE);
                break;
        }
    }
}