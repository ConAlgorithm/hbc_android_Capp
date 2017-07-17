package com.hugboga.custom.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.SharedPre;


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
        //需要判断之前是否填写过航班类型,按航班号查询=1,按城市查询=2,默认是航班号
        SharedPre sharedPre = new SharedPre(getContext());
        int chooseAirType = sharedPre.getIntValue("chooseAirType",1);
        setCurrentFragment(chooseAirType);
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
                    fragment = new FgChooseAirNumber();
                    break;
                case 2:
                    fragment = new FgChooseAirAddress();
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
