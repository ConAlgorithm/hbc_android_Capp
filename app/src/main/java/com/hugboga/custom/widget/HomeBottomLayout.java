package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.DailyWebInfoActivity;
import com.hugboga.custom.activity.PickSendActivity;
import com.hugboga.custom.activity.SingleNewActivity;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.data.bean.HomeData;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.fragment.FgHome;
import com.hugboga.custom.fragment.FgLogin;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by qingcha on 16/6/19.
 */
public class HomeBottomLayout extends LinearLayout implements View.OnClickListener{

    private FgHome fragment;

    private LinearLayout charteredLayout;
    private TextView pickupTV, singleTV, activeTV;
    private ImageView activeIV;

    private HomeData.SalesPromotion activeData;

    public HomeBottomLayout(Context context) {
        this(context, null);
    }

    public HomeBottomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        final int paddingLeft = getContext().getResources().getDimensionPixelOffset(R.dimen.home_view_padding_left);
        setOrientation(LinearLayout.VERTICAL);
        setPadding(paddingLeft, 0, paddingLeft, UIUtils.dip2px(10));

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

        int itemWidth = UIUtils.getScreenWidth() - paddingLeft * 2;

        int charteredLayoutHeight = (int)((111 / 497.0) * itemWidth);
        charteredLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, charteredLayoutHeight));

        float pickupTVWidth = (itemWidth - UIUtils.dip2px(10))/ 2.0f;
        float pickupTVHight = (int)((86 / 241.0) * pickupTVWidth);
        LinearLayout.LayoutParams pickupParams= new LinearLayout.LayoutParams((int)pickupTVWidth, (int)pickupTVHight);
        pickupTV.setLayoutParams(pickupParams);
        singleTV.setLayoutParams(pickupParams);

        int activeImgHeight = (int)((254 / 690.0) * itemWidth);
        LinearLayout.LayoutParams activeImgParams = new LinearLayout.LayoutParams(itemWidth, activeImgHeight);
        activeImgParams.bottomMargin = UIUtils.dip2px(5);
        activeIV.setLayoutParams(activeImgParams);
    }

    /**
     * 为了跳转使用
     * */
    public void setFragment(FgHome _fragment) {
        this.fragment = _fragment;
    }

    @Override
    public void onClick(View v) {
        if (fragment == null) {
            return;
        }
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
                if (!UserEntity.getUser().isLogin(fragment.getActivity())) {
                    CommonUtils.showToast(R.string.login_hint);
                    Bundle bundle = new Bundle();;
                    bundle.putString("source", "首页");
                    fragment.startFragment(new FgLogin(), bundle);
                    break;
                }
                if (activeData == null || TextUtils.isEmpty(activeData.getUrlAddress())) {
                    return;
                }
                String urlAddress = activeData.getUrlAddress();
                if (urlAddress.lastIndexOf("?") != urlAddress.length() - 1) {
                    urlAddress = urlAddress + "?";
                }

                urlAddress = urlAddress + "userId="+ UserEntity.getUser().getUserId(fragment.getContext())+"&t=" + new Random().nextInt(100000);

//                Bundle bundle = new Bundle();
//                bundle.putString(FgWebInfo.WEB_URL, urlAddress);
//                fragment.startFragment(new FgDailyWeb(), bundle);

                Intent intent = new Intent(v.getContext(), WebInfoActivity.class);
                intent.putExtra(WebInfoActivity.WEB_URL, urlAddress);
                v.getContext().startActivity(intent);

                break;
        }
    }

    public void setSalesPromotion(HomeData.SalesPromotion data) {
        if (data == null || TextUtils.isEmpty(data.getPicture())) {
            activeTV.setVisibility(View.GONE);
            activeIV.setVisibility(View.GONE);
        } else {
            this.activeData = data;
            activeTV.setVisibility(View.VISIBLE);
            activeIV.setVisibility(View.VISIBLE);
            Tools.showImage(activeIV, data.getPicture());
        }
    }

    /**
     * 以下代码copy自久版本首页
     * */
    private void goPickSend(){
//        Bundle bundle = new Bundle();
//
//        FgPickSend fgPickSend = new FgPickSend();
//        bundle.putString("source", "首页");
//        fgPickSend.setArguments(bundle);
//        fragment.startFragment(fgPickSend, bundle);
//        HashMap<String,String> map = new HashMap<String,String>();
//        map.put("source", "首页");
//        MobclickAgent.onEvent(fragment.getActivity(), "chose_pndairport", map);

        Intent intent = new Intent(getContext(), PickSendActivity.class);
        intent.putExtra(WebInfoActivity.WEB_URL, UrlLibs.H5_DAIRY);
        this.getContext().startActivity(intent);

    }

    private void goDairy(){
        Bundle bundle = new Bundle();
        HashMap<String,String> map = new HashMap<String,String>();
//        OrderSelectCityActivity fgOrderSelectCity = new OrderSelectCityActivity();
        bundle.putString("source","首页");
        Intent intent = new Intent(this.getContext(), DailyWebInfoActivity.class);
        intent.putExtras(bundle);
//        fgOrderSelectCity.setIntent(intent);
//        fragment.startFragment(fgOrderSelectCity, bundle);

//        bundle.putString(FgWebInfo.WEB_URL, UrlLibs.H5_DAIRY);
//        fragment.startFragment(new FgDailyWeb(), bundle);

        intent.putExtra(WebInfoActivity.WEB_URL, UrlLibs.H5_DAIRY);
        this.getContext().startActivity(intent);


        map.put("source", "首页");
        MobclickAgent.onEvent(fragment.getActivity(), "chose_oneday", map);
    }

    private void goSingle(){
//        FgSingleNew fgSingleNew = new FgSingleNew();
//        Bundle bundle = new Bundle();
//        fgSingleNew.setArguments(bundle);
//        fragment.startFragment(fgSingleNew);
//        HashMap<String,String> map = new HashMap<String,String>();
//        map.put("source", "首页");
//        MobclickAgent.onEvent(fragment.getActivity(), "chose_oneway", map);

        Intent intent = new Intent(getContext(),SingleNewActivity.class);
        getContext().startActivity(intent);
    }
}
