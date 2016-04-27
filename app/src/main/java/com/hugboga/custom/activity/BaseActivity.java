package com.hugboga.custom.activity;

import android.os.Bundle;

import com.huangbaoche.hbcframe.activity.BaseFragmentActivity;
import com.hugboga.custom.utils.ChannelUtils;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;


public class BaseActivity extends BaseFragmentActivity {
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        AnalyticsConfig.setChannel("channel"+ChannelUtils.getChannel(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
