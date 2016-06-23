package com.hugboga.custom.widget;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hugboga.custom.MainActivity;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.fragment.FgChooseCity;
import com.hugboga.custom.fragment.FgHome;
import com.hugboga.custom.utils.UIUtils;

/**
 * Created by qingcha on 16/6/21.
 */
public class HomeSearchView extends RelativeLayout implements View.OnClickListener, RevealBackgroundView.OnStateChangeListener {

    private ImageView sideMenuIV, searchIV;
    private LinearLayout searchLayout;
    private FgHome fragment;
    private RevealBackgroundView revealView;
    private int[] startLocation;
    private boolean isHide = false;

    public HomeSearchView(Context context) {
        this(context, null);
    }

    public HomeSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.view_home_search, this);
        sideMenuIV = (ImageView) findViewById(R.id.home_side_menu_iv);
        searchIV = (ImageView) findViewById(R.id.home_search_iv);
        searchLayout = (LinearLayout) findViewById(R.id.home_search_layout);
        revealView = (RevealBackgroundView) findViewById(R.id.home_reveal_view);
        revealView.setOnStateChangeListener(this);
        sideMenuIV.setOnClickListener(this);
        searchIV.setOnClickListener(this);
        searchLayout.setOnClickListener(this);
        startLocation = new int[2];
    }

    /**
     * 为了跳转使用
     * */
    public void setFragment(FgHome _fragment) {
        this.fragment = _fragment;
    }

    public void setSearchLayoutHide(boolean _isHide) {
        this.isHide = _isHide;
        if (isHide) {
            setBackgroundColor(0x00000000);
            searchIV.setVisibility(View.VISIBLE);
            searchLayout.setVisibility(View.GONE);
            revealView.setVisibility(View.GONE);
        } else {
            setBackgroundColor(0xFF2D2B28);
            searchIV.setVisibility(View.GONE);
            revealView.setVisibility(View.VISIBLE);
            startLocation[0] = (int) searchIV.getX();
            startLocation[1] = (int) searchIV.getY();
            revealView.startFromLocation(startLocation);
        }
    }

    public ImageView getSearchIV() {
        return searchIV;
    }

    public LinearLayout getSearchLayout() {
        return searchLayout;
    }

    @Override
    public void onClick(View myView) {
        if (fragment == null) {
            return;
        }
        switch (myView.getId()) {
            case R.id.home_side_menu_iv:
                ((MainActivity) fragment.getActivity()).openDrawer();
                break;
            case R.id.home_search_iv:
            case R.id.home_search_layout:
                Bundle bundle = new Bundle();
                bundle.putInt("com.hugboga.custom.home.flush", Constants.BUSINESS_TYPE_HOME);
                bundle.putString("source","小搜索按钮");
                fragment.startFragment(new FgChooseCityNew(), bundle);
                break;
        }
    }

    @Override
    public void onStateChange(int state) {
        if (state == revealView.STATE_FINISHED && !isHide) {
            searchLayout.setVisibility(View.VISIBLE);
            searchLayout.setBackgroundResource(R.drawable.shape_home_search_bg);
        }
    }
}
