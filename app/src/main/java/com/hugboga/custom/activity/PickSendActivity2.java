package com.hugboga.custom.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.hugboga.custom.R;
import com.hugboga.custom.fragment.FgPickup;
import com.hugboga.custom.fragment.FgSend;
import com.hugboga.custom.utils.OrderUtils;
import com.hugboga.custom.widget.DialogUtil;
import com.hugboga.custom.widget.title.TitleBarPickSend;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/5/17.
 */
public class PickSendActivity2 extends BaseActivity implements TitleBarPickSend.TitlerBarOnClickLister {

    @Bind(R.id.pick_send_titlebar)
    TitleBarPickSend titlebar;

    private Fragment currentFragment;

    @Override
    public int getContentViewId() {
        return R.layout.activity_pick_send;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    public void initView() {
        titlebar.setTitlerBarOnClickLister(this);
        titlebar.onTabSelected(0);
    }

    @Override
    public void onBack() {
        if (!isShowSaveDialog()) {
            finish();
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            if (isShowSaveDialog()) {
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    public boolean isShowSaveDialog() {
        boolean isShowDialog = false;
        if (currentFragment instanceof FgPickup) {
            isShowDialog = !((FgPickup) currentFragment).isFlightBeanNull();
        } else if (currentFragment instanceof FgSend) {
            isShowDialog = !((FgSend) currentFragment).isAirPortNull();
        }
        if (isShowDialog) {
            OrderUtils.showSaveDialog(this);
        }
        return isShowDialog;
    }

    @Override
    public void onCustomerService() {
        DialogUtil.getInstance(PickSendActivity2.this).showServiceDialog(PickSendActivity2.this, null, UnicornServiceActivity.SourceType.TYPE_CHARTERED, null, null, getEventSource());
    }

    @Override
    public void onTabSelected(int index) {
        changeFragment(index == 0 ? FgPickup.TAG : FgSend.TAG);
    }

    public void changeFragment(String tag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (currentFragment != null) {
            ft.hide(currentFragment);
        }
        if (fragment == null) {
            if (FgPickup.TAG.equals(tag)) {
                fragment = new FgPickup();
            } else if (FgSend.TAG.equals(tag)) {
                fragment = new FgSend();
            }
            ft.add(R.id.pick_send_container_layout, fragment, tag);
        } else {
            ft.show(fragment);
        }
        currentFragment = fragment;
        ft.commitAllowingStateLoss();
    }
}
