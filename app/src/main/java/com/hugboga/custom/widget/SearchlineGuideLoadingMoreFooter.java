package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.TravelFundActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.utils.UIUtils;
import com.jcodecraeer.xrecyclerview.LoadingMoreFooter;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.SimpleViewSwitcher;
import com.jcodecraeer.xrecyclerview.progressindicator.AVLoadingIndicatorView;

/**
 * Created by zhangqiang on 17/8/24.
 */

public class SearchlineGuideLoadingMoreFooter extends LoadingMoreFooter {

    private SimpleViewSwitcher progressCon;
    private TextView mText;
    LayoutInflater inflater;
    public SearchlineGuideLoadingMoreFooter(Context context) {
        super(context);
    }

    public SearchlineGuideLoadingMoreFooter(Context context, AttributeSet attrs) {
        super(context, attrs);

    }
    public void setCustomlayout(LayoutInflater inflater){
        this.inflater = inflater;
    }
    @Override
    public void initView() {
        setGravity(Gravity.CENTER_HORIZONTAL);
        setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIUtils.dip2px(120)));
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

        //addView(getFooterView(this.inflater));
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
                mText.setText("正在加载...");
                this.setVisibility(View.VISIBLE);
                break;
            case STATE_COMPLETE:
                this.setVisibility(View.GONE);
                break;
            case STATE_NOMORE:
                progressCon.setVisibility(View.GONE);
                mText.setText("没有更多了");
                mText.setVisibility(GONE);
                this.setVisibility(View.VISIBLE);
                break;
        }
    }

    public String getEventSource(){
        return "";
    }
}
