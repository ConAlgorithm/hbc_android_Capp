package com.hugboga.custom.fragment;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.HomeAdapter;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.statistic.sensors.SensorsConstant;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.utils.WrapContentLinearLayoutManager;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by qingcha on 17/11/22.
 */

public class FgHome extends BaseFragment{

    @BindView(R.id.home_list_view)
    RecyclerView homeListView;

    HomeAdapter homeAdapter;

    @Override
    public int getContentViewId() {
        return R.layout.fg_home;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        setSensorsDefaultEvent(getEventSource(), SensorsConstant.DISCOVERY);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        setSensorsViewScreenBeginEvent();
    }

    @Override
    public void onStop() {
        super.onStop();
        setSensorsViewScreenEndEvent();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initView() {
        homeAdapter = new HomeAdapter();
        WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(this.getActivity());
        homeListView.setLayoutManager(layoutManager);
        homeListView.setHasFixedSize(true);
        homeListView.setAdapter(homeAdapter);
    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case CLICK_USER_LOGIN:
                break;
        }
    }

    private void setSensorsViewScreenBeginEvent() {
        try {
            SensorsDataAPI.sharedInstance(getContext()).trackTimerBegin("AppViewScreen");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setSensorsViewScreenEndEvent() {
        try {
            JSONObject properties = new JSONObject();
            properties.put("pageName", getEventSource());
            properties.put("pageTitle", getEventSource());
            properties.put("refer", "");
            SensorsDataAPI.sharedInstance(getContext()).trackTimerEnd("AppViewScreen", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
