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
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.UIUtils;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.SimpleViewSwitcher;
import com.jcodecraeer.xrecyclerview.progressindicator.AVLoadingIndicatorView;

/**
 * Created by zhangqiang on 17/6/27.
 */

public class TravelLoadingMoreFooter extends HbcLoadingMoreFooter {
    private SimpleViewSwitcher progressCon;
    private TextView mText;
    LayoutInflater inflater;
    private View footerView;

    public TravelLoadingMoreFooter(Context context) {
        super(context);
    }

    public TravelLoadingMoreFooter(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public void setCustomlayout(LayoutInflater inflater) {
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
        layoutParams.setMargins(UIUtils.dip2px(8), 0, 0, 0);

        mText.setLayoutParams(layoutParams);
        addView(mText);

        //addView(getFooterView(this.inflater));
    }

    @Override
    public void setProgressStyle(int style) {
        if (style == ProgressStyle.SysProgress) {
            progressCon.setView(new ProgressBar(getContext(), null, android.R.attr.progressBarStyle));
        } else {
            AVLoadingIndicatorView progressView = new AVLoadingIndicatorView(this.getContext());
            progressView.setIndicatorColor(0xffB5B5B5);
            progressView.setIndicatorId(style);
            progressCon.setView(progressView);
        }
    }

    @Override
    public void setState(int state) {
        switch (state) {
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
                addView(getFooterView(this.inflater));
                this.setVisibility(View.VISIBLE);
                break;
        }
    }

    protected View getFooterView(LayoutInflater inflater) {
        footerView = inflater.inflate(R.layout.view_travel_footer, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        footerView.setLayoutParams(params);
        intentTravelFundActivity(footerView);
        return footerView;
    }

    protected void intentTravelFundActivity(View view) {
        view.findViewById(R.id.travel_footer_get_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SensorsUtils.onAppClick("行程", "旅游基金-点击领取", "");
                MobClickUtils.onEvent(StatisticConstant.CLICK_TRAVELFOUND_XC);
                Intent intent = new Intent(getContext(), WebInfoActivity.class);
                intent.putExtra(WebInfoActivity.WEB_URL, UrlLibs.H5_INVITE_FRIEND);
                intent.putExtra(Constants.PARAMS_SOURCE, "行程");
                getContext().startActivity(intent);

            }
        });

    }

    public void setFooterContent(String str) {
        if (footerView == null) return;
        TextView textView = footerView.findViewById(R.id.travel_footer_text_layout);
        textView.setText("".equals(str) ? getResources().getText(R.string.travel_footer_fund_content) : str);
    }

}
