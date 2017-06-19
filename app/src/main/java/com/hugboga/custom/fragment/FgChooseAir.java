package com.hugboga.custom.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.utils.CommonUtils;


import butterknife.Bind;
import butterknife.OnClick;

public class FgChooseAir extends BaseFragment{

    @Bind(R.id.daily_tap_1)
    TextView dailyTap1;
    @Bind(R.id.daily_tap_line1)
    View dailyTapLine1;
    @Bind(R.id.daily_layout_1)
    RelativeLayout dailyLayout1;
    @Bind(R.id.daily_tap_2)
    TextView dailyTap2;
    @Bind(R.id.daily_tap_line2)
    View dailyTapLine2;
    @Bind(R.id.daily_layout_2)
    RelativeLayout dailyLayout2;

    @Bind(R.id.choose_content)
    FrameLayout choose_content;

    Fragment currentFragment;

    @Override
    public int getContentViewId() {
        return R.layout.fg_choose_air;
    }

    @Override
    protected void initView() {
        setCurrentFragment(2);
    }

    @OnClick({R.id.daily_layout_1, R.id.daily_layout_2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.daily_layout_1:
                setCurrentFragment(1);
                break;
            case R.id.daily_layout_2:
                setCurrentFragment(2);
                break;
        }
    }

    public void setCurrentFragment(int index) {
        if (getContext() instanceof Activity) {
            CommonUtils.hideSoftInput((Activity) getContext());
        }
        selectTap(index);
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

    private void selectTap(int index) {
        if (index == 2) {
            dailyTapLine1.setVisibility(View.GONE);
            dailyTapLine2.setVisibility(View.VISIBLE);
            dailyTap1.setTextColor(getResources().getColor(R.color.common_font_air));
            dailyTap2.setTextColor(getResources().getColor(R.color.common_font_color_black));
        } else {
            dailyTapLine1.setVisibility(View.VISIBLE);
            dailyTapLine2.setVisibility(View.GONE);
            dailyTap1.setTextColor(getResources().getColor(R.color.common_font_color_black));
            dailyTap2.setTextColor(getResources().getColor(R.color.common_font_air));
        }
    }
}
