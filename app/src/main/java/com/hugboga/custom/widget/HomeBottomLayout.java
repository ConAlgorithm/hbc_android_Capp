package com.hugboga.custom.widget;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.fragment.FgHome;
import com.hugboga.custom.fragment.FgOrderSelectCity;
import com.hugboga.custom.fragment.FgPickSend;
import com.hugboga.custom.fragment.FgSingleNew;
import com.hugboga.custom.utils.UIUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

/**
 * Created by qingcha on 16/6/19.
 */
public class HomeBottomLayout extends LinearLayout implements View.OnClickListener{

    private LinearLayout charteredLayout;
    private TextView pickupTV, singleTV, activeTV;
    private ImageView activeIV;

    private FgHome fragment;

    public HomeBottomLayout(Context context) {
        this(context, null);
    }

    public HomeBottomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        final int paddingLeft = getContext().getResources().getDimensionPixelOffset(R.dimen.home_view_padding_left);
        setOrientation(LinearLayout.VERTICAL);
        setPadding(paddingLeft, 0, paddingLeft, 0);

        inflate(getContext(), R.layout.view_home_bottom, this);
        charteredLayout = (LinearLayout) findViewById(R.id.home_bottom_chartered_layout);
        pickupTV = (TextView) findViewById(R.id.home_bottom_pickup_tv);
        singleTV = (TextView) findViewById(R.id.home_bottom_single_tv);
        activeTV = (TextView) findViewById(R.id.home_bottom_active_tv);
        activeIV = (ImageView) findViewById(R.id.home_bottom_active_iv);

        charteredLayout.setOnClickListener(this);
        pickupTV.setOnClickListener(this);
        singleTV.setOnClickListener(this);
        activeIV.setOnClickListener(this);

        int charteredLayoutHeight = (int)((111 / 497.0) * (UIUtils.getScreenWidth() - paddingLeft * 2));
        charteredLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, charteredLayoutHeight));

        float pickupTVWidth = (UIUtils.getScreenWidth() - paddingLeft * 2 - UIUtils.dip2px(10))/ 2.0f;
        float pickupTVHight = (int)((86 / 241.0) * pickupTVWidth);
        LinearLayout.LayoutParams pickupParams= new LinearLayout.LayoutParams((int)pickupTVWidth, (int)pickupTVHight);
        pickupTV.setLayoutParams(pickupParams);
        singleTV.setLayoutParams(pickupParams);
    }

    public void setFragment(FgHome _fragment) {
        this.fragment = _fragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_bottom_chartered_layout://包车
                goDairy();
                break;
            case R.id.home_bottom_pickup_tv://中文接送机
                goPickSend();
                break;
            case R.id.home_bottom_single_tv://单次接送
                goSingle();
                break;
            case R.id.home_bottom_active_iv:
                break;
        }
    }

    /**
     * 以下代码copy自久版本首页
     * */
    private void goPickSend(){
        Bundle bundle = new Bundle();

        FgPickSend fgPickSend = new FgPickSend();
        bundle.putString("source", "首页");
        fgPickSend.setArguments(bundle);
        fragment.startFragment(fgPickSend, bundle);
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("source", "首页");
        MobclickAgent.onEvent(fragment.getActivity(), "chose_pndairport", map);
    }

    private void goDairy(){
        Bundle bundle = new Bundle();
        HashMap<String,String> map = new HashMap<String,String>();
        FgOrderSelectCity fgOrderSelectCity = new FgOrderSelectCity();
        bundle.putString("source","首页");
        fgOrderSelectCity.setArguments(bundle);
        fragment.startFragment(fgOrderSelectCity, bundle);
        map.put("source", "首页");
        MobclickAgent.onEvent(fragment.getActivity(), "chose_oneday", map);
    }

    private void goSingle(){
        FgSingleNew fgSingleNew = new FgSingleNew();
        Bundle bundle = new Bundle();
        fgSingleNew.setArguments(bundle);
        fragment.startFragment(fgSingleNew);
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("source", "首页");
        MobclickAgent.onEvent(fragment.getActivity(), "chose_oneway", map);
    }
}
