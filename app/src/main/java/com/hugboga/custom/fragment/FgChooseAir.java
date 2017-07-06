package com.hugboga.custom.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.utils.CommonUtils;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import butterknife.Bind;

public class FgChooseAir extends BaseFragment{

    @Bind(R.id.choose_content)
    FrameLayout choose_content;

    Fragment currentFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case CHOOSE_AIR_FRAGMENT:
                setCurrentFragment((int) action.getData());
                break;
        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.fg_choose_air;
    }

    @Override
    protected void initView() {
        setCurrentFragment(2);
    }

    public void setCurrentFragment(int index) {
        if (getContext() instanceof Activity) {
            CommonUtils.hideSoftInput((Activity) getContext());
        }
        String tag = "" + index;
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        Fragment fragment = getChildFragmentManager().findFragmentByTag(tag);
        if (currentFragment != null) {
            ft.hide(currentFragment);
        }
        if (fragment == null) {
            switch (index) {
                case 1:
                    fragment = new FgChooseAirAddress();
                    break;
                case 2:
                    fragment = new FgChooseAirNumber();
                    break;
            }
            ft.add(R.id.choose_content, fragment, tag);
        } else {
            ft.show(fragment);
        }
        currentFragment = fragment;
        ft.commitAllowingStateLoss();
    }
}
